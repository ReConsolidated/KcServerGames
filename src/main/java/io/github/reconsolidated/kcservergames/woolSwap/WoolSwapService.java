package io.github.reconsolidated.kcservergames.woolSwap;

import io.github.reconsolidated.kcservergames.KcServerGames;
import io.github.reconsolidated.kcservergames.music.MusicService;
import io.github.reconsolidated.kcservergames.regions.Region;
import io.github.reconsolidated.kcservergames.utils.Translations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class WoolSwapService {
    private final Logger log = Bukkit.getLogger();
    private final WoolSwapArenaRepository repository;
    private final MusicService musicService;

    public WoolSwapService(WoolSwapArenaRepository woolSwapArenaRepository, MusicService musicService) {
        this.repository = woolSwapArenaRepository;
        this.musicService = musicService;

        for (WoolSwapArena arena : repository.getAll()) {
            if (arena.isReady()) {
                setAllColors(arena);
                openEntrance(arena);
                Location musicLocation = arena.getWoolRegion().getCenterLocation();
                arena.setMusicPlayer(musicService.createPlayer(musicLocation, 20));
            }
        }

        Bukkit.getScheduler().runTaskTimer(KcServerGames.getInstance(), () -> {
            for (WoolSwapArena arena : repository.getAll()) {
                if (arena.isReady()) {
                    tick(arena);
                }
            }
        }, 0L, 10L);
    }




    private void tick(WoolSwapArena arena) {
        if (arena.getState() == WoolSwapGameState.WAITING_FOR_PLAYERS) {
            playMusic(arena);
            int players = countPlayers(arena);
            if (players >= arena.getMinPlayers()) {
                setRemainingCountTime(arena);
                arena.setState(WoolSwapGameState.COUNTING);
            }
        }
        else if (arena.getState() == WoolSwapGameState.COUNTING) {
            if (arena.getRemainingCountTime() < 0) {
                log.info("Remaining count time is less than 0, starting the arena.");
                startArena(arena);
                setRemainingRunTime(arena);
                displayColor(arena);
                closeEntrance(arena);
                arena.setState(WoolSwapGameState.IN_PROGRESS_RUN);
            } else {
                log.info("Remaining count time is %d, doing the counting.".formatted(arena.getRemainingCountTime()));
                doCounting(arena);
            }
        }
        else {
            if (countPlayers(arena) == 0) {
                arena.setState(WoolSwapGameState.WAITING_FOR_PLAYERS);
                setAllColors(arena);
                openEntrance(arena);
                return;
            }
            if (arena.getState() == WoolSwapGameState.IN_PROGRESS_RUN) {
                playMusic(arena);
                if (arena.getRemainingRunTime() < 0) {
                    clearInfo(arena);
                    setOnlyOneColor(arena);
                    setRemainingStandTime(arena);
                    arena.setState(WoolSwapGameState.IN_PROGRESS_FALL);
                } else {
                    sendColorInfo(arena);
                }
            } else {
                stopMusic(arena);
                if (arena.getRemainingStandTime() < 0) {
                    setAllColors(arena);
                    incrementLevel(arena);
                    setRemainingRunTime(arena);
                    displayColor(arena);
                    arena.setState(WoolSwapGameState.IN_PROGRESS_RUN);
                }
            }
        }
    }

    private void openEntrance(WoolSwapArena arena) {
        arena.getEntranceRegion().getBlocks().forEach(block -> block.setType(Material.AIR));
    }

    private void closeEntrance(WoolSwapArena arena) {
        arena.getEntranceRegion().getBlocks().forEach(block -> block.setType(Material.RED_STAINED_GLASS));
    }

    private void displayColor(WoolSwapArena arena) {
        arena.getColorDisplayRegion().getBlocks().forEach(block -> {
            block.setType(arena.getColor().getMaterial());
        });
    }

    private void stopMusic(WoolSwapArena arena) {
        if (arena.getSongPlayer() == null) return;
        if (arena.getSongPlayer().isPlaying()) {
            arena.getSongPlayer().setPlaying(false);
        }
    }

    private void playMusic(WoolSwapArena arena) {
        if (arena.getSongPlayer() == null) return;
        if (!arena.getSongPlayer().isPlaying()) {
            arena.getSongPlayer().setPlaying(true);
        }
    }

    private void clearInfo(WoolSwapArena arena) {
        for (Player player : getPlayersInArena(arena)) {
            player.showTitle(Title.title(Component.text(" "), Component.empty()));
        }
    }

    private void sendColorInfo(WoolSwapArena arena) {
        for (Player player : getPlayersInArena(arena)) {
            switch (arena.getColor()) {
                case WHITE -> player.showTitle(Title.title(Component.text(Translations.WHITE), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case ORANGE -> player.showTitle(Title.title(Component.text(Translations.ORANGE), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case MAGENTA -> player.showTitle(Title.title(Component.text(Translations.MAGENTA), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case LIGHT_BLUE -> player.showTitle(Title.title(Component.text(Translations.LIGHT_BLUE), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case YELLOW -> player.showTitle(Title.title(Component.text(Translations.YELLOW), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case LIME -> player.showTitle(Title.title(Component.text(Translations.LIME), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case PINK -> player.showTitle(Title.title(Component.text(Translations.PINK), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case GRAY -> player.showTitle(Title.title(Component.text(Translations.GRAY), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case LIGHT_GRAY -> player.showTitle(Title.title(Component.text(Translations.LIGHT_GRAY), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case CYAN -> player.showTitle(Title.title(Component.text(Translations.CYAN), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case PURPLE -> player.showTitle(Title.title(Component.text(Translations.PURPLE), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case BLUE -> player.showTitle(Title.title(Component.text(Translations.BLUE), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case BROWN -> player.showTitle(Title.title(Component.text(Translations.BROWN), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case GREEN -> player.showTitle(Title.title(Component.text(Translations.GREEN), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case RED -> player.showTitle(Title.title(Component.text(Translations.RED), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
                case BLACK -> player.showTitle(Title.title(Component.text(Translations.BLACK), Component.empty(), Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0))));
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

        int currentTime = minimalTime + startingTime * (Math.max(1, 10-arena.getLevel()))/10;
        arena.setRemainingRunTime(currentTime);
    }

    private void setRemainingStandTime(WoolSwapArena arena) {
        arena.setRemainingStandTime(2000);
    }

    private void startArena(WoolSwapArena arena) {
        log.info("Starting arena " + arena.getName());
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
