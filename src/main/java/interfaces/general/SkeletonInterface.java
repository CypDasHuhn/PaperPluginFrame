package interfaces.general;

import dto.interface_context.ContextDTO;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class SkeletonInterface {
    public Inventory getInventory(Player player, ContextDTO context) {
        return Bukkit.createInventory(null, 9, "interface");
    }
    public String getInterfaceName() {
        return "interface";
    }
    public List<ClickableItem> getItems() {
        return new ArrayList<>();
    }
}
