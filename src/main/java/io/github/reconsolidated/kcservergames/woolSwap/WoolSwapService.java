package io.github.reconsolidated.kcservergames.woolSwap;

import io.github.reconsolidated.kcservergames.KcServerGames;
import io.github.reconsolidated.kcservergames.Utils.Translations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WoolSwapService {
    private final WoolSwapArenaRepository repository;

    public WoolSwapService(WoolSwapArenaRepository woolSwapArenaRepository) {
        this.repository = woolSwapArenaRepository;

        Bukkit.getScheduler().runTaskTimer(KcServerGames.getInstance(), () -> {
            for (WoolSwapArena arena : repository.getAll()) {
                tick(arena);
            }
        }, 0L, 10L);
    }

    private void tick(WoolSwapArena arena) {
        if (arena.getState() == WoolSwapGameState.WAITING_FOR_PLAYERS) {
            int players = countPlayers(arena);
            if (players >= arena.getMinPlayers()) {
                setRemainingCountTime(arena);
                arena.setState(WoolSwapGameState.COUNTING);
            }
        }
        else if (arena.getState() == WoolSwapGameState.COUNTING) {
            if (arena.getRemainingCountTime() < 0) {
                startArena(arena);
                arena.setState(WoolSwapGameState.IN_PROGRESS_RUN);
            } else {
                doCounting(arena);
            }
        }
        else {
            int players = countPlayers(arena);
            if (players == 0) {
                resetArena(arena);
                arena.setState(WoolSwapGameState.WAITING_FOR_PLAYERS);
                return;
            }
            if (arena.getState() == WoolSwapGameState.IN_PROGRESS_RUN) {
                if (arena.getRemainingRunTime() < 0) {
                    setOnlyOneColor(arena);
                    setRemainingStandTime(arena);
                    arena.setState(WoolSwapGameState.IN_PROGRESS_FALL);
                }
            } else {
                if (arena.getRemainingStandTime() < 0) {
                    setAllColors(arena);
                    incrementLevel(arena);
                    setRemainingRunTime(arena);
                    arena.setState(WoolSwapGameState.IN_PROGRESS_RUN);
                }
            }
        }
    }

    private void setAllColors(WoolSwapArena arena) {
        for (Block block : arena.getWoolRegion().getBlocks()) {
            block.setType(getRandomColorMaterial());
        }
    }

    private Material getRandomColorMaterial() {
        return getRandomColor().getMaterial();
    }

    private void setOnlyOneColor(WoolSwapArena arena) {
        WoolSwapColor color = arena.getColor();
        for (Block block : arena.getWoolRegion().getBlocks()) {
            if (!block.getType().equals(color.getMaterial())) {
                block.setType(Material.AIR);
            }
        }
    }

    private void setRemainingRunTime(WoolSwapArena arena) {
        int startingTime = 10000;
        int minimalTime = 700;

        int currentTime = minimalTime + (int) (1-Math.tanh(arena.getLevel()-1)) * (startingTime-minimalTime);
        arena.setRemainingRunTime(currentTime);
    }

    private void setRemainingStandTime(WoolSwapArena arena) {
        arena.setRemainingStandTime(2000);
    }

    private void startArena(WoolSwapArena arena) {
        pickNextColor(arena);
        getPlayersInArena(arena).forEach((player) -> {
            player.showTitle(Title.title(
                    Component.text(Translations.ARENA_STARTED),
                    Component.text(""),
                    Title.Times.times(Duration.ZERO, Duration.ofMillis(900), Duration.ofMillis(100))
            ));
        });
    }

    private void pickNextColor(WoolSwapArena arena) {
        WoolSwapColor color = getRandomColor();
        arena.setColor(color);
    }

    private WoolSwapColor getRandomColor() {
        Random random = new Random();
        return WoolSwapColor.values()[random.nextInt(WoolSwapColor.values().length)];
    }

    private void doCounting(WoolSwapArena arena) {
        int timeLeft = 1 + (int) ((arena.getRemainingCountTime()-1) / 1000);
        for (Player player : getPlayersInArena(arena)) {
            player.showTitle(Title.title(
                    Component.text(Translations.STARTS_IN.replace("<time>", timeLeft + "")),
                    Component.text(""),
                    Title.Times.times(Duration.ZERO, Duration.ofMillis(900), Duration.ofMillis(100))
            ));
        }
    }

    private List<Player> getPlayersInArena(WoolSwapArena arena) {
        List<Player> list = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (arena.getEntireRegion().isInRegion(player.getLocation())) {
                list.add(player);
            }
        }
        return list;
    }

    private void resetArena(WoolSwapArena arena) {
        arena.resetLevel();
    }

    private void incrementLevel(WoolSwapArena arena) {
        arena.incrementLevel();
        pickNextColor(arena);
    }

    private void setRemainingCountTime(WoolSwapArena arena) {
        arena.setRemainingCountTime(5000);
    }

    private int countPlayers(WoolSwapArena arena) {
        int count = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (arena.getPlayRegion().isInRegion(player.getLocation())) {
                count++;
            }
        }
        return count;
    }
}
