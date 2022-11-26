package io.github.reconsolidated.kcservergames.configUtils;

import io.github.reconsolidated.kcservergames.KcServerGames;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomConfig {

    private final File file;
    private FileConfiguration customFile;

    //Finds or generates the custom config file
    public CustomConfig(String configName){
        if (!configName.endsWith(".yml")) {
            configName += ".yml";
        }
        file = new File(KcServerGames.getInstance().getDataFolder(), configName);

        if (!file.exists()){
            try{
                file.createNewFile();
            } catch (IOException e){
                Bukkit.getLogger().warning("Could not create custom config file: %s".formatted(configName));
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get(){
        return customFile;
    }

    public void save(){
        try{
            customFile.save(file);
        }catch (IOException e){
            Bukkit.getLogger().warning("Couldn't save file: %s".formatted(file.getName()));
        }
    }

    public void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }

}
