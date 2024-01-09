package command;

import command.general.Argument;
import command.general.SkeletonCommand;
import file_manager.yml.CustomFiles;
import file_manager.yml.FileManager;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.*;

public class TestCommand extends SkeletonCommand {

    @Override
    public void command(CommandSender sender, String[] args, String label) {
        /*
        System.out.println("interface!");
        TestContextDTO context = new TestContextDTO("cooler context",1, Material.GRASS_BLOCK);
        Interface.openTargetInterface((Player)sender, new TestInterface().getInterfaceName(), context);
        */

        /*
        int value = (int) FrameMain.cache.cache("testValue", () -> {
            Bukkit.broadcastMessage("generating");
            return 5;
        }, 1000);
        Bukkit.broadcastMessage("Value is "+value);
        */

        CustomFiles[] customFiles = CustomFiles.getCustomFiles(1);
        FileConfiguration fileConfiguration = customFiles[0].getFileConfiguration("data", "");

        Player player = (Player) sender;
        List<ItemStack> items = Arrays.asList(player.getInventory().getContents());

        Map<Integer, EntityType> eggEntities = new HashMap<>();
        Map<Integer, String> eggMaterials = new HashMap<>();
        FileManager.setList(fileConfiguration, "Items.Item", items,
                (list, value) -> {
                    ItemStack item = (ItemStack) value;
                    return item != null && item.getItemMeta() instanceof SpawnEggMeta;
                }, (list, value, index) -> {
                    ItemStack item = (ItemStack) value;
                    SpawnEggMeta itemMeta = (SpawnEggMeta) item.getItemMeta();
                    EntityType entityType = itemMeta.getCustomSpawnedType();
                    Material material = item.getType();

                    eggEntities.put(index, entityType);
                    eggMaterials.put(index, material.toString());

                    return list;
                }
        );
        FileManager.set(fileConfiguration, "Items.EntityType",eggEntities);
        FileManager.set(fileConfiguration, "Items.EggMaterial",eggMaterials);

        CustomFiles.saveArray(customFiles);

        final Map<Integer, EntityType> eggEntitiesLoaded = (Map<Integer, EntityType>) FileManager.get(fileConfiguration, "Items.EntityType");
        final Map<Integer, String> eggMaterialsLoaded = (Map<Integer, String>) FileManager.get(fileConfiguration, "Items.EggMaterial");
        List<ItemStack> itemsLoaded = FileManager.getList(fileConfiguration, "Items.Item",
                (list, value) -> {
                    ItemStack item = (ItemStack) value;
                    return item != null &&  item.getItemMeta() instanceof SpawnEggMeta;
                }, (list, value, index) -> {
                    ItemStack item = (ItemStack) value;
                    SpawnEggMeta itemMeta = (SpawnEggMeta) item.getItemMeta();

                    itemMeta.setCustomSpawnedType(eggEntitiesLoaded.get(index));
                    item.setItemMeta(itemMeta);
                    item.setType(Material.valueOf(eggMaterialsLoaded.get(index)));

                    return item;
                }
        );

        ItemStack[] itemArray = itemsLoaded.toArray(new ItemStack[0]);
        player.getInventory().setContents(itemArray);

    }

    @Override
    public List<String> completer(CommandSender sender, String[] args, String label) {
        return null;
    }
}
