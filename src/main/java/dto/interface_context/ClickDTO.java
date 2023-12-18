package dto.interface_context;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ClickDTO {
    public InventoryClickEvent event;
    public Player player;
    public ItemStack item;
    public Material material;
    public int slot;

    public ClickDTO(InventoryClickEvent event, Player player, ItemStack item, Material material, int slot) {
        this.event = event;
        this.player = player;
        this.item = item;
        this.material = material;
        this.slot = slot;
    }
}
