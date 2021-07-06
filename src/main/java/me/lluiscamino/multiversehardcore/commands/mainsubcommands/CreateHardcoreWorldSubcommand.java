package me.lluiscamino.multiversehardcore.commands.mainsubcommands;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import me.lluiscamino.multiversehardcore.commands.HelpCommand;
import me.lluiscamino.multiversehardcore.commands.MainSubcommand;
import me.lluiscamino.multiversehardcore.exceptions.HardcoreWorldCreationException;
import me.lluiscamino.multiversehardcore.exceptions.InvalidCommandInputException;
import me.lluiscamino.multiversehardcore.models.HardcoreWorld;
import me.lluiscamino.multiversehardcore.models.HardcoreWorldConfiguration;
import me.lluiscamino.multiversehardcore.utils.MessageSender;
import me.lluiscamino.multiversehardcore.utils.WorldUtils;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public final class CreateHardcoreWorldSubcommand extends MainSubcommand {

    private boolean createNether, createEnd;
    private String worldName;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender.hasPermission("multiversehardcore.create")) {
            try {
                initProperties(sender, args);
                checkValidInput();
                attemptWorldCreation();
                makeWorldHardcore();
                sendSuccessMessage();
            } catch (HardcoreWorldCreationException e) {
                handleHardcoreWorldCreationException(e);
            } catch (InvalidCommandInputException e) {
                MessageSender.sendError(sender, e.getMessage());
            }
        } else {
            MessageSender.sendError(sender, MainSubcommand.PERMISSION_ERROR);
        }
        return true;
    }

    protected void initProperties(@NotNull CommandSender sender, @NotNull String[] args) {
        super.initProperties(sender, args);
        createNether = args.length > 3 && Boolean.parseBoolean(args[3]);
        createEnd = args.length > 4 && Boolean.parseBoolean(args[4]);
        worldName = args.length > 1 ? args[1] : "";
    }

    private void checkValidInput() throws InvalidCommandInputException {
        checkCommandContainsWorldName();
        checkWorldDoesNotExist();
    }

    private void checkWorldDoesNotExist() throws InvalidCommandInputException {
        if (WorldUtils.worldExists(worldName)) {
            throw new InvalidCommandInputException("World " + worldName + " already exists");
        }
    }

    private void checkCommandContainsWorldName() throws InvalidCommandInputException {
        if (args.length < 2) {
            throw new InvalidCommandInputException(getWrongUsageMessage(HelpCommand.CREATE_COMMAND));
        }
    }

    private void attemptWorldCreation() throws InvalidCommandInputException {
        MessageSender.sendInfo(sender, "Starting creation of world(s)...");
        if (!createWorlds()) {
            throw new InvalidCommandInputException("World(s) could not be created!");
        }
    }

    private void makeWorldHardcore() throws HardcoreWorldCreationException {
        HardcoreWorldConfiguration configuration = getConfigurationFromArgs();
        HardcoreWorld.createHardcoreWorld(configuration);
    }

    private void sendSuccessMessage() {
        MessageSender.sendSuccess(sender, "World " + ChatColor.DARK_GREEN + worldName + ChatColor.GREEN + " created!");
    }

    private void handleHardcoreWorldCreationException(@NotNull HardcoreWorldCreationException exception) {
        MVWorldManager worldManager = plugin.getMVWorldManager();
        MessageSender.sendError(sender, exception.getMessage());
        worldManager.deleteWorld(worldName);
        if (createNether) worldManager.deleteWorld(worldName + "_nether");
        if (createEnd) worldManager.deleteWorld(worldName + "_the_end");
    }

    private HardcoreWorldConfiguration getConfigurationFromArgs() {
        return new HardcoreWorldConfiguration(
                plugin.getServer().getWorld(args[1]),
                args.length > 9 ? plugin.getServer().getWorld(args[9]) : null,
                new Date(),
                args.length <= 5 || Boolean.parseBoolean(args[5]),
                args.length > 6 ? Long.parseLong(args[6]) : 30,
                args.length <= 2 || Boolean.parseBoolean(args[2]),
                args.length <= 7 || Boolean.parseBoolean(args[7]),
                args.length <= 8 || Boolean.parseBoolean(args[8]));
    }

    private boolean createWorlds() {
        return createOverworld() && createNetherIfNecessary() && createEndIfNecessary();
    }

    private boolean createOverworld() {
        return createHardcoreWorld(worldName, World.Environment.NORMAL);
    }

    private boolean createNetherIfNecessary() {
        MVWorldManager worldManager = plugin.getMVWorldManager();
        if (createNether && !createHardcoreWorld(worldName + "_nether", World.Environment.NETHER)) {
            worldManager.deleteWorld(worldName);
            return false;
        }
        return true;
    }

    private boolean createEndIfNecessary() {
        MVWorldManager worldManager = plugin.getMVWorldManager();
        if (createEnd && !createHardcoreWorld(worldName + "_the_end", World.Environment.THE_END)) {
            worldManager.deleteWorld(worldName);
            if (createNether) worldManager.deleteWorld(worldName + "_nether");
            return false;
        }
        return true;
    }

    private boolean createHardcoreWorld(@NotNull String worldName, @NotNull World.Environment environment) {
        try {
            attemptWorldCreation(worldName, environment);
            makeWorldAttributesHardcore(worldName);
            return true;
        } catch (HardcoreWorldCreationException e) {
            return false;
        }
    }

    private void attemptWorldCreation(@NotNull String worldName, @NotNull World.Environment environment)
            throws HardcoreWorldCreationException {
        MVWorldManager worldManager = plugin.getMVWorldManager();
        HardcoreWorldCreationException worldCreationException =
                new HardcoreWorldCreationException("World " + worldName + " could not be created");
        try {
            if (!worldManager.addWorld(worldName, environment, "", WorldType.NORMAL, true, "")) {
                throw worldCreationException;
            }
        } catch (IllegalArgumentException e) {
            throw worldCreationException;
        }
    }

    private void makeWorldAttributesHardcore(@NotNull String worldName) {
        MVWorldManager worldManager = plugin.getMVWorldManager();
        MultiverseWorld world = worldManager.getMVWorld(worldName);
        world.setColor("DARKRED");
        world.setDifficulty(Difficulty.HARD);
    }
}
