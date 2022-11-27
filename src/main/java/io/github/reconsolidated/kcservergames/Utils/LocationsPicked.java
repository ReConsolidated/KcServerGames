package io.github.reconsolidated.kcservergames.Utils;


import org.bukkit.Location;

public class LocationsPicked {
    public Location first;
    public Location second;

    public boolean areAllPicked() {
        return first != null && second != null;
    }
}
