package life.lluis.multiversehardcore.files;

import life.lluis.multiversehardcore.MultiverseHardcore;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class List {

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
        try {
            file = new File(plugin.getDataFolder(), fileName);
            if (!file.exists() && !createNewFile()) {
                throw new RuntimeException("Could not create new file " + fileName);
            }
            listFile = new YamlConfiguration();
            listFile.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException("Could not load file " + fileName);
        }
    }

    protected void save() {
        try {
            listFile.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    private boolean createNewFile() {
        try {
            return file.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

}
