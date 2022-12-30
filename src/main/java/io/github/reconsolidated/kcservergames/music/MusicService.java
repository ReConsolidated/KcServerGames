package io.github.reconsolidated.kcservergames.music;

import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import io.github.reconsolidated.kcservergames.KcServerGames;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicService implements Listener {

    private final List<Song> songs = new ArrayList<>();
    private Playlist playlist;
    private final List<SongPlayer> players = new ArrayList<>();

    public MusicService() {
        reload();
        KcServerGames.getInstance().getServer().getPluginManager().registerEvents(this, KcServerGames.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (SongPlayer songPlayer : players) {
            songPlayer.addPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        for (SongPlayer songPlayer : players) {
            songPlayer.removePlayer(event.getPlayer());
        }
    }

    public SongPlayer createPlayer(Location location, int distance) {
        PositionSongPlayer positionSongPlayer = new PositionSongPlayer(playlist);
        positionSongPlayer.setCategory(SoundCategory.RECORDS);
        positionSongPlayer.setTargetLocation(location);
        positionSongPlayer.setDistance(distance);
        positionSongPlayer.setPlaying(true);
        positionSongPlayer.setRepeatMode(RepeatMode.ALL);
        for (Player player : Bukkit.getOnlinePlayers()) {
            positionSongPlayer.addPlayer(player);
        }
        players.add(positionSongPlayer);
        return positionSongPlayer;
    }

    public void reload() {
        loadSongs();
        if (songs.size() > 0) {
            playlist = new Playlist(songs.toArray(new Song[0]));
        }
    }

    private void loadSongs() {
        songs.clear();
        File musicFolder = new File(KcServerGames.getInstance().getDataFolder().getPath() + "/songs");
        if (!musicFolder.exists()) {
            musicFolder.mkdirs();
        }

        for (File file : musicFolder.listFiles()) {
            if (file.getName().endsWith(".nbs")) {
                Bukkit.getLogger().info("Found song: " + file.getName());
                songs.add(NBSDecoder.parse(file));
            }
        }
    }
}
