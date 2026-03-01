package org.lud.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.lud.engine.core.AudioService;
import org.lud.engine.enums.Direction;
import org.lud.engine.enums.LastInput;
import org.lud.engine.gui.Button;
import org.lud.engine.gui.Colors;
import org.lud.engine.gui.Menu;
import org.lud.engine.input.BoardInput;
import org.lud.engine.input.Coordinator;
import org.lud.game.data.ButtonData;
import org.lud.game.data.Tooltip;
import org.lud.game.entities.Piece;
import org.lud.game.enums.UIButton;
import org.lud.game.service.BoardService;
import org.lud.game.service.GameService;
import org.lud.game.service.PieceService;

import java.util.ArrayList;
import java.util.List;

public class BoardScreen extends Menu {
    private static final int TILE_SIZE = 64;
    private static final int BOARD_SIZE = TILE_SIZE * 8;
    private static final int PADDING = 32;

    private Coordinator coordinator;
    private final BoardInput boardInput;
    private InputMultiplexer multiplexer;

    private final BoardService boardService;
    private final GameService gameService;
    private final PieceService pieceService;
    private final AudioService audioService;

    private final List<ButtonData> data;
    private final Tooltip tooltip;
    private ShapeRenderer shaper;

    private Texture baseButton;
    private Texture frame;

    private final float startX;
    private final float startY;

    private boolean isCursorActive;

    public BoardScreen(BoardService boardService, GameService gameService, PieceService pieceService,
                       AudioService audioService) {
        super(gameService, audioService);
        this.boardService = boardService;
        this.gameService = gameService;
        this.pieceService = pieceService;
        this.audioService = audioService;
        this.data = new ArrayList<>();
        this.tooltip = new Tooltip("", getFont());

        this.startX = (Gdx.graphics.getWidth() - BOARD_SIZE)/2f;
        this.startY = (Gdx.graphics.getHeight() - BOARD_SIZE)/2f;

        this.boardInput = new BoardInput(pieceService, gameService, boardService,
            audioService, startX, startY, TILE_SIZE);

        addMenu(this);
        loadSprites();
    }

    public void loadSprites() {
        String defaultPath = "buttons/";
        this.baseButton = new Texture(defaultPath + "button_small.png");
        this.frame = new Texture(defaultPath + "button_small_highlighted.png");
        data.add(new ButtonData(UIButton.PREVIOUS_PAGE, gameService::showMainMenu, () -> audioService.playFX(0)));
        data.add(new ButtonData(UIButton.RESET, gameService::newGame, () -> audioService.playFX(0)));
    }

    @Override
    public void setup() {
        float spacing = 1f;
        float buttonX = 50f;
        float buttonY = 50f;

        for(ButtonData data : data) {
            Texture icon = getButton(data, false);
            Texture highlighted = getButton(data, true);

            Button b = new Button(buttonX, buttonY, baseButton.getWidth(),
                baseButton.getHeight(), baseButton, icon, frame,
                highlighted, data.soundPath(), data.action()
            );

            addButton(b);
            buttonX += baseButton.getWidth() + spacing;
        }
    }

    @Override
    public void show() {
        super.show();
        this.shaper = getShaper();
        pieceService.setPieces();

        coordinator = new Coordinator();
        multiplexer = new InputMultiplexer(coordinator, getStage());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);

        drawBoard();
        drawPieces();
        drawTooltip(delta);

        boardInput.update();
        checkInput();
        if(!isCursorActive) {
            globalInput();
        }
    }

    private void drawBoard() {
        shaper.begin(ShapeRenderer.ShapeType.Filled);

        shaper.setColor(Colors.getEdge());
        shaper.rect(
            startX - PADDING,
            startY - PADDING,
            BOARD_SIZE + PADDING * 2,
            BOARD_SIZE + PADDING * 2
        );

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                shaper.setColor((row + col) % 2 == 0 ? Colors.getBackground() : Colors.getForeground());
                shaper.rect(startX + col * TILE_SIZE, startY + row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        if(Coordinator.getLastInput() == LastInput.KEYBOARD &&
            isCursorActive) {
            shaper.setColor(Colors.getHighlight());
            shaper.rect(startX + getMoveX() * TILE_SIZE,
                startY + getMoveY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        shaper.end();
    }

    private void drawPieces() {
        getBatch().begin();
        for(Piece p : pieceService.getPieces()) {
            Texture tex = pieceService.getSprite(p);
            float x, y;

            if(boardInput.getPiece() == p) {
                x = p.getX();
                y = p.getY();
            } else {
                x = startX + p.getCol() * TILE_SIZE;
                y = startY + p.getRow() * TILE_SIZE;
            }

            getBatch().draw(tex, x, y, TILE_SIZE, TILE_SIZE);
        }
        getBatch().end();
    }

    private void drawTooltip(float delta) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        Piece hoveredPiece = null;
        for(Piece p : pieceService.getPieces()) {
            float px = startX + p.getCol() * TILE_SIZE;
            float py = startY + p.getRow() * TILE_SIZE;
            if(mouseX >= px && mouseX <= px + TILE_SIZE && mouseY >= py && mouseY <= py + TILE_SIZE) {
                hoveredPiece = p;
                break;
            }
        }

        if(hoveredPiece != null) {
            tooltip.setText("tooltip." + hoveredPiece.getTypeID().getLabelKey());
        }

        tooltip.update(delta, hoveredPiece != null, mouseX, mouseY);
        tooltip.render(getBatch(), shaper);
    }

    @Override
    public void checkInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.TAB)) { isCursorActive ^= true; }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (isCursorActive) {
                select();
            } else {
                activate();
            }
        }
        if(!isCursorActive) { return; }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) { cursor(Direction.UP, true); }
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) { cursor(Direction.LEFT, true); }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) { cursor(Direction.DOWN, true); }
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) { cursor(Direction.RIGHT, true); }
    }

    @Override
    public void dispose() {
        shaper.dispose();
    }
}
