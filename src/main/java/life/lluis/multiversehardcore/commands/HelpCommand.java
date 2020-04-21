package life.lluis.multiversehardcore.commands;

import life.lluis.multiversehardcore.MultiverseHardcore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements CommandExecutor {

    public static void printHelpDialog(@NotNull CommandSender sender) {
        MultiverseHardcore plugin = MultiverseHardcore.getInstance();
        sender.sendMessage("Available commands: ");
        if (plugin.isOp(sender)) {
            sender.sendMessage(ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " create" + ChatColor.RED + " <world>" +
                    ChatColor.GOLD + " <spectator_mode> <create_nether> <create_end> <ban_ops> <ban_forever> <ban_length> " +
                    "<include_nether> <include_end> <respawn_world>");
            sender.sendMessage(ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " makehc" + ChatColor.RED + " <world>" +
                    ChatColor.GOLD + " <spectator_mode> <ban_ops> <ban_forever> <ban_length> " +
                    "<include_nether> <include_end> <respawn_world>");
        }
        if (plugin.isOp(sender)) {
            sender.sendMessage(ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " player" + ChatColor.GOLD + " <world> " +
                    "<player>");
        } else {
            sender.sendMessage(ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " player");
        }
        if (plugin.isOp(sender)) {
            sender.sendMessage(ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " world" + ChatColor.RED + " <world>");
        } else {
            sender.sendMessage(ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " world");
        }
        if (plugin.isOp(sender)) {
            sender.sendMessage(ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " worlds");
            sender.sendMessage(ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " version");
        }
        sender.sendMessage(ChatColor.RESET + "");
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        printHelpDialog(sender);
        return true;
    }
}
