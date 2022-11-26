package io.github.reconsolidated.kcservergames.Utils;

import org.bukkit.Location;

public class LocationUtils {
    public static String prettyLocation(Location location) {
        return location.getWorld().getName() + ": " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }
}
