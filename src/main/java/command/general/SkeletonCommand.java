package command.general;

import org.bukkit.command.CommandSender;

import java.util.List;

public class SkeletonCommand {
    // command to override
    public void command(CommandSender sender, String[] args, String label) {

    }

    // completer to override
    public List<String> completer(CommandSender sender, String[] args, String label) {
        return null;
    }
}
