package me.lluiscamino.multiversehardcore.commands;

import me.lluiscamino.multiversehardcore.exceptions.InvalidMainSubcommandException;
import me.lluiscamino.multiversehardcore.utils.MessageSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        try {
            if (args.length == 0) {
                HelpCommand.printHelpDialog(sender);
            } else {
                MainSubcommandFactory subcommandFactory = new MainSubcommandFactory();
                MainSubcommand subcommand = subcommandFactory.getMainSubcommand(args[0]);
                subcommand.onCommand(sender, args);
            }
        } catch (InvalidMainSubcommandException e) {
            MessageSender.sendError(sender, e.getMessage());
        }
        return true;
    }
}
