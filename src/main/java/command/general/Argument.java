package command.general;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;

public class Argument {
    PentaPredicate<CommandSender, String[], String, Integer, HashMap<String, Object>> isArgument;
    PentaPredicate<CommandSender, String[], String, Integer, HashMap<String, Object>> isValid;
    PentaFunction<CommandSender, String[], String, Integer, HashMap<String, Object>, List<String>> tabCompletions;
    PentaConsumer<CommandSender, String[], String, Integer, HashMap<String, Object>> errorInvalid;
    PentaConsumer<CommandSender, String[], String, Integer, HashMap<String, Object>> errorMissing;
    PentaFunction<CommandSender, String[], String, Integer, HashMap<String, Object>, Object> argumentHandler;
    TriConsumer<CommandSender, String[], HashMap<String, Object>> invoke;
    boolean modifier;
    List<Argument> followingArguments;
    String key;

    public Argument(PentaPredicate<CommandSender, String[], String, Integer, HashMap<String, Object>> isArgument,
                    PentaPredicate<CommandSender, String[], String, Integer, HashMap<String, Object>> isValid,
                    PentaFunction<CommandSender, String[], String, Integer, HashMap<String, Object>, List<String>> tabCompletions,
                    PentaConsumer<CommandSender, String[], String, Integer, HashMap<String, Object>> errorInvalid,
                    PentaConsumer<CommandSender, String[], String, Integer, HashMap<String, Object>> errorMissing,
                    PentaFunction<CommandSender, String[], String, Integer, HashMap<String, Object>, Object> argumentHandler,
                    TriConsumer<CommandSender, String[], HashMap<String, Object>> invoke,
                    List<Argument> followingArguments) {
        this.isArgument = isArgument;
        this.isValid = isValid;
        this.tabCompletions = tabCompletions;
        this.errorInvalid = errorInvalid;
        this.errorMissing = errorMissing;
        this.argumentHandler = argumentHandler;
        this.invoke = invoke;
        this.followingArguments = followingArguments;

        this.modifier = false;
    }

    public Argument(PentaPredicate<CommandSender, String[], String, Integer, HashMap<String, Object>> isArgument,
                    PentaFunction<CommandSender, String[], String, Integer, HashMap<String, Object>, List<String>> tabCompletions,
                    PentaFunction<CommandSender, String[], String, Integer, HashMap<String, Object>, Object> argumentHandler) {
        this.isArgument = isArgument;
        this.tabCompletions = tabCompletions;
        this.argumentHandler = argumentHandler;

        this.modifier = true;
    }

    @FunctionalInterface
    public interface PentaPredicate<T, U, V, W, K> {
        boolean test(T t, U u, V v, W w, K k);
    }
    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
    @FunctionalInterface
    public interface PentaConsumer<T, U, V, W, K> {
        void accept(T t, U u, V v, W w, K k);
    }
    @FunctionalInterface
    public interface PentaFunction<T, U, V, W, K, R> {
        R apply(T t, U u, V v, W w, K k);
    }

}
