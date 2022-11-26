package io.github.reconsolidated.kcservergames.woolSwap;

import org.bukkit.Material;

public enum WoolSwapColor {

    WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK;

    public Material getMaterial() {
        final String BLOCK_USED = "color_CONCRETE";
        return Material.valueOf(BLOCK_USED.replace("color", this.name()));
    }
}
