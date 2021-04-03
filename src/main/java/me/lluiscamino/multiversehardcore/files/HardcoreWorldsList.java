package me.lluiscamino.multiversehardcore.files;

import me.lluiscamino.multiversehardcore.exceptions.HardcoreWorldCreationException;
import me.lluiscamino.multiversehardcore.exceptions.WorldIsNotHardcoreException;
import me.lluiscamino.multiversehardcore.models.HardcoreWorldConfiguration;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HardcoreWorldsList extends List {

    public static final HardcoreWorldsList instance = new HardcoreWorldsList();
    private static final String FILE_NAME = "worlds.yml";

    public HardcoreWorldsList() {
        super(FILE_NAME);
    }

    public void cleanWorlds() {
        for (String worldName : listFile.getValues(false).keySet()) {
            World world = plugin.getServer().getWorld(worldName);
            if (world == null) {
                deleteHardcoreWorld(worldName);
                plugin.getLogger().info("Removed world " + worldName + " from " + FILE_NAME);
            }
        }
    }

    public String[] getHardcoreWorldNames() {
        int worldCount = listFile.getValues(false).size();
        return listFile.getValues(false).keySet().toArray(new String[worldCount]);
    }

    public HardcoreWorldConfiguration getHardcoreWorldConfiguration(@NotNull String worldName)
            throws WorldIsNotHardcoreException {
        if (!containsWorld(worldName)) throw new WorldIsNotHardcoreException("World " + worldName + " is not Hardcore");
        Map<String, Object> worldConfig = listFile.getConfigurationSection(worldName).getValues(false);
        return new HardcoreWorldConfiguration(worldName, worldConfig);
    }

    public void addHardcoreWorld(@NotNull HardcoreWorldConfiguration configuration)
            throws HardcoreWorldCreationException {
        String worldName = configuration.getWorld().getName();
        if (containsWorld(worldName))
            throw new HardcoreWorldCreationException("World " + worldName + " already exists.");
        update(worldName, configuration.toMap());
    }

    private void update(@NotNull String worldName, Map<String, Object> newWorld) {
        if (containsWorld(worldName)) {
            listFile.set(worldName, newWorld); // Save world
        } else {
            listFile.createSection(worldName, newWorld); // Create new world
        }
        save();
    }

    private boolean containsWorld(@NotNull String worldName) {
        return listFile.isConfigurationSection(worldName);
    }

    private void deleteHardcoreWorld(@NotNull String worldName) {
        listFile.set(worldName, null);
        PlayersList.instance.deleteWorldDeathBans(worldName);
        save();
    }
}
