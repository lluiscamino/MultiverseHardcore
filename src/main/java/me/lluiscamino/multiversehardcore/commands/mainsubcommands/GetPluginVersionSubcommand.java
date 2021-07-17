package me.lluiscamino.multiversehardcore.commands.mainsubcommands;

import me.lluiscamino.multiversehardcore.commands.MainSubcommand;
import me.lluiscamino.multiversehardcore.exceptions.InvalidCommandInputException;
import me.lluiscamino.multiversehardcore.utils.MessageSender;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class GetPluginVersionSubcommand extends MainSubcommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender.hasPermission("multiversehardcore.version")) {
            initProperties(sender, args);
            String pluginVersion = plugin.getDescription().getVersion();
            MessageSender.sendInfo(sender, "Version: " + pluginVersion);
        } else {
            MessageSender.sendError(sender, MainSubcommand.PERMISSION_ERROR);
        }
        return true;
    }
}
