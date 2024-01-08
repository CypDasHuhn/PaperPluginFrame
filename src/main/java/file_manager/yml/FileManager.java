package file_manager.yml;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class FileManager {
    public static Object get(FileConfiguration fileConfiguration, String dir) {
        return fileConfiguration.get(dir);
    }
    public static Object get(FileConfiguration fileConfiguration, String dir, Predicate<Object> condition, BiFunction<FileConfiguration, String, Object> alternative) {
        Object value = fileConfiguration.get(dir);
        if (condition.test(value)) value = alternative.apply(fileConfiguration, dir);
        return value;
    }

    public static <T> List<T> getList(FileConfiguration fileConfiguration, String dir) {
        return (List<T>) fileConfiguration.getList(dir);
    }
    public static <T> List<T> getList(FileConfiguration fileConfiguration, String dir, BiPredicate<List<T>, Object> condition, TriFunction<List<T>, Object, Integer, Object> alternative) {
        List<T> list = getList(fileConfiguration, dir);
        for (int i = 0; i < list.size(); i++) {
            T value = list.get(i);
            if (condition.test(list, value)) {
                Object alternativeResult = alternative.apply(list, value, i);
                list.set(i, (T) alternativeResult);
            }
        }
        return list;
    }

    public static void set(FileConfiguration fileConfiguration, String dir, Object value) {
        fileConfiguration.set(dir, value);
    }
    public static void set(FileConfiguration fileConfiguration, String dir, Object value, Predicate<Object> condition, TriConsumer<FileConfiguration, String, Object> alternative) {
        if (condition.test(value)) {
            alternative.accept(fileConfiguration, dir, value);
        } else {
            fileConfiguration.set(dir, value);
        }
    }

    public static void setList(FileConfiguration fileConfiguration, String dir, Object value) {
        set(fileConfiguration, dir, value);
    }
    public static void setList(FileConfiguration fileConfiguration, String dir, List<?> list, BiPredicate<List<?>, Object> condition, TriFunction<List<?>, Object, Integer, List<?>> alternative) {
        for (int i = 0; i < list.size(); i++) {
            Object value = list.get(i);
            if (condition.test(list, value)) {
                list = alternative.apply(list, value, i);
            }
        }
        setList(fileConfiguration, dir, list);
    }

    public static void add(FileConfiguration fileConfiguration, String dir, Object value) {
        List<Object> list = getList(fileConfiguration, dir);
        if (list != null) {
            list.add(value);
            set(fileConfiguration, dir, list);
        }
    }
    public static void add(FileConfiguration fileConfiguration, String dir, Object value, BiPredicate<Object, List<?>> condition, BiFunction<Object, Object, Object> alternative) {
        List<Object> list = getList(fileConfiguration, dir);
        if (list != null) {
            if (condition.test(value, list)) {
                alternative.apply(value, list);
            } else {
                list.add(value);
                set(fileConfiguration, dir, list);
            }
        }
    }

    public static void remove(FileConfiguration fileConfiguration, String dir, Object value) {
        List<Object> list = getList(fileConfiguration, dir);
        if (list != null) {
            list.remove(value);
            set(fileConfiguration, dir, list);
        }
    }
    public static void remove(FileConfiguration fileConfiguration, String dir, Object value, BiPredicate<Object, Object> condition, BiFunction<Object, Object, Object> alternative) {
        List<Object> list = getList(fileConfiguration, dir);
        if (list != null) {
            if (condition.test(value, list)) {
                alternative.apply(value, list);
            } else {
                list.remove(value);
                set(fileConfiguration, dir, list);
            }
        }
    }

    @FunctionalInterface
    public interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }
    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
}
