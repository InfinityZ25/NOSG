package me.lofro.core.paper.utils.persistentDataContainers;

import me.lofro.core.paper.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;

public class Data {
    public static NamespacedKey key(String key) {
        return new NamespacedKey(Main.getPlugin(Main.class), key);
    }

    public static PersistentDataContainer getData(@Nonnull PersistentDataHolder dataHolder) throws PlayerIsNotOnlineException {
        if (dataHolder instanceof Player && !((Player) dataHolder).isOnline()) throw new PlayerIsNotOnlineException((Player) dataHolder);
        return dataHolder.getPersistentDataContainer();
    }


    public static <T> void set(@Nonnull PersistentDataContainer data, String key, PersistentDataType<T, T> type, T value) {
        data.set(key(key), type, value);
    }

    public static <T> T get(@Nonnull PersistentDataContainer data, String key, PersistentDataType<T, T> type) {
        return data.get(key(key), type);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static <T> boolean has(@Nonnull PersistentDataContainer data, String key, PersistentDataType<T, T> type) {
        return data.has(key(key), type);
    }
}
