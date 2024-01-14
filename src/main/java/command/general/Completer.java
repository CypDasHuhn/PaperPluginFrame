package command.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class Completer implements TabCompleter {
    static final List<String> emptyResult = new ArrayList<>();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command commands, String label, String[] args) {
        List<String> list = new LinkedList<>(Arrays.asList(args));
        list.add(0, label);
        final String[] finalArgs = list.toArray(new String[list.size()]);

        List<Argument> argumentsCopy = command.general.Command.getArguments();

        HashMap<String, Object> values = new HashMap<>();
        for (int i = 0; i < finalArgs.length; i++) {
            final String arg = finalArgs[i];
            final int finalI = i;

            if (finalI+1 == finalArgs.length) {
                List<String> tabCompletions = new ArrayList<>();
                argumentsCopy.forEach((obj) -> tabCompletions.addAll(obj.tabCompletions.apply(sender, finalArgs, arg, finalI, values)));

                int argEnd = finalArgs.length - 1;
                List<String> result = new ArrayList<>();
                for (String tabCompletion : tabCompletions) {
                    if (tabCompletion.toLowerCase().startsWith(finalArgs[argEnd].toLowerCase())) {
                        result.add(tabCompletion);
                    }
                }

                return result;
            } else{
                Optional<Argument> currentOptionalArgument = argumentsCopy.stream()
                        .filter(obj -> obj.isArgument.test(sender, finalArgs, arg, finalI, values))
                        .findFirst();

                if (currentOptionalArgument.isEmpty()) return emptyResult;

                Argument currentArgument = currentOptionalArgument.get();

                if (!(currentArgument.isValid == null || currentArgument.isValid.test(sender, finalArgs, arg, finalI, values))) return emptyResult;

                values.put(currentArgument.key, currentArgument.argumentHandler.apply(sender, finalArgs, arg, finalI, values));
                argumentsCopy.stream().filter((a) -> a.modifier).forEach((ma) -> {
                    if (!values.containsKey(ma.key)) values.put(ma.key, false);
                });

                if (currentArgument.modifier) {
                    argumentsCopy.remove(currentArgument);
                } else {
                    argumentsCopy = currentArgument.followingArguments;
                    if (argumentsCopy == null) return emptyResult;
                }
            }
        }

        return emptyResult;
    }
}
