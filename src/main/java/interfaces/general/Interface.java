package interfaces.general;

import dto.interface_context.ContextDTO;
import file_manager.routing.PlayerDataManager;
import interfaces.TestInterface;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Interface {
    public static HashMap<Player, Boolean> opening = new HashMap<>();
    public static final List<SkeletonInterface> interfaceMap = new ArrayList<>() {{
        add(new TestInterface());
    }};

    public static void openTargetInterface(Player player, String interfaceName, ContextDTO context) {
        System.out.println(interfaceName);

        SkeletonInterface skeletonInterface = null;
        for (SkeletonInterface currentSkeletonInterface : interfaceMap) {
            if (currentSkeletonInterface.getInterfaceName().equals(interfaceName)) skeletonInterface = currentSkeletonInterface;
        }

        if (skeletonInterface == null) return;

        System.out.println("durchgekommen! "+interfaceName);

        opening.put(player, true);
        PlayerDataManager.setInventory(player, interfaceName);

        Inventory inventory = skeletonInterface.getInventory(player, context);
        inventory = fillInventory(inventory, skeletonInterface.getItems(), context);
        player.openInventory(inventory);

        opening.put(player, false);
    }

    public static void openCurrentInterface(Player player, ContextDTO context) {
        String interfaceName = PlayerDataManager.getInventory(player);

        openTargetInterface(player, interfaceName, context);
    }

    public static Inventory fillInventory(Inventory inventory, List<ClickableItem> items, ContextDTO context) {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ClickableItem clickableItem = null;
            for(ClickableItem currentClickableItem : items) {
                if (currentClickableItem.testCondition(slot, context)) clickableItem = currentClickableItem;
            }

            if (clickableItem != null) {
                ItemStack item = clickableItem.createItemStack(context, slot);
                inventory.setItem(slot, item);
            }
        }

        return inventory;
    }
}
