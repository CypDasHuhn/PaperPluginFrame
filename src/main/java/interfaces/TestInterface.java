package interfaces;

import dto.interface_context.ContextDTO;
import dto.interface_context.TestContextDTO;
import interfaces.general.ClickableItem;
import interfaces.general.Interface;
import interfaces.general.SkeletonInterface;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import shared.SpigotMethods;

import java.util.ArrayList;
import java.util.List;

public class TestInterface extends SkeletonInterface {
    @Override
    public Inventory getInventory(Player player, ContextDTO context) {
        TestContextDTO testContext = (TestContextDTO) context;
        return Bukkit.createInventory(null, testContext.inventoryRows*9, testContext.inventoryName);
    }
    @Override
    public String getInterfaceName() {
        return "test";
    }
    @Override
    public List<ClickableItem> getItems() {
        return new ArrayList<>() {{
            add(new ClickableItem(
                    (slot, currentContext) -> slot == 0,
                    (slot, currentContext) -> new ItemStack(Material.DIAMOND_SWORD),
                    (currentContext, clickDTO) -> {
                        clickDTO.player.sendMessage("Hallo!");
                    }
            ));
            add(new ClickableItem(
                    (slot, currentContext) -> slot > 2 && slot < 7,
                    (slot, currentContext) -> {
                        TestContextDTO testContext = (TestContextDTO) currentContext;
                        return SpigotMethods.createItem(testContext.material,"cool", false, null, null);
                    },
                    (currentContext, clickDTO) -> {
                        TestContextDTO testContext = (TestContextDTO) currentContext;
                        if (clickDTO.material.equals(Material.GRASS_BLOCK)) {
                            testContext.material = Material.PLAYER_HEAD;
                        } else {
                            testContext.material = Material.GRASS_BLOCK;
                        }
                        Interface.openCurrentInterface(clickDTO.player, testContext);
                    }
            ));
        }};
    }
}
