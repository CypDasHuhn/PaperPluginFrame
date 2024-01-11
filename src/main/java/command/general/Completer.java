package command.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class Completer implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command commands, String label, String[] args) {
        List<String> list = new LinkedList<>(Arrays.asList(args));
        list.add(0, label);
        args = list.toArray(new String[list.size()]);

        List<Argument> argumentsCopy = command.general.Command.arguments;
        String[] finalArgs = args;

        HashMap<String, Object> values = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            final String arg = args[i];
            final int finalI = i;

            Optional<Argument> currentOptionalArgument = argumentsCopy.stream()
                    .filter(obj -> obj.isArgument.test(sender, finalArgs, arg, finalI, values))
                    .findFirst();

            if (!currentOptionalArgument.isPresent()) return null;

            Argument currentArgument = currentOptionalArgument.get();

            if (!(currentArgument.isValid == null || currentArgument.isValid.test(sender, finalArgs, arg, finalI, values))) return null;

            values.put(currentArgument.key,currentArgument.argumentHandler.apply(sender, finalArgs, arg, finalI, values));
            if (i+1 == args.length) {
                List<String> tabCompletions = currentArgument.tabCompletions.apply(sender, finalArgs, arg, i, values);

                int argEnd = args.length - 1;
                List<String> result = new ArrayList<>();
                for (String argument : tabCompletions) {
                    if (argument.toLowerCase().startsWith(args[argEnd].toLowerCase())) {
                        result.add(argument);
                    }
                }
                return result;
            } else {
                argumentsCopy = currentArgument.followingArguments;
            }
        }
        return null;
    }
}
