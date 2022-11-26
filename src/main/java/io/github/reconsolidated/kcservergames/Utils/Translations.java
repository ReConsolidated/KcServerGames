package io.github.reconsolidated.kcservergames.Utils;

import io.github.reconsolidated.kcservergames.KcServerGames;
import io.github.reconsolidated.kcservergames.configUtils.CustomConfig;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;

import static io.github.reconsolidated.kcservergames.Utils.Tr.tr;

public class Translations {


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
