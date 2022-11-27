package io.github.reconsolidated.kcservergames.ymlRepository;

import io.github.reconsolidated.kcservergames.KcServerGames;
import io.github.reconsolidated.kcservergames.configUtils.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class YmlRepository<T extends ConfigurationSerializable> {
    private final Map<String, T> storage = new HashMap<>();
    private final CustomConfig config;

    public YmlRepository(String configName) {
        this.config = new CustomConfig(configName);
        Set<String> keys = config.get().getKeys(false);
        for (String key : keys) {
            storage.put(key, (T) config.get().get(key));
        }
    }

    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<T> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    public void save(String id, T item) {
        storage.put(id, item);

        this.config.get().set(id, storage.get(id));
        Bukkit.getScheduler().runTaskAsynchronously(KcServerGames.getInstance(), this.config::save);
    }

}
