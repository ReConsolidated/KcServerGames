package io.github.reconsolidated.kcservergames.utils;

import org.bukkit.ChatColor;

public class Tr {
    public static String tr(String phrase) {
        return ChatColor.translateAlternateColorCodes('&', phrase);
    }
}