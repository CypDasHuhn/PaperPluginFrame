package command.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CustomTabCompleter implements TabCompleter {
    // if the label (the command written) equals to one of the aliases in aliasesMap,
    // it takes the result of the linked completer and returns it.
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> arguments = new ArrayList<>();

        for (HashMap.Entry<String, String[]> entry : CustomCommand.aliasesMap.entrySet()) {
            String commandLabel = entry.getKey();
            String[] aliases = entry.getValue();

            if (label.equalsIgnoreCase(commandLabel) || Arrays.stream(aliases).anyMatch(label::equalsIgnoreCase)) {
                SkeletonCommand skeletonCommand = CustomCommand.commandMap.get(commandLabel);
                arguments = skeletonCommand.completer(sender, args, label);
            }
        }

        int argEnd = args.length - 1;
        List<String> result = new ArrayList<>();
        for (String argument : arguments) {
            if (argument.toLowerCase().startsWith(args[argEnd].toLowerCase())) {
                result.add(argument);
            }
        }

        return result;
    }

}
