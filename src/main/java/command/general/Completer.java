package command.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

import static command.general.Command.arguments;

public class Completer implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command commands, String label, String[] args) {
        List<String> list = new LinkedList<>(Arrays.asList(args));
        list.add(0, label);
        final String[] finalArgs = list.toArray(new String[list.size()]);

        List<Argument> argumentsCopy = arguments;

        HashMap<String, Object> values = new HashMap<>();
        for (int i = 0; i < finalArgs.length; i++) {
            final String arg = finalArgs[i];
            final int finalI = i;

            Optional<Argument> currentOptionalArgument = argumentsCopy.stream()
                    .filter(obj -> obj.isArgument.test(sender, finalArgs, arg, finalI, values))
                    .findFirst();

            if (currentOptionalArgument.isEmpty()) return null;

            Argument currentArgument = currentOptionalArgument.get();

            if (!(currentArgument.isValid == null || currentArgument.isValid.test(sender, finalArgs, arg, finalI, values))) return null;

            values.put(currentArgument.key, currentArgument.argumentHandler.apply(sender, finalArgs, arg, finalI, values));
            argumentsCopy.stream().filter((a) -> a.modifier).forEach((ma) -> {
                if (!values.containsKey(ma.key)) values.put(ma.key, false);
            });

            if (i+1 == finalArgs.length) {
                return currentArgument.tabCompletions.apply(sender, finalArgs, arg, finalI, values);
            } else {
                if (currentArgument.modifier) {
                    argumentsCopy.remove(currentArgument);
                } else {
                    argumentsCopy = currentArgument.followingArguments;
                }
            }

        }

        return null;
    }
}
