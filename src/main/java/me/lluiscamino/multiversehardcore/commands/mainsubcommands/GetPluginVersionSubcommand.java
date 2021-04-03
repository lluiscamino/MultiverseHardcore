package me.lluiscamino.multiversehardcore.commands.mainsubcommands;

import me.lluiscamino.multiversehardcore.commands.MainSubcommand;
import me.lluiscamino.multiversehardcore.exceptions.InvalidCommandInputException;
import me.lluiscamino.multiversehardcore.utils.MessageSender;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class GetPluginVersionSubcommand extends MainSubcommand {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        try {
            initProperties(sender, args);
            checkSenderIsOp();
            String pluginVersion = plugin.getDescription().getVersion();
            MessageSender.sendInfo(sender, "Version: " + pluginVersion);
        } catch (InvalidCommandInputException e) {
            MessageSender.sendError(sender, e.getMessage());
        }
    }
}
