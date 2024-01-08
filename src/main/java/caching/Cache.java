package caching;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class Cache<K, V> {
    private final HashMap<K, V> cache = new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public V cache(K key, Supplier<V> valueSupplier, int clearTime) {
        if (key == null || valueSupplier == null) {
            throw new IllegalArgumentException("Key and valueSupplier cannot be null");
        }

        V cachedValue = cache.get(key);

        if (cachedValue == null) {
            cachedValue = valueSupplier.get();
            cache.put(key, cachedValue);

            if (clearTime > 0) {
                scheduler.schedule(() -> {
                    clearCache(key);
                }, clearTime, TimeUnit.MILLISECONDS);
            }
        }

        System.out.println(cache.get(key));

        return cachedValue;
    }

    public void clearCache(K key) {
        cache.remove(key);
    }

    public V getCache(K key) {
        return cache.get(key);
    }

    public void shutdownScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
