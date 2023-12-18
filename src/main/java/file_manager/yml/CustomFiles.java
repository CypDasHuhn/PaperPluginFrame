package file_manager.yml;

import FrameMain;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomFiles {
    private File file;
    private FileConfiguration customFile;
    public FileConfiguration getFileConfiguration(String name, String folder) {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin(FrameMain.pluginName).getDataFolder()+"/"+folder, name+".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
        return customFile;
    }

    public void save() {
        try {
            if (this.customFile != null){
                customFile.save(file);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void saveArray(CustomFiles[] customFiles) {
        for (CustomFiles customFile : customFiles) {
            customFile.save();
        }
    }

    public static CustomFiles[] getCustomFiles(int amount) {
        CustomFiles[] customFiles = new CustomFiles[amount];
        for (int i = 0; i < amount; i++) {
            customFiles[i] = new CustomFiles();
        }return customFiles;
    }

    public void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public void delete(String name, String folder) {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin(FrameMain.pluginName).getDataFolder() + "/" + folder, name + ".yml");

        if (file.exists()) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                file.delete();
            }
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }

        directory.delete();
    }

}