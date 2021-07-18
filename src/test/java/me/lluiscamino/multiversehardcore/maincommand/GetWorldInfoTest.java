package me.lluiscamino.multiversehardcore.maincommand;

import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.lluiscamino.multiversehardcore.utils.TestUtils;
import org.bukkit.ChatColor;
import org.junit.Test;

import java.util.Date;

public class GetWorldInfoTest extends MainCommandTest {

    @Test
    public void consoleHasToSpecifyWorldToGetPlayerInfo() {
        String[] args = {"world"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE] " + ChatColor.RESET
                + ChatColor.RED + "Wrong usage: " + ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " world"
                + ChatColor.RESET + ChatColor.GOLD + " <world>" + ChatColor.RESET + ChatColor.RESET;
        ConsoleCommandSenderMock sender = new ConsoleCommandSenderMock();
        mainCommand.onCommand(sender, command, "", args);
        TestUtils.assertMessage(sender, expectedMessage);
    }

    @Test
    public void worldHasToExistToGetWorldInfo() {
        String[] args = {"world", "non_existing_world"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE] " + ChatColor.RESET + ChatColor.RED +
                "World does not exist!" + ChatColor.RESET;
        PlayerMock player = server.addPlayer();
        player.setOp(true);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void worldHasToBeHardcoreToGetWorldInfo() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        String[] args = {"world", world.getName()};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE] " + ChatColor.RESET + ChatColor.RED +
                "World " + world.getName() + " is not Hardcore" + ChatColor.RESET;
        PlayerMock player = server.addPlayer();
        player.setOp(true);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void OPCanGetAnyWorldInfo() {
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        WorldMock normalWorld = mockWorldCreator.createNormalWorld();
        String[] args = {"world", hcWorld.getName()};
        Date mockStartDate = new Date();
        String expectedMessage =
                ChatColor.DARK_BLUE + hcWorld.getName() + ChatColor.BLUE + " info:\n" + ChatColor.RESET +
                        ChatColor.BOLD + "- Start date: " + ChatColor.RESET + mockStartDate + "\n" +
                        ChatColor.BOLD + "- Ban Duration: " + ChatColor.RESET + "FOREVER\n" +
                        ChatColor.BOLD + "- Spectator Mode: " + ChatColor.RESET + "Activated\n" +
                        ChatColor.BOLD + "- Include Nether: " + ChatColor.RESET + "true\n" +
                        ChatColor.BOLD + "- Include The End: " + ChatColor.RESET + "true\n";
        PlayerMock op = TestUtils.addOP(server);
        mockWorldCreator.makeWorldHardcore(hcWorld);
        TestUtils.teleportPlayer(op, normalWorld);
        mainCommand.onCommand(op, command, "", args);
        TestUtils.assertMessage(op, expectedMessage);
    }
}
