package me.lluiscamino.multiversehardcore;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import me.lluiscamino.multiversehardcore.commands.HelpCommand;
import me.lluiscamino.multiversehardcore.commands.MainCommand;
import me.lluiscamino.multiversehardcore.events.PlayerChangeOfWorld;
import me.lluiscamino.multiversehardcore.events.PlayerDeath;
import me.lluiscamino.multiversehardcore.events.PlayerJoin;
import me.lluiscamino.multiversehardcore.files.HardcoreWorldsList;
import me.lluiscamino.multiversehardcore.utils.MessageSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class MultiverseHardcore extends JavaPlugin {

    private static MultiverseHardcore instance;
    private MVWorldManager MVWorldManager;
    private final boolean testing;

    public MultiverseHardcore() {
        testing = false;
    }

    // Constructor needed for tests.
    protected MultiverseHardcore(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        MVWorldManager = null;
        testing = true;
    }

    public static MultiverseHardcore getInstance() {
        return instance;
    }

    public MVWorldManager getMVWorldManager() {
        return MVWorldManager;
    }

    public void setMVWorldManager(MVWorldManager MVWorldManager) {
        this.MVWorldManager = MVWorldManager;
    }

    @Override
    public void onEnable() {
        instance = this;
        if (!testing) {
            loadMultiverseCore();
        }
        saveDefaultConfig();
        loadMessagesPrefix();
        loadEventListeners();
        loadCommands();
        scheduleWorldCleanUp();
    }

    private void loadMultiverseCore() {
        Plugin multiversePlugin = getServer().getPluginManager().getPlugin("Multiverse-Core");
        if (multiversePlugin instanceof MultiverseCore) {
            MVWorldManager = ((MultiverseCore) multiversePlugin).getMVWorldManager();
        } else {
            throw new RuntimeException("Multiverse-Core not found!");
        }
    }

    private void loadMessagesPrefix() {
        String prefix = getConfig().getString("prefix");
        if (prefix != null) {
            MessageSender.setPrefix(prefix);
        }
    }

    private void loadEventListeners() {
        Listener[] listeners = {new PlayerDeath(), new PlayerChangeOfWorld(), new PlayerJoin()};
        for (Listener listener : listeners) {
            loadEventListener(listener);
        }
    }

    private void loadEventListener(Listener eventListener) {
        getServer().getPluginManager().registerEvents(eventListener, this);
    }

    private void loadCommands() {
        PluginCommand mainCommand = getCommand("mvhc");
        PluginCommand helpCommand = getCommand("mvhchelp");
        if (mainCommand != null && helpCommand != null) {
            mainCommand.setExecutor(new MainCommand());
            helpCommand.setExecutor(new HelpCommand());
        } else {
            throw new RuntimeException("Multiverse-Hardcore Command not found!");
        }
    }

    private void scheduleWorldCleanUp() {
        int cleanWorldsTicks = getConfig().getInt("clean_worlds_ticks");
        getServer().getScheduler().runTaskLater(this, HardcoreWorldsList.instance::cleanWorlds, cleanWorldsTicks);
    }
}
