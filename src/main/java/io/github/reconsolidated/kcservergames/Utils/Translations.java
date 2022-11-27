package io.github.reconsolidated.kcservergames.Utils;

import io.github.reconsolidated.kcservergames.KcServerGames;
import io.github.reconsolidated.kcservergames.configUtils.CustomConfig;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;

import static io.github.reconsolidated.kcservergames.Utils.Tr.tr;

public class Translations {
    public static String WHITE = "&fWHITE";
    public static String ORANGE = "&6ORANGE";
    public static String MAGENTA = "&dMAGENTA";
    public static String LIGHT_BLUE = "&9LIGHT_BLUE";
    public static String YELLOW = "&eYELLOW";
    public static String LIME = "&aLIME";
    public static String PINK = "&dPINK";
    public static String GRAY = "&8GRAY";
    public static String LIGHT_GRAY = "&7LIGHT_GRAY";
    public static String CYAN = "&3CYAN";
    public static String PURPLE = "&5PURPLE";
    public static String BLUE = "&1BLUE";
    public static String BROWN = "&6BROWN";
    public static String GREEN = "&2GREEN";
    public static String RED = "&cRED";
    public static String BLACK = "&0BLACK";

    public static String STARTS_IN = "Starts in <time>";
    public static String ARENA_STARTED = "STARTED!";

    public static void loadTranslations() {
        String translationsFileName = KcServerGames.getInstance().getConfig().getString("translations_file", "translations.yml");
        CustomConfig translationsConfig = new CustomConfig(translationsFileName);
        FileConfiguration config = translationsConfig.get();
        for (Field field : Translations.class.getDeclaredFields()) {
            if (field.getType() == String.class) {
                try {
                    if (config.contains(field.getName())) {
                        field.set(null, tr(config.getString(field.getName())));
                    } else {
                        field.set(null, tr(field.get(null).toString()));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createDefaultTranslationsFile() {
        CustomConfig config = new CustomConfig("default_messages.yml");
        FileConfiguration configFile = config.get();
        for (Field field : Translations.class.getDeclaredFields()) {
            if (field.getType() == String.class) {
                try {
                    configFile.set(field.getName(), field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        config.save();
    }
}
