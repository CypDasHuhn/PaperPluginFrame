package command.general;

import org.bukkit.command.CommandSender;

import java.util.List;

public class Argument {
    TriPredicate<CommandSender, String[], String> isArgument;
    TriPredicate<CommandSender, String[], String> isValid;
    TriFunction<CommandSender, String[], String, List<String>> tabCompletions;
    TriConsumer<CommandSender, String[], String> errorInvalid;
    TriConsumer<CommandSender, String[], String> errorMissing;
    TriConsumer<CommandSender, String[], String> invoke;
    boolean modifier;
    List<Argument> followingArguments;

    public Argument(TriPredicate<CommandSender, String[], String> isArgument, TriPredicate<CommandSender, String[], String> isValid,
                    TriFunction<CommandSender, String[], String, List<String>> tabCompletions, TriConsumer<CommandSender, String[], String> errorInvalid,
                    TriConsumer<CommandSender, String[], String> errorMissing, TriConsumer<CommandSender, String[], String> invoke,
                    boolean modifier, List<Argument> followingArguments) {

        this.isArgument = isArgument;
        this.isValid = isValid;
        this.tabCompletions = tabCompletions;
        this.errorInvalid = errorInvalid;
        this.errorMissing = errorMissing;
        this.invoke = invoke;
        this.modifier = modifier;
        this.followingArguments = followingArguments;
    }

    @FunctionalInterface
    public interface TriPredicate<T, U, V> {
        boolean test(T t, U u, V v);
    }
    @FunctionalInterface
    public interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }
    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
    @FunctionalInterface
    public interface QuadPredicate<T, U, V, W> {
        boolean test(T t, U u, V v, W w);
    }
    @FunctionalInterface
    public interface QuadFunction<T, U, V, W, R> {
        R apply(T t, U u, V v, W w);
    }

    @FunctionalInterface
    public interface QuadConsumer<T, U, V, W> {
        void accept(T t, U u, V v, W w);
    }

}
