package io.github.reconsolidated.kcservergames.configUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("Loc")
public class SerializableLocation implements ConfigurationSerializable {
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    @Nullable
    private final String worldName;

    public SerializableLocation(double x, double y, double z, float yaw, float pitch, @Nullable String worldName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.worldName = worldName;
    }

    public SerializableLocation(Location location) {
        this(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), location.getWorld().getName());
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
        map.put("yaw", yaw);
        map.put("pitch", pitch);
        map.put("world", worldName);
        return map;
    }

    public static SerializableLocation deserialize(Map<String, Object> args) {
        return new SerializableLocation(
                (double) args.get("x"),
                (double) args.get("y"),
                (double) args.get("z"),
                (float) (double) args.get("yaw"),
                (float) (double) args.get("pitch"),
                (String) args.get("world")
                );
    }

    public Location getLocation() {
        return new Location(createWorld(worldName), x, y, z, yaw, pitch);
    }

    public static World createWorld(String worldName) {
        WorldCreator creator = new WorldCreator(worldName);
        return Bukkit.createWorld(creator);
    }

}
