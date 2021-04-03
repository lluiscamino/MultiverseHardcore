package maincommand;

import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.ChatColor;
import org.junit.Ignore;
import org.junit.Test;
import utils.TestUtils;

public class MakeWorldHardcoreTest extends MainCommandTest {
    @Test
    public void playerCannotMakeWorldHardcore() {
        String[] args = {"makehc", "hc_world"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "Only OPs can use this command!" + ChatColor.RESET;
        PlayerMock player = server.addPlayer();
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void cannotMakeWorldWithNoNameHardcore() {
        String[] args = {"makehc"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "Wrong usage: " + ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " makehc"
                + ChatColor.RED + " <world>" + ChatColor.GOLD + " <spectator_mode> <ban_ops> <ban_forever> " +
                "<ban_length> <include_nether> <include_end> <respawn_world>" + ChatColor.RESET + ChatColor.RESET;
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void cannotMakeNonExistentWorldHardcore() {
        String[] args = {"makehc", "hc_world"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                "World does not exist" + ChatColor.RESET;
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void cannotMakeAlreadyHardcoreWorldHardcore() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        String worldName = world.getName();
        String[] args = {"makehc", worldName};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                "World is already hardcore" + ChatColor.RESET;
        PlayerMock player = TestUtils.addOP(server);
        mockWorldCreator.makeWorldHardcore(world);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void cannotMakeAnRespawnHardcoreWorldThatEqualsRespawnWorld() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        String worldName = world.getName();
        String[] args = {"makehc", worldName, "false", "true", "true", "0", "true", "true", worldName};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                "World and spawn world cannot be equal" + ChatColor.RESET;
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void cannotMakeAnRespawnHardcoreWorldWithNonExistentRespawnWorld() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        String worldName = world.getName();
        String[] args = {"makehc", worldName, "false", "true", "true", "0", "true", "true", "non_existing_world"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                "Respawn world does not exist" + ChatColor.RESET;
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void cannotMakeAnRespawnHardcoreWorldWithAHardcoreRespawnWorld() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        WorldMock respawnWorld = mockWorldCreator.createNormalWorld();
        String[] args = {"makehc", world.getName(), "false", "true", "true", "0", "true", "true", respawnWorld.getName()};
        String expectedMessage2 = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                "Respawn world cannot be hardcore" + ChatColor.RESET;
        PlayerMock player = TestUtils.addOP(server);
        mockWorldCreator.makeWorldHardcore(respawnWorld);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage2);
    }

    @Test
    public void cannotMakeAFiniteBanLengthHardcoreWorldWithANegativeBanLength() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        String worldName = world.getName();
        String[] args = {"makehc", worldName, "true", "true", "false", "-1", "true", "true", ""};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                "Ban length cannot be less than 0" + ChatColor.RESET;
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void consoleCanMakeWorldHardcore() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        String worldName = world.getName();
        String[] args = {"makehc", worldName};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.GREEN +
                "World " + ChatColor.DARK_GREEN + worldName + ChatColor.GREEN + " is now Hardcore!" + ChatColor.RESET;
        ConsoleCommandSenderMock sender = new ConsoleCommandSenderMock();
        mainCommand.onCommand(sender, command, "", args);
        TestUtils.assertMessage(sender, expectedMessage);
    }

    @Test
    public void OPCanMakeWorldHardcore() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        String worldName = world.getName();
        String[] args = {"makehc", worldName};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.GREEN +
                "World " + ChatColor.DARK_GREEN + worldName + ChatColor.GREEN + " is now Hardcore!" + ChatColor.RESET;
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }
}
