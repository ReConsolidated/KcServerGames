package io.github.reconsolidated.kcservergames.woolSwap;

import org.bukkit.Color;
import org.bukkit.Material;

public enum WoolSwapColor {

    WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW,
    LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE,
    BLUE, BROWN, GREEN, RED, BLACK;

    public Material getMaterial() {
        final String BLOCK_USED = "<color>_CONCRETE";
        return Material.valueOf(BLOCK_USED.replace("<color>", this.name()));
    }

    public Color getRGBColor() {
        return switch (this) {
            case WHITE -> Color.WHITE;
            case ORANGE -> Color.ORANGE;
            case MAGENTA -> Color.FUCHSIA;
            case LIGHT_BLUE -> Color.AQUA;
            case YELLOW -> Color.YELLOW;
            case LIME -> Color.LIME;
            case PINK -> Color.fromRGB(255, 192, 203);
            case GRAY -> Color.GRAY;
            case LIGHT_GRAY -> Color.SILVER;
            case CYAN -> Color.TEAL;
            case PURPLE -> Color.PURPLE;
            case BLUE -> Color.BLUE;
            case BROWN -> Color.MAROON;
            case GREEN -> Color.GREEN;
            case RED -> Color.RED;
            case BLACK -> Color.BLACK;
        };
    }
}
