package org.lud.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.lud.engine.core.AudioService;
import org.lud.engine.gui.Button;
import org.lud.engine.gui.Colors;
import org.lud.engine.gui.Menu;
import org.lud.game.data.ButtonData;
import org.lud.game.data.Piece;
import org.lud.game.data.Tooltip;
import org.lud.game.enums.UIButton;
import org.lud.game.service.GameService;
import org.lud.game.service.PieceService;

import java.util.ArrayList;
import java.util.List;

public class BoardScreen extends Menu {
    private static final int TILE_SIZE = 64;
    private static final int BOARD_SIZE = TILE_SIZE * 8;
    private static final int PADDING = 32;

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

    public BoardScreen(GameService gameService, PieceService pieceService,
                       AudioService audioService) {
        super(gameService, audioService);
        this.gameService = gameService;
        this.pieceService = pieceService;
        this.audioService = audioService;
        this.data = new ArrayList<>();
        this.tooltip = new Tooltip("", getFont());

        this.startX = (Gdx.graphics.getWidth() - BOARD_SIZE)/2f;
        this.startY = (Gdx.graphics.getHeight() - BOARD_SIZE)/2f;

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

            Button b = new Button(
                buttonX,
                buttonY,
                baseButton.getWidth(),
                baseButton.getHeight(),
                baseButton,
                icon,
                frame,
                highlighted,
                data.soundPath(),
                data.action()
            );

            addButton(b);
            buttonX += baseButton.getWidth() + spacing;
        }
    }

    @Override
    public void show() {
        this.shaper = getShaper();
        pieceService.setPieces();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);

        drawBoard();
        drawPieces();
        drawTooltip(delta);

        globalInput();
        checkInput();
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
                if((row + col) % 2 == 0) {
                    shaper.setColor(Colors.getBackground());
                } else {
                    shaper.setColor(Colors.getForeground());
                }

                shaper.rect( startX + col * TILE_SIZE,
                    startY + row * TILE_SIZE, TILE_SIZE, TILE_SIZE
                );
            }
        }

        shaper.end();
    }

    private void drawPieces() {
        getBatch().begin();
        for(Piece p : pieceService.getPieces()) {
            Texture tex = pieceService.getSprite(p);
            float x = startX + p.col() * TILE_SIZE;
            float y = startY + p.row() * TILE_SIZE;
            getBatch().draw(tex, x, y, TILE_SIZE, TILE_SIZE);
        }
        getBatch().end();
    }


    private void drawTooltip(float delta) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        Piece hoveredPiece = null;
        for(Piece p : pieceService.getPieces()) {
            float px = startX + p.col() * TILE_SIZE;
            float py = startY + p.row() * TILE_SIZE;
            boolean inside =
                mouseX >= px &&
                    mouseX <= px + TILE_SIZE &&
                    mouseY >= py &&
                    mouseY <= py + TILE_SIZE;

            if(inside) {
                hoveredPiece = p;
                break;
            }
        }

        boolean isHovered = hoveredPiece != null;
        if(isHovered) {
            tooltip.setText("tooltip." + hoveredPiece.type().getLabelKey());
        }

        tooltip.update(delta, isHovered, mouseX, mouseY);
        tooltip.render(getBatch(), shaper);
    }

    @Override
    public void checkInput() {

    }

    @Override
    public void dispose() {
        shaper.dispose();
    }

}
