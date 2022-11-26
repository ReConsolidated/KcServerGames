package io.github.reconsolidated.kcservergames.Utils;

import org.bukkit.ChatColor;

public class Tr {
    public static String tr(String phrase) {
        return ChatColor.translateAlternateColorCodes('&', phrase);
    }
}