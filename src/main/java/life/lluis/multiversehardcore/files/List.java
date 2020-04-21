package life.lluis.multiversehardcore.files;

import life.lluis.multiversehardcore.MultiverseHardcore;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class List {

    protected final String fileName;
    protected final MultiverseHardcore plugin;
    protected File file;
    protected FileConfiguration listFile;

    public List(String fileName) {
        plugin = MultiverseHardcore.getInstance();
        this.fileName = fileName;
        load();
    }

    protected void load() {
        file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create " + fileName);
                e.printStackTrace();
            }
        }
        listFile = new YamlConfiguration();
        try {
            listFile.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    protected void save() {
        try {
            listFile.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

}
