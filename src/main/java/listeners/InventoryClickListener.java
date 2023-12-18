package listeners;

import dto.interface_context.ClickDTO;
import dto.interface_context.ContextDTO;
import dto.interface_context.TestContextDTO;
import file_manager.routing.PlayerDataManager;
import interfaces.general.ClickableItem;
import interfaces.general.Interface;
import interfaces.general.SkeletonInterface;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import shared.Finals;

import java.util.Optional;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void listener(InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof PlayerInventory) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        String inventory = PlayerDataManager.getInventory(player);
        if (inventory.equals(Finals.EMPTY)) {
            return;
        }

        SkeletonInterface skeletonInterface = null;
        for (SkeletonInterface currentSkeletonInterface : Interface.interfaceMap) {
            if (currentSkeletonInterface.getInterfaceName().equals(inventory)) skeletonInterface = currentSkeletonInterface;
        }

        if (skeletonInterface == null) return;

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        Material clickedMaterial = clickedItem.getType();
        int clickedSlot = event.getSlot();

        event.setCancelled(true);

        ClickDTO click = new ClickDTO(event, player, clickedItem, clickedMaterial, clickedSlot);

        ContextDTO context = new TestContextDTO("lol", 1, Material.PLAYER_HEAD);

        ClickableItem clickableItem = null;
        for(ClickableItem currentClickableItem : skeletonInterface.getItems()) {
            if (currentClickableItem.testCondition(clickedSlot, context)) clickableItem = currentClickableItem;
        }

        if (clickableItem != null) {
            clickableItem.performAction(context, click);
        }
    }
}
