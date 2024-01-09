package command.general;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class Command implements CommandExecutor {
    private final List<Argument> arguments = new ArrayList<>() {{
        add(new Argument(
                (sender, args, value, index, values) -> value.equalsIgnoreCase("testCommand"),
                (sender, args, value, index, values) -> true,
                null,
                null,
                null,
                (sender, args, value, index, values) -> value,
                null,
                new ArrayList<>(){{
                    add(new Argument(
                            (sender, args, value, index, values) -> value.equalsIgnoreCase("-g"),
                            (sender, args, value, index, values) -> new ArrayList<>() {{add("-g");}},
                            (sender, args, value, index, values) -> value
                    ));
                    add(new Argument(
                            (sender, args, value, index, values) -> value.equalsIgnoreCase("cool"),
                            null,
                            (sender, args, value, index, values) -> new ArrayList<>() {{add("cool");}},
                            null,
                            (sender, args, value, index, values) -> System.out.println("Write a version of cool"),
                            (sender, args, value, index, values) -> value,
                            (sender, args, values) -> System.out.println("Success!"),
                            null
                    ));
                    add(new Argument(
                            (sender, args, value, index, values) -> value.equalsIgnoreCase("Coolsta!"),
                            null,
                            (sender, args, value, index, values) -> new ArrayList<>() {{add("cool");}},
                            null,
                            (sender, args, value, index, values) -> System.out.println("Write a version of cool"),
                            (sender, args, value, index, values) -> value,
                            (sender, args, values) -> System.out.println("Success!"),
                            null
                    ));
                }}
        ));
    }};
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        List<String> list = new LinkedList<>(Arrays.asList(args));
        list.add(0, label);
        args = list.toArray(new String[list.size()]);

        List<Argument> argumentsCopy = this.arguments;
        String[] finalArgs = args;

        HashMap<String, Object> values = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            final String arg = args[i];
            final int finalI = i;

            Optional<Argument> currentOptionalArgument = argumentsCopy.stream()
                    .filter(obj -> obj.isArgument.test(sender, finalArgs, arg, finalI, values))
                    .findFirst();

            Argument firstArgument = argumentsCopy.stream()
                    .filter(obj -> obj.errorMissing != null)
                    .findFirst().get();

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
                        }
                    }
                } else {
                    currentArgument.errorInvalid.accept(sender, finalArgs, arg, finalI, values);
                    return false;
                }
            } else {
                firstArgument.errorMissing.accept(sender, finalArgs, arg, i, values);
                return false;
            }
        }

        return true;
    }
}
