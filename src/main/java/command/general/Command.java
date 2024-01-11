package command.general;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.logging.Logger;

public class Command implements CommandExecutor {
    public static final List<Argument> arguments = new ArrayList<>() {{
        add(new Argument(
                (sender, args, value, index, values) -> value.equalsIgnoreCase("testCommand"),
                (sender, args, value, index, values) -> true,
                null,
                null,
                null,
                (sender, args, value, index, values) -> value,
                null,
                "label",
                new ArrayList<>(){{
                    add(new Argument(
                            (sender, args, value, index, values) -> value.equalsIgnoreCase("-g"),
                            (sender, args, value, index, values) -> new ArrayList<>() {{add("-g");}},
                            (sender, args, value, index, values) -> value,
                            "global"
                    ));
                    add(new Argument(
                            (sender, args, value, index, values) -> value.equalsIgnoreCase("cool"),
                            null,
                            (sender, args, value, index, values) -> new ArrayList<>() {{add("cool");}},
                            null,
                            (sender, args, value, index, values) -> System.out.println("Write a version of cool"),
                            (sender, args, value, index, values) -> value,
                            (sender, args, values) -> {
                                boolean global = (boolean) values.get("global");
                                if (global) System.out.println("Success! (1)");
                                System.out.println("Success! (1)");
                            },
                            "cool",
                            null
                    ));
                    add(new Argument(
                            (sender, args, value, index, values) -> value.equalsIgnoreCase("Coolsta!"),
                            null,
                            (sender, args, value, index, values) -> new ArrayList<>() {{add("cool");}},
                            null,
                            (sender, args, value, index, values) -> System.out.println("Write a version of cool"),
                            (sender, args, value, index, values) -> value,
                            (sender, args, values) -> {
                                boolean global = (boolean) values.get("global");
                                if (global) System.out.println("Success! (2)");
                                System.out.println("Success! (2)");
                            },
                            "coolsta",
                            null
                    ));
                }}
        ));
    }};
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        List<String> list = new LinkedList<>(Arrays.asList(args));
        list.add(0, label);
        final String[] finalArgs = list.toArray(new String[list.size()]);

        Bukkit.broadcastMessage(arguments.get(0).followingArguments.size()+"");
        List<Argument> argumentsCopy = arguments;

        HashMap<String, Object> values = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            final String arg = args[i];
            final int finalI = i;

            Optional<Argument> currentOptionalArgument = argumentsCopy.stream()
                    .filter(obj -> obj.isArgument.test(sender, finalArgs, arg, finalI, values))
                    .findFirst();

            if (currentOptionalArgument.isPresent()) {
                Argument currentArgument = currentOptionalArgument.get();

                if (currentArgument.isValid == null || currentArgument.isValid.test(sender, finalArgs, arg, finalI, values)){
                    values.put(currentArgument.key,currentArgument.argumentHandler.apply(sender, finalArgs, arg, finalI, values));
                    if (i+1 == args.length) {
                        if (currentArgument.invoke != null) {
                            currentArgument.invoke.accept(sender, finalArgs, values);
                        } else {
                            List<Argument> inferiorArguments = currentArgument.followingArguments;
                            Argument firstInferiorArgument = inferiorArguments.stream()
                                    .filter(obj -> obj.errorMissing != null)
                                    .findFirst().get();

                            firstInferiorArgument.errorMissing.accept(sender, finalArgs, arg, i, values);
                            return false;
                        }
                    } else {
                        if (currentArgument.modifier) {
                            argumentsCopy.remove(currentArgument);
                        } else {
                            List<Argument> inferiorArguments = currentArgument.followingArguments;
                            Argument firstInferiorArgument = inferiorArguments.stream()
                                    .filter(obj -> obj.errorMissing != null)
                                    .findFirst().get();

                            argumentsCopy.stream().filter((obj) -> obj.modifier).forEach((mObj) -> {
                                if (!values.containsKey(mObj.key)) values.put(mObj.key, false);
                            });

                            argumentsCopy = firstInferiorArgument.followingArguments;
                            Bukkit.broadcastMessage("guck mal!");
                        }
                    }
                } else {
                    currentArgument.errorInvalid.accept(sender, finalArgs, arg, finalI, values);
                    return false;
                }
            } else {
                Optional<Argument> firstArgument = argumentsCopy.stream()
                        .filter(obj -> obj.errorMissing != null)
                        .findFirst();

                if (firstArgument.isPresent()) {
                    firstArgument.get().errorMissing.accept(sender, finalArgs, arg, i, values);
                }
                return false;
            }
        }

        return true;
    }
}
