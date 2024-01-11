package main;

import caching.Cache;
import com.destroystokyo.paper.utils.PaperPluginLogger;
import command.general.Command;
import command.general.Completer;
import command.general.CustomCommand;
import command.general.CustomTabCompleter;
import listeners.InventoryClickListener;
import listeners.InventoryCloseListener;
import listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class FrameMain extends JavaPlugin {
    private FrameMain plugin;
    public static boolean usesSQL;
    public static final String pluginName = "FramePlugin";
    private final Listener[] LISTENERS = {new InventoryClickListener(), new InventoryCloseListener(), new PlayerJoinListener()};
    public static Cache<String, Object> cache = new Cache<>();

    public void onEnable(){
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        //plugin = this;

        for (String command : CustomCommand.aliasesMap.keySet()) {
            if (getCommand(command) == null) {
                PaperPluginLogger.getLogger(this.getPluginMeta()).log(Level.WARNING, "command in custom command registered, not in plugin.yml defined");
                break;
            }
            getCommand(command).setExecutor(new CustomCommand());
            getCommand(command).setTabCompleter(new CustomTabCompleter());
        }

        getCommand("testCommand").setExecutor(new Command());
        getCommand("testCommand").setTabCompleter(new Completer());

        PluginManager pluginManager = Bukkit.getPluginManager();
        for (Listener listener : LISTENERS) {
            pluginManager.registerEvents(listener, this);
        }
    }

    public void onDisable() {
        if (cache != null) {
            cache.shutdownScheduler();
        }

        // Any other cleanup code goes here
    }
}
