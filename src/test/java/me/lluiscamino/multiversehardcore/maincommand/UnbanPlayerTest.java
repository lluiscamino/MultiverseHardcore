package me.lluiscamino.multiversehardcore.maincommand;

import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.lluiscamino.multiversehardcore.utils.TestUtils;
import org.bukkit.ChatColor;
import org.junit.Test;

import java.util.Date;

public class UnbanPlayerTest extends MainCommandTest {
    @Test
    public void playerCannotUnban() {
        String[] args = {"unban"};
        PlayerMock player = server.addPlayer();
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "You need the following permission to run this command: multiversehardcore.unban"
                + ChatColor.RESET;
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void OPHasToSpecifyWorld() {
        String[] args = {"unban"};
        PlayerMock op = TestUtils.addOP(server);
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "Wrong usage: " + ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " unban" +
                ChatColor.RESET + ChatColor.RED + " <world> <player>" + ChatColor.RESET + ChatColor.RESET;
        mainCommand.onCommand(op, command, "", args);
        TestUtils.assertMessage(op, expectedMessage);
    }

    @Test
    public void OPHasToSpecifyPlayer() {
        String[] args = {"unban", "world"};
        PlayerMock op = TestUtils.addOP(server);
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "Wrong usage: " + ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " unban" +
                ChatColor.RESET + ChatColor.RED + " <world> <player>" + ChatColor.RESET + ChatColor.RESET;
        mainCommand.onCommand(op, command, "", args);
        TestUtils.assertMessage(op, expectedMessage);
    }

    @Test
    public void OPCannotUnbanInNonExistentWorld() {
        PlayerMock op = TestUtils.addOP(server);
        String[] args = {"unban", "non_existent_world", op.getName()};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "World does not exist!" + ChatColor.RESET;
        mainCommand.onCommand(op, command, "", args);
        TestUtils.assertMessage(op, expectedMessage);
    }

    @Test
    public void OPCannotUnbanInNonHardcoreWorld() {
        PlayerMock op = TestUtils.addOP(server);
        WorldMock world = mockWorldCreator.createNormalWorld();
        String[] args = {"unban", world.getName(), op.getName()};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "World " + world.getName() + " is not Hardcore" + ChatColor.RESET;
        mainCommand.onCommand(op, command, "", args);
        TestUtils.assertMessage(op, expectedMessage);
    }

    @Test
    public void OPCannotUnbanNonExistentPlayer() {
        PlayerMock op = TestUtils.addOP(server);
        WorldMock world = mockWorldCreator.createNormalWorld();
        String[] args = {"unban", world.getName(), "non_existent_player"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "Player does not exist!" + ChatColor.RESET;
        mainCommand.onCommand(op, command, "", args);
        TestUtils.assertMessage(op, expectedMessage);
    }

    @Test
    public void OPCannotUnbanAlivePlayer() {
        WorldMock world = mockWorldCreator.createHardcoreWorld();
        PlayerMock op = TestUtils.addOP(server);
        String[] args = {"unban", world.getName(), op.getName()};
        String[] expectedMessages = {
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " You are entering a HARDCORE world, be careful!",
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED
                        + "Player is not deathbanned!" + ChatColor.RESET
        };
        mainCommand.onCommand(op, command, "", args);
        TestUtils.assertMessages(op, expectedMessages);
    }

    @Test
    public void OPCannotUnbanPlayerThatHasNotParticipatedInWorld() {
        PlayerMock op = TestUtils.addOP(server);
        WorldMock world = mockWorldCreator.createHardcoreWorld();
        String[] args = {"unban", world.getName(), op.getName()};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "Player " + op.getName() + " has not participated in the world " + world.getName()
                + ChatColor.RESET;
        mainCommand.onCommand(op, command, "", args);
        TestUtils.assertMessage(op, expectedMessage);
    }

    @Test
    public void OPCanUnbanPlayer() {
        PlayerMock op = TestUtils.addOP(server);
        WorldMock world = mockWorldCreator.createHardcoreWorld();
        PlayerMock player = server.addPlayer();
        TestUtils.teleportPlayer(player, world);
        TestUtils.fireJoinEvent(server, player);
        TestUtils.killPlayer(server, player);
        String[] args = {"unban", world.getName(), player.getName()};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.GREEN
                + "Player " + ChatColor.DARK_GREEN + player.getName() + ChatColor.GREEN + " has been unbanned!"
                + ChatColor.RESET;
        mainCommand.onCommand(op, command, "", args);
        op.nextMessage(); // Skip death notification message
        TestUtils.assertMessage(op, expectedMessage);
    }

    @Test
    public void playerDeathBanNumIsUpdatedAfterUnban() {
        PlayerMock op = TestUtils.addOP(server);
        WorldMock world = mockWorldCreator.createHardcoreWorld();
        PlayerMock player = server.addPlayer();
        TestUtils.teleportPlayer(player, world);
        TestUtils.fireJoinEvent(server, player);
        TestUtils.killPlayer(server, player);
        String[] unbanArgs = {"unban", world.getName(), player.getName()};
        String[] getInfoArgs = {"player", world.getName(), player.getName()};
        Date mockJoinDate = new Date();
        String expectedMessage =
                ChatColor.BLUE + player.getName() + ChatColor.RESET + " info:\n" + ChatColor.RESET +
                        ChatColor.BOLD + "- Join Date: " + ChatColor.RESET + mockJoinDate + "\n" +
                        ChatColor.BOLD + "- Death banned: " + ChatColor.RESET + "NO\n" +
                        ChatColor.BOLD + "- Deaths: " + ChatColor.RESET + "0\n";
        mainCommand.onCommand(op, command, "", unbanArgs);
        op.nextMessage(); // Skip death notification message
        mainCommand.onCommand(op, command, "", getInfoArgs);
        op.nextMessage(); // Skip player unbanned message
        TestUtils.assertMessage(op, expectedMessage);
    }

}
