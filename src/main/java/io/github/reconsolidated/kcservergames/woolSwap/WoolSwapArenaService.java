package io.github.reconsolidated.kcservergames.woolSwap;

import io.github.reconsolidated.kcservergames.utils.LocationsPicked;
import io.github.reconsolidated.kcservergames.commandManagement.InformException;
import io.github.reconsolidated.kcservergames.regions.Region;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class WoolSwapArenaService {
    private final WoolSwapArenaRepository woolSwapArenaRepository;
    private final Map<Player, LocationsPicked> playerLocationsPicked = new HashMap<>();

    public WoolSwapArenaService(WoolSwapArenaRepository woolSwapArenaRepository) {
        this.woolSwapArenaRepository = woolSwapArenaRepository;
    }

    public void createArena(String name) {
        woolSwapArenaRepository.save(name, new WoolSwapArena(name));
    }

    public void setLoc(Player player, int locInt) {
        LocationsPicked locationsPicked = playerLocationsPicked.getOrDefault(player, new LocationsPicked());
        if (locInt == 1) {
            locationsPicked.first = player.getLocation();
        } else {
            locationsPicked.second = player.getLocation();
        }
        playerLocationsPicked.put(player, locationsPicked);
    }

    public void setRegion(Player player, String arenaName, String regionType) {
        LocationsPicked locationsPicked = playerLocationsPicked.get(player);
        if (locationsPicked == null || !locationsPicked.areAllPicked()) {
            throw new InformException("You must pick two locations first: /woolswap loc <1 or 2>");
        }
        WoolSwapArena arena = getArena(arenaName);

        if (regionType.equalsIgnoreCase("entire")) {
            arena.setEntireRegion(new Region(locationsPicked.first, locationsPicked.second));
        }
        else if (regionType.equalsIgnoreCase("play")) {
            arena.setPlayRegion(new Region(locationsPicked.first, locationsPicked.second));
        }
        else if (regionType.equalsIgnoreCase("wool")) {
            arena.setWoolRegion(new Region(locationsPicked.first, locationsPicked.second));
        }
        else if (regionType.equalsIgnoreCase("display")) {
            arena.setColorDisplayRegion(new Region(locationsPicked.first, locationsPicked.second));
        } else {
            throw new InformException("Invalid region type. Allowed types: entire, play, wool, display");
        }
        woolSwapArenaRepository.save(arena.getName(), arena);
    }

    public WoolSwapArena getArena(String name) {
        return woolSwapArenaRepository.findById(name).orElseThrow(
                () -> new InformException("Arena %s not found".formatted(name)));
    }
}
