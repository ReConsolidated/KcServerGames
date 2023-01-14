package io.github.reconsolidated.kcservergames.utils;

import io.github.reconsolidated.kcservergames.KcServerGames;
import io.github.reconsolidated.kcservergames.configUtils.CustomConfig;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;

import static io.github.reconsolidated.kcservergames.utils.Tr.tr;

public class Translations {
    public static String WHITE = "WHITE";
    public static String ORANGE = "ORANGE";
    public static String MAGENTA = "MAGENTA";
    public static String LIGHT_BLUE = "LIGHT_BLUE";
    public static String YELLOW = "YELLOW";
    public static String LIME = "LIME";
    public static String PINK = "PINK";
    public static String GRAY = "GRAY";
    public static String LIGHT_GRAY = "LIGHT_GRAY";
    public static String CYAN = "CYAN";
    public static String PURPLE = "PURPLE";
    public static String BLUE = "BLUE";
    public static String BROWN = "BROWN";
    public static String GREEN = "GREEN";
    public static String RED = "RED";
    public static String BLACK = "BLACK";

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
