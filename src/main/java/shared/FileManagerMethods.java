package shared;

import java.util.ArrayList;
import java.util.List;

public class FileManagerMethods {
    static List<String> illegalCharacters = new ArrayList<String>(){{
        add("/");
        add("\\");
        add(":");
        add("*");
        add("?");
        add("\"");
        add("<");
        add(">");
        add("|");
    }};
    static List<String> illegalName = new ArrayList<String>(){{

    }};
    public static boolean illegalName(String name) {
        for (String character : illegalCharacters) {
            if (name.contains(character)) return false;
        }
        return illegalName.contains(name);
    }
}
