package command.general;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor {
    private List<Argument> arguments = new ArrayList<Argument>() {{
        add(new Argument(
                (sender, args, label) -> label.equalsIgnoreCase("testCommand"),
                (sender, args, label) -> true,
                (sender, args, label) -> new ArrayList<>() {{
                    add("cool");
                    add("lol");
                }},
                null,
                (sender, args, label) -> System.out.println("Enter a following command!"),
                null,
                false,
                new ArrayList<Argument>(){{
                    add(new Argument(
                            (sender, args, label) -> false,
                            (sender, args, label) -> false,
                            (sender, args, label) -> new ArrayList<String>(),
                            (sender, args, label) -> System.out.println("Error, invalid!"),
                            null,
                            (sender, args, label) -> System.out.println("Success!"),
                            false,
                            null
                    ));
                    add(new Argument(
                            (sender, args, label) -> false,
                            (sender, args, label) -> false,
                            (sender, args, label) -> new ArrayList<String>(),
                            (sender, args, label) -> System.out.println("Error, invalid!"),
                            null,
                            (sender, args, label) -> System.out.println("Success!"),
                            false,
                            null
                    ));
                }}
        ));
    }};
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        for (Argument argument : arguments) {
            if (argument.isArgument.test(sender, args, label)) {
                if (argument.isValid == null || argument.isValid.test(sender, args, label)) {
                    // either invoke, or
                } else {
                    argument.errorInvalid.accept(sender, args, label);
                }
                break;
            }
        }
        return false;
    }
}
