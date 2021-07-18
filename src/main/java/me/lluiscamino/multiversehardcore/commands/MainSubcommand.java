package me.lluiscamino.multiversehardcore.commands;

import me.lluiscamino.multiversehardcore.MultiverseHardcore;
import me.lluiscamino.multiversehardcore.exceptions.InvalidCommandInputException;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class MainSubcommand {
    protected MultiverseHardcore plugin = MultiverseHardcore.getInstance();
    protected CommandSender sender;
    protected String[] args;

    public abstract boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args);

    protected abstract String getRequiredPermission();

    protected void initProperties(@NotNull CommandSender sender, @NotNull String[] args) {
        this.sender = sender;
        this.args = args;
    }

    protected void checkSenderHasPermission() throws InvalidCommandInputException {
        if (!sender.hasPermission(getRequiredPermission())) {
            throw new InvalidCommandInputException(getPermissionErrorMessage());
        }
    }

    protected void checkWorldExists(World world) throws InvalidCommandInputException {
        if (world == null) {
            throw new InvalidCommandInputException("World does not exist!");
        }
    }

    protected void checkPlayerExists(Player player) throws InvalidCommandInputException {
        if (player == null) {
            throw new InvalidCommandInputException("Player does not exist!");
        }
    }

    protected String getWrongUsageMessage(@NotNull String commandText) {
        return "Wrong usage: " + commandText;
    }

    private String getPermissionErrorMessage() {
        return "You need the following permission to run this command: " + getRequiredPermission();
    }
}
