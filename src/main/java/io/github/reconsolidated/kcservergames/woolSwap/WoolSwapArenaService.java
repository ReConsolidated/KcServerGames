package io.github.reconsolidated.kcservergames.woolSwap;

import io.github.reconsolidated.kcservergames.Utils.LocationsPicked;
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

    public void setLoc(Player sender, int locInt) {
        LocationsPicked locationsPicked = playerLocationsPicked.getOrDefault(sender, new LocationsPicked());
        if (locInt == 1) {
            locationsPicked.first = sender.getLocation();
        } else {
            locationsPicked.second = sender.getLocation();
        }
    }
}
