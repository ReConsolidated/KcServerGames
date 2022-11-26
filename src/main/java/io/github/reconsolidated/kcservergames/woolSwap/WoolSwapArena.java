package io.github.reconsolidated.kcservergames.woolSwap;

import io.github.reconsolidated.kcservergames.regions.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("WoolSwapArena")
@NoArgsConstructor
public class WoolSwapArena implements ConfigurationSerializable {
    // SERIALIZABLE
    private String name;
    @Getter
    private Region playRegion;
    @Getter
    private Region woolRegion;
    @Getter
    private Region entireRegion;
    @Getter
    private int minPlayers;

    // NOT SERIALIZABLE
    @Getter
    @Setter
    private WoolSwapGameState state = WoolSwapGameState.WAITING_FOR_PLAYERS;
    @Getter
    private int level = 1;
    private long countFinishTime = 0;
    private long runFinishTime = 0;
    private long standFinishTime = 0;
    @Getter
    @Setter
    private WoolSwapColor color = WoolSwapColor.BLUE;


    public WoolSwapArena(String name) {
        this.name = name;
    }


    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("playRegion", playRegion);
        result.put("woolRegion", woolRegion);
        result.put("entireRegion", entireRegion);
        result.put("minPlayers", minPlayers);
        return result;
    }

    public static WoolSwapArena deserialize(Map<String, Object> args) {
        WoolSwapArena result = new WoolSwapArena();
        result.name = (String) args.get("name");
        result.playRegion = (Region) args.get("playRegion");
        result.woolRegion = (Region) args.get("woolRegion");
        result.entireRegion = (Region) args.get("entireRegion");
        result.minPlayers = (Integer) args.get("minPlayers");
        return result;
    }

    public void incrementLevel() {
        level++;
    }

    public void resetLevel() {
        level = 1;
    }

    public void setRemainingCountTime(int remainingCountTime) {
        this.countFinishTime = System.currentTimeMillis() + remainingCountTime;
    }

    public void setRemainingStandTime(int remainingStandTime) {
        this.standFinishTime = System.currentTimeMillis() + remainingStandTime;
    }

    public void setRemainingRunTime(int remainingRunTime) {
        this.runFinishTime = System.currentTimeMillis() + remainingRunTime;
    }

    public long getRemainingCountTime() {
        return countFinishTime - System.currentTimeMillis();
    }

    public long getRemainingRunTime() {
        return runFinishTime - System.currentTimeMillis();
    }

    public long getRemainingStandTime() {
        return standFinishTime - System.currentTimeMillis();
    }

}
