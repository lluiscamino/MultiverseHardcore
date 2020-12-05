package life.lluis.multiversehardcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements CommandExecutor {

    public static final String CREATE_COMMAND = ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " create" + ChatColor.RED
            + " <world>" + ChatColor.GOLD + " <spectator_mode> <create_nether> <create_end> <ban_ops> <ban_forever> " +
            "<ban_length> <include_nether> <include_end> <respawn_world>" + ChatColor.RESET;

    public static final String MAKE_COMMAND = ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " makehc" + ChatColor.RED +
            " <world>" + ChatColor.GOLD + " <spectator_mode> <ban_ops> <ban_forever> <ban_length> " +
            "<include_nether> <include_end> <respawn_world>" + ChatColor.RESET;

    public static final String PLAYER_COMMAND = ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " player" + ChatColor.RESET;

    public static final String PLAYER_COMMAND_OP = PLAYER_COMMAND + ChatColor.GOLD + " <world> <player>" + ChatColor.RESET;

    public static final String WORLD_COMMAND = ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " world" + ChatColor.RESET;

    public static final String WORLD_COMMAND_OP = WORLD_COMMAND + ChatColor.GOLD + " <world>" + ChatColor.RESET;

    public static final String WORLDS_COMMAND = ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " worlds" + ChatColor.RESET;

    public static final String UNBAN_COMMAND = ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " unban" + ChatColor.RESET +
            ChatColor.RED + " <world> <player>" + ChatColor.RESET;

    public static final String VERSION_COMMAND = ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " version" + ChatColor.RESET;


    public static void printHelpDialog(@NotNull CommandSender sender) {
        StringBuilder message = new StringBuilder();
        message.append(ChatColor.BOLD).append("Available commands: ").append(ChatColor.RESET).append("\n");
        if (sender.isOp()) {
            message.append(CREATE_COMMAND).append("\n");
            message.append(MAKE_COMMAND).append("\n");
            message.append(PLAYER_COMMAND_OP).append("\n");
            message.append(WORLD_COMMAND_OP).append("\n");
            message.append(WORLDS_COMMAND).append("\n");
            message.append(UNBAN_COMMAND).append("\n");
            message.append(VERSION_COMMAND).append("\n");
        } else {
            message.append(PLAYER_COMMAND).append("\n");
            message.append(WORLD_COMMAND);
        }
        sender.sendMessage(message.toString());
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        printHelpDialog(sender);
        return true;
    }
}
