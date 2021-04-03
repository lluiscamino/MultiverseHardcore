package me.lluiscamino.multiversehardcore.maincommand;

import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.ChatColor;
import org.junit.Test;
import me.lluiscamino.multiversehardcore.utils.TestUtils;

import java.util.Date;

public class GetWorldInfoTest extends MainCommandTest {

    @Test
    public void consoleHasToSpecifyWorldToGetPlayerInfo() {
        String[] args = {"world"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "Wrong usage: " + ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " world"
                + ChatColor.RESET + ChatColor.GOLD + " <world>" + ChatColor.RESET + ChatColor.RESET;
        ConsoleCommandSenderMock sender = new ConsoleCommandSenderMock();
        mainCommand.onCommand(sender, command, "", args);
        TestUtils.assertMessage(sender, expectedMessage);
    }

    @Test
    public void worldHasToExistToGetWorldInfo() {
        String[] args = {"world", "non_existing_world"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                "World does not exist!" + ChatColor.RESET;
        PlayerMock player = server.addPlayer();
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void worldHasToBeHardcoreToGetWorldInfo() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        String[] args = {"world", world.getName()};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                "World " + world.getName() + " is not Hardcore" + ChatColor.RESET;
        PlayerMock player = server.addPlayer();
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void playerHasToBeInWorldToGetItsInfo() {
        WorldMock world1 = mockWorldCreator.createNormalWorld();
        WorldMock world2 = mockWorldCreator.createNormalWorld();
        String[] args = {"world", world1.getName()};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "Wrong usage: " + ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " world"
                + ChatColor.RESET + ChatColor.RESET;
        PlayerMock player = server.addPlayer();
        mockWorldCreator.makeWorldHardcore(world1);
        TestUtils.teleportPlayer(player, world2);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void playerCanGetWorldInfo() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        String[] args = {"world", world.getName()};
        Date mockStartDate = new Date();
        String expectedMessage =
                ChatColor.DARK_BLUE + world.getName() + ChatColor.BLUE + " info:\n" + ChatColor.RESET +
                        ChatColor.BOLD + "- Start date: " + ChatColor.RESET + mockStartDate + "\n" +
                        ChatColor.BOLD + "- Ban OPs: " + ChatColor.RESET + "true\n" +
                        ChatColor.BOLD + "- Ban Duration: " + ChatColor.RESET + "FOREVER\n" +
                        ChatColor.BOLD + "- Spectator Mode: " + ChatColor.RESET + "Activated\n" +
                        ChatColor.BOLD + "- Include Nether: " + ChatColor.RESET + "true\n" +
                        ChatColor.BOLD + "- Include The End: " + ChatColor.RESET + "true\n";
        PlayerMock player = server.addPlayer();
        mockWorldCreator.makeWorldHardcore(world);
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
                        ChatColor.BOLD + "- Ban OPs: " + ChatColor.RESET + "true\n" +
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
