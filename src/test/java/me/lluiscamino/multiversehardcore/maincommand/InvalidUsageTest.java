package me.lluiscamino.multiversehardcore.maincommand;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.ChatColor;
import org.junit.Test;
import me.lluiscamino.multiversehardcore.utils.TestUtils;

public class InvalidUsageTest extends MainCommandTest {
    @Test
    public void getHelpMessageWhenNoArgs() {
        String expectedMessage =
                ChatColor.BOLD + "Available commands: " + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " player" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " world" + ChatColor.RESET;
        PlayerMock player = server.addPlayer();
        mainCommand.onCommand(player, command, "", new String[0]);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void getHelpMessageWhenNoArgsOP() {
        String expectedMessage =
                ChatColor.BOLD + "Available commands: " + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " create" + ChatColor.RED
                        + " <world>" + ChatColor.GOLD + " <spectator_mode> <create_nether> <create_end> <ban_forever> " +
                        "<ban_length> <include_nether> <include_end> <respawn_world>" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " makehc" + ChatColor.RED +
                        " <world>" + ChatColor.GOLD + " <spectator_mode> <ban_forever> <ban_length> " +
                        "<include_nether> <include_end> <respawn_world>" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " player" + ChatColor.RESET + ChatColor.GOLD + " <world> <player>" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " world" + ChatColor.RESET + ChatColor.GOLD + " <world>" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " worlds" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " unban" + ChatColor.RESET +
                        ChatColor.RED + " <world> <player>" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " version" + ChatColor.RESET + "\n";
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", new String[0]);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void getErrorMessageWhenInvalidSubCommand() {
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE] " + ChatColor.RESET + ChatColor.RED + "Invalid subcommand <invalid-subcommand>!" + ChatColor.RESET;
        PlayerMock player = server.addPlayer();
        mainCommand.onCommand(player, command, "", new String[]{"invalid-subcommand"});
        TestUtils.assertMessage(player, expectedMessage);
    }
}
