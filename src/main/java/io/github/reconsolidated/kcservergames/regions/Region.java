package io.github.reconsolidated.kcservergames.regions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SerializableAs("Region")
public class Region implements ConfigurationSerializable {
    private final String worldName;
    private final Vector min;
    private final Vector max;

    // NOT SERIALIZED!!! SET FOR OPTIMIZATION
    private List<Block> blocks = null;

    public Region(@NotNull Location loc1,@NotNull Location loc2) {
        this.worldName = loc1.getWorld().getName();
        if (!loc1.getWorld().getUID().equals(loc2.getWorld().getUID())) {
            throw new IllegalArgumentException("Locations must be in the same world");
        }
        this.min = Vector.getMinimum(loc1.toVector(), loc2.toVector());
        this.max = Vector.getMaximum(loc1.toVector(), loc2.toVector());
    }

    private Region(String worldName, Vector loc1, Vector loc2) {
        this.worldName = worldName;
        this.min = Vector.getMinimum(loc1, loc2);
        this.max = Vector.getMaximum(loc1, loc2);
    }

    public boolean isInRegion(Location location) {
        if (!location.getWorld().getName().equals(worldName)) {
            return false;
        }
        Vector pos = location.toVector();
        return pos.isInAABB(min, max);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("worldName", worldName);
        result.put("min", min);
        result.put("max", max);
        return result;
    }

    public static Region deserialize(Map<String, Object> map) {
        return new Region(
                (String) map.get("worldName"),
                (Vector) map.get("min"),
                (Vector) map.get("max")
        );
    }

    public List<Block> getBlocks() {
        if (blocks == null) {
            blocks = new ArrayList<>();
            World world = Bukkit.getWorld(worldName);
            for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                    for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                        Location location = new Location(world, x, y, z);
                        blocks.add(location.getBlock());
                    }
                }
            }
        }
        return blocks;
    }

    @Override
    public String toString() {
        return
                "\n    worldName=" + worldName +
                "\n    min=%d, %d, %d".formatted(min.getBlockX(), min.getBlockY(), min.getBlockZ()) +
                "\n    max=%d, %d, %d".formatted(max.getBlockX(), max.getBlockY(), max.getBlockZ());
    }
}
