package command;

import command.general.SkeletonCommand;
import dto.interface_context.TestContextDTO;
import interfaces.TestInterface;
import interfaces.general.Interface;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TestCommand extends SkeletonCommand {
    @Override
    public void command(CommandSender sender, String[] args, String label) {
        System.out.println("interface!");
        TestContextDTO context = new TestContextDTO("cooler context",1, Material.GRASS_BLOCK);
        Interface.openTargetInterface((Player)sender, new TestInterface().getInterfaceName(), context);
    }

    @Override
    public List<String> completer(CommandSender sender, String[] args, String label) {
        return null;
    }
}
