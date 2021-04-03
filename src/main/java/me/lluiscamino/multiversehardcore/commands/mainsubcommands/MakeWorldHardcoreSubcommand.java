package me.lluiscamino.multiversehardcore.commands.mainsubcommands;

import me.lluiscamino.multiversehardcore.commands.HelpCommand;
import me.lluiscamino.multiversehardcore.commands.MainSubcommand;
import me.lluiscamino.multiversehardcore.exceptions.HardcoreWorldCreationException;
import me.lluiscamino.multiversehardcore.exceptions.InvalidCommandInputException;
import me.lluiscamino.multiversehardcore.models.HardcoreWorld;
import me.lluiscamino.multiversehardcore.models.HardcoreWorldConfiguration;
import me.lluiscamino.multiversehardcore.utils.MessageSender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public final class MakeWorldHardcoreSubcommand extends MainSubcommand {

    private String worldName;

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        try {
            initProperties(sender, args);
            checkValidInput();
            makeWorldHardcore();
            sendSuccessMessage();
        } catch (HardcoreWorldCreationException | InvalidCommandInputException e) {
            MessageSender.sendError(sender, e.getMessage());
        }
    }

    protected void initProperties(@NotNull CommandSender sender, @NotNull String[] args) {
        super.initProperties(sender, args);
        worldName = args.length > 1 ? args[1] : "";
    }

    private void checkValidInput() throws InvalidCommandInputException {
        checkSenderIsOp();
        checkCommandContainsWorldName();
    }

    private void checkCommandContainsWorldName() throws InvalidCommandInputException {
        if (args.length < 2) {
            throw new InvalidCommandInputException(getWrongUsageMessage(HelpCommand.MAKE_COMMAND));
        }
    }

    private void makeWorldHardcore() throws HardcoreWorldCreationException {
        HardcoreWorldConfiguration configuration = getConfigurationFromArgs(args);
        HardcoreWorld.createHardcoreWorld(configuration);
    }

    private void sendSuccessMessage() {
        MessageSender.sendSuccess(sender, "World " + ChatColor.DARK_GREEN + worldName + ChatColor.GREEN + " is now Hardcore!");
    }

    private HardcoreWorldConfiguration getConfigurationFromArgs(@NotNull String[] args) {
        return new HardcoreWorldConfiguration(
                plugin.getServer().getWorld(args[1]),
                args.length > 8 ? plugin.getServer().getWorld(args[8]) : null,
                new Date(),
                args.length <= 3 || Boolean.parseBoolean(args[3]),
                args.length <= 4 || Boolean.parseBoolean(args[4]),
                args.length > 5 ? Long.parseLong(args[5]) : 30,
                args.length <= 2 || Boolean.parseBoolean(args[2]),
                args.length <= 6 || Boolean.parseBoolean(args[6]),
                args.length <= 7 || Boolean.parseBoolean(args[7]));
    }
}
