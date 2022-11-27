package io.github.reconsolidated.kcservergames;

import io.github.reconsolidated.kcservergames.music.MusicService;
import io.github.reconsolidated.kcservergames.utils.Translations;
import io.github.reconsolidated.kcservergames.regions.Region;
import io.github.reconsolidated.kcservergames.woolSwap.WoolSwapArena;
import io.github.reconsolidated.kcservergames.woolSwap.WoolSwapArenaRepository;
import io.github.reconsolidated.kcservergames.woolSwap.WoolSwapArenaService;
import io.github.reconsolidated.kcservergames.woolSwap.WoolSwapService;
import io.github.reconsolidated.kcservergames.woolSwap.setup.WoolSwapCommand;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class KcServerGames extends JavaPlugin {
    @Getter
    private static KcServerGames instance;

    private MusicService musicService;
    private WoolSwapArenaRepository woolSwapArenaRepository;
    private WoolSwapService woolSwapService;
    private WoolSwapArenaService woolSwapArenaService;

    @Override
    public void onEnable() {
        instance = this;
        ConfigurationSerialization.registerClass(Region.class);
        ConfigurationSerialization.registerClass(WoolSwapArena.class);

        getConfig().set("version", "1.0.0");
        saveConfig();

        Translations.createDefaultTranslationsFile();
        Translations.loadTranslations();

        musicService = new MusicService();
        woolSwapArenaRepository = new WoolSwapArenaRepository();
        woolSwapService = new WoolSwapService(woolSwapArenaRepository, musicService);
        woolSwapArenaService = new WoolSwapArenaService(woolSwapArenaRepository);
        WoolSwapCommand woolSwapCommand = new WoolSwapCommand(woolSwapArenaService);
        getCommand("woolswap").setExecutor(woolSwapCommand);
        getCommand("woolswap").setTabCompleter(woolSwapCommand);
    }

    @Override
    public void onDisable() {

    }
}
