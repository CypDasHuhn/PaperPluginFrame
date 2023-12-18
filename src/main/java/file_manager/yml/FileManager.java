package file_manager.yml;

import org.bukkit.configuration.file.FileConfiguration;

public class FileManager {
    public enum ObjectTypes {
        STRING,
        INT,
        BOOL,
        ITEM_STACK,
        OBJECT
    }
    public static Object get(FileConfiguration fileConfiguration, String dir) {
        return fileConfiguration.getSerializable("", );
    }
}
