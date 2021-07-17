package me.lluiscamino.multiversehardcore.maincommand;

import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.lluiscamino.multiversehardcore.commands.MainSubcommand;
import org.bukkit.ChatColor;
import org.junit.Test;
import me.lluiscamino.multiversehardcore.utils.TestUtils;

import java.util.Date;

public class GetPlayerParticipationInfoTest extends MainCommandTest {

    @Test
    public void consoleHasToSpecifyWorldToGetPlayerInfo() {
        String[] args = {"player"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "Wrong usage: " + ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " player" +
                ChatColor.RESET + ChatColor.GOLD + " <world> <player>" + ChatColor.RESET + ChatColor.RESET;
        ConsoleCommandSenderMock sender = new ConsoleCommandSenderMock();
        mainCommand.onCommand(sender, command, "", args);
        TestUtils.assertMessage(sender, expectedMessage);
    }

    @Test
    public void consoleHasToSpecifyPlayerToGetPlayerInfo() {
        String[] args = {"player", "world"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "Wrong usage: " + ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " player" +
                ChatColor.RESET + ChatColor.GOLD + " <world> <player>" + ChatColor.RESET + ChatColor.RESET;
        ConsoleCommandSenderMock sender = new ConsoleCommandSenderMock();
        mainCommand.onCommand(sender, command, "", args);
        TestUtils.assertMessage(sender, expectedMessage);
    }

    @Test
    public void worldHasToExistToGetPlayerInfo() {
        String[] args = {"player", "non_existing_world"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                "World does not exist!" + ChatColor.RESET;
        PlayerMock player = server.addPlayer();
        player.setOp(true);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void worldHasToBeHardcoreToGetPlayerInfo() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        String[] args = {"player", world.getName()};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                "World " + world.getName() + " is not Hardcore" + ChatColor.RESET;
        PlayerMock player = server.addPlayer();
        player.setOp(true);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void playerHasToExistToGetPlayerInfo() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        String[] args = {"player", world.getName(), "non_existing_player"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                "Player does not exist!" + ChatColor.RESET;
        PlayerMock op = TestUtils.addOP(server);
        mockWorldCreator.makeWorldHardcore(world);
        mainCommand.onCommand(op, command, "", args);
        mainCommand.onCommand(op, command, "", args);
        TestUtils.assertMessage(op, expectedMessage);
    }

    @Test
    public void playerCannotSeeOtherPlayersInfo() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        PlayerMock player1 = server.addPlayer();
        PlayerMock player2 = server.addPlayer();
        String[] args = {"player", world.getName(), player2.getName()};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                MainSubcommand.PERMISSION_ERROR + ChatColor.RESET;
        mockWorldCreator.makeWorldHardcore(world);
        mainCommand.onCommand(player1, command, "", args);
        TestUtils.assertMessage(player1, expectedMessage);
    }

    @Test
    public void playerCannotSeeOtherWorldsInfo() {
        WorldMock world1 = mockWorldCreator.createNormalWorld();
        WorldMock world2 = mockWorldCreator.createNormalWorld();
        PlayerMock player = server.addPlayer();
        String[] args = {"player", world1.getName(), player.getName()};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                MainSubcommand.PERMISSION_ERROR + ChatColor.RESET;
        mockWorldCreator.makeWorldHardcore(world1);
        TestUtils.teleportPlayer(player, world2);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void playerHasToParticipateInWorldToGetPlayerInfo() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        PlayerMock player = TestUtils.addOP(server);
        String[] args = {"player", world.getName(), player.getName()};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                "Player " + player.getName() + " has not participated in the world " + world.getName() + ChatColor.RESET;
        mockWorldCreator.makeWorldHardcore(world);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void playerCanGetHisInfo() {
        mockWorldCreator.createHardcoreWorld();
        PlayerMock player = server.addPlayer(); // Player will automatically join hardcore world
        player.setOp(true); // testing doesn't allow specific permission setting
        Date mockJoinDate = new Date();
        String[] args = {"player"};
        String[] expectedMessages = {
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " You are entering a HARDCORE world, be careful!",
                ChatColor.BLUE + player.getName() + ChatColor.RESET + " info:\n" + ChatColor.RESET +
                        ChatColor.BOLD + "- Join Date: " + ChatColor.RESET + mockJoinDate + "\n" +
                        ChatColor.BOLD + "- Death banned: " + ChatColor.RESET + "NO\n" +
                        ChatColor.BOLD + "- Deaths: " + ChatColor.RESET + "0\n"
        };
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessages(player, expectedMessages);
    }

    @Test
    public void OPCanGetOtherPlayersInfo() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        PlayerMock player = server.addPlayer();
        PlayerMock op = TestUtils.addOP(server);
        Date mockJoinDate = new Date();
        String[] args = {"player", world.getName(), player.getName()};
        String expectedMessage =
                ChatColor.BLUE + player.getName() + ChatColor.RESET + " info:\n" + ChatColor.RESET +
                        ChatColor.BOLD + "- Join Date: " + ChatColor.RESET + mockJoinDate + "\n" +
                        ChatColor.BOLD + "- Death banned: " + ChatColor.RESET + "NO\n" +
                        ChatColor.BOLD + "- Deaths: " + ChatColor.RESET + "0\n";
        mockWorldCreator.makeWorldHardcore(world);
        TestUtils.teleportPlayer(player, world);
        TestUtils.fireJoinEvent(server, player);
        mainCommand.onCommand(op, command, "", args);
        TestUtils.assertMessage(op, expectedMessage);
    }
}
