package org.lud.engine.enums;

import com.badlogic.gdx.graphics.Color;

public enum Theme {
    DEFAULT(
        "white", "brown",
        new Color(1f, 1f, 1f, 1f),
        new Color(0.078f, 0.078f, 0.157f, 1f),
        new Color(0.078f, 0.078f, 0.157f, 1f),
        new Color(1f, 0.784f, 0f, 1f)
    ),
    BLACK(
        "creme", "brown",
        new Color(0f, 0f, 0f, 1f),
        new Color(1f, 1f, 1f, 1f),
        new Color(0.078f, 0.078f, 0.157f, 1f),
        new Color(1f, 1f, 0.392f, 1f)
    ),
    LEGACY(
        "creme", "brown",
        new Color(0.823f, 0.647f, 0.490f, 1f),
        new Color(0.686f, 0.451f, 0.275f, 1f),
        new Color(0.471f, 0.275f, 0.157f, 1f),
        new Color(1f, 0.882f, 0f, 1f)
    ),
    OCEAN(
        "ocean", "pacific",
        new Color(0.392f, 0.549f, 0.784f, 1f),
        new Color(0.392f, 0.353f, 0.784f, 1f),
        new Color(0.275f, 0.235f, 0.667f, 1f),
        new Color(0.706f, 0.667f, 1f, 1f)
    ),
    FOREST(
        "forest", "rainforest",
        new Color(0.392f, 0.706f, 0.392f, 1f),
        new Color(0.118f, 0.471f, 0.235f, 1f),
        new Color(0.039f, 0.392f, 0.157f, 1f),
        new Color(0.588f, 0.941f, 0.706f, 1f)
    ),
    LOGO(
        "logo", "brown",
        new Color(0.435f, 0.686f, 0.302f, 1f),
        new Color(0.278f, 0.529f, 0.145f, 1f),
        new Color(0.200f, 0.451f, 0.067f, 1f),
        new Color(0.749f, 1f, 0.616f, 1f)
    ),
    FAIRY(
        "fairy", "purple",
        new Color(0.706f, 0.549f, 0.784f, 1f),
        new Color(0.706f, 0.275f, 0.784f, 1f),
        new Color(0.549f, 0.157f, 0.667f, 1f),
        new Color(0.980f, 0.549f, 1f, 1f)
    );

    private final String lightName;
    private final String darkName;
    private final Color background;
    private final Color foreground;
    private final Color edge;
    private final Color highlight;

    Theme(String lightName,
          String darkName,
          Color background,
          Color foreground,
          Color edge,
          Color highlight) {

        this.lightName = lightName;
        this.darkName = darkName;
        this.background = background;
        this.foreground = foreground;
        this.edge = edge;
        this.highlight = highlight;
    }

    public String getColor(Turn turn) {
        return turn == Turn.LIGHT ? lightName : darkName;
    }

    public Color getBackground() { return background; }
    public Color getForeground() { return foreground; }
    public Color getEdge() { return edge; }
    public Color getHighlight() { return highlight; }
}
