package io.github.reconsolidated.kcservergames.regions;

import io.github.reconsolidated.kcservergames.configUtils.SerializableLocation;
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
    private final String name;
    private final UUID worldUID;
    private Vector loc1;
    private Vector loc2;

    // NOT SERIALIZED!!! SET FOR OPTIMIZATION
    private List<Block> blocks = null;

    public Region(String name, Location loc1, Location loc2) {
        this.name = name;
        this.worldUID = loc1.getWorld().getUID();
        if (!loc1.getWorld().getUID().equals(loc2.getWorld().getUID())) {
            throw new IllegalArgumentException("Locations must be in the same world");
        }
        this.loc1 = loc1.toVector();
        this.loc2 = loc2.toVector();
    }

    private Region(String name, UUID worldUID, Vector loc1, Vector loc2) {
        this.name = name;
        this.worldUID = worldUID;
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public boolean isInRegion(Location location) {
        if (location.getWorld().getUID() != worldUID) {
            return false;
        }
        Vector pos = location.toVector();
        return pos.isInAABB(loc1, loc2);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("worldUID", worldUID.toString());
        result.put("loc1", loc1);
        result.put("loc2", loc2);
        return result;
    }

    public static Region deserialize(Map<String, Object> map) {
        return new Region(
                (String) map.get("name"),
                UUID.fromString((String) map.get("worldUID")),
                (Vector) map.get("loc1"),
                (Vector) map.get("loc2")
        );
    }

    public List<Block> getBlocks() {
        if (blocks == null) {
            World world = Bukkit.getWorld(worldUID);
            for (int x = loc1.getBlockX(); x <= loc2.getBlockX(); x++) {
                for (int y = loc1.getBlockY(); y <= loc2.getBlockY(); y++) {
                    for (int z = loc1.getBlockZ(); z <= loc2.getBlockZ(); z++) {
                        Location location = new Location(world, x, y, z);
                        blocks.add(location.getBlock());
                    }
                }
            }
        }
        return blocks;
    }
}
