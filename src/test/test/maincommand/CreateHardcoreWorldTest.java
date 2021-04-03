package maincommand;

import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.ChatColor;
import org.junit.Ignore;
import org.junit.Test;
import utils.TestUtils;

public class CreateHardcoreWorldTest extends MainCommandTest {
    @Test
    public void playerCannotCreateHardcoreWorld() {
        String[] args = {"create", "hardcore_world"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "Only OPs can use this command!" + ChatColor.RESET;
        PlayerMock player = server.addPlayer();
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void cannotCreateHardcoreWorldWithNoName() {
        String[] args = {"create"};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "Wrong usage: " + ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " create"
                + ChatColor.RED + " <world>" + ChatColor.GOLD + " <spectator_mode> <create_nether> <create_end> " +
                "<ban_ops> <ban_forever> <ban_length> <include_nether> <include_end> <respawn_world>"
                + ChatColor.RESET + ChatColor.RESET;
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void cannotCreateAlreadyExistingWorld() {
        String worldName = mockWorldCreator.createNormalWorld().getName();
        String[] args = {"create", worldName};
        String expectedMessage = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " "
                + ChatColor.RED + "World " + worldName + " already exists" + ChatColor.RESET;
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void cannotCreateARespawnHardcoreWorldThatEqualsRespawnWorld() {
        String worldName = "hardcore_world";
        String[] args = {"create", worldName, "false", "false", "false", "true", "true", "0", "true", "true", worldName};
        String[] expectedMessages = {
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.BLUE +
                        "Starting creation of world(s)..." + ChatColor.RESET,
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                        "World and spawn world cannot be equal" + ChatColor.RESET
        };
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessages(player, expectedMessages);
    }

    @Test
    public void cannotMakeAnRespawnHardcoreWorldWithNonExistentRespawnWorld() {
        String[] args = {"create", "hardcore_world", "false", "false", "false", "true", "true", "0", "true", "true", "non_existing_world"};
        String[] expectedMessages = {
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.BLUE +
                        "Starting creation of world(s)..." + ChatColor.RESET,
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                        "Respawn world does not exist" + ChatColor.RESET
        };
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessages(player, expectedMessages);
    }

    @Test
    public void cannotCreateAnRespawnHardcoreWorldWithAHardcoreRespawnWorld() {
        WorldMock respawnWorld = mockWorldCreator.createNormalWorld();
        String[] args = {"create", "hardcore_world", "false", "false", "false", "true", "true", "0", "true", "true", respawnWorld.getName()};
        String[] expectedMessages = {
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.BLUE +
                        "Starting creation of world(s)..." + ChatColor.RESET,
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                        "Respawn world cannot be hardcore" + ChatColor.RESET
        };
        PlayerMock player = TestUtils.addOP(server);
        mockWorldCreator.makeWorldHardcore(respawnWorld);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessages(player, expectedMessages);
    }

    @Test
    public void cannotCreateAFiniteBanLengthHardcoreWorldWithANegativeBanLength() {
        String[] args = {"create", "hardcore_world", "true", "false", "false", "true", "false", "-1", "true", "true", ""};
        String[] expectedMessages = {
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.BLUE +
                        "Starting creation of world(s)..." + ChatColor.RESET,
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.RED +
                        "Ban length cannot be less than 0" + ChatColor.RESET
        };
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessages(player, expectedMessages);
    }

    @Test
    public void worldsAreNotCreatedOnError() {
        String[] args = {"create", "hardcore_world", "true", "true", "true", "true", "false", "-1", "true", "true", ""};
        PlayerMock player = TestUtils.addOP(server);
        MVWorldManager worldManager = plugin.getMVWorldManager();
        mainCommand.onCommand(player, command, "", args);
        assert !worldManager.isMVWorld("hardcore_world") &&
                !worldManager.isMVWorld("hardcore_world_nether") &&
                !worldManager.isMVWorld("hardcore_world_the_end");
    }

    @Test
    public void consoleCanCreateSimpleHardcoreWorld() {
        String worldName = "hardcore_world";
        String[] args = {"create", worldName};
        String[] expectedMessages = {
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.BLUE + "Starting creation of world(s)..." + ChatColor.RESET,
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.GREEN + "World " + ChatColor.DARK_GREEN + worldName + ChatColor.GREEN + " created!" + ChatColor.RESET,
        };
        ConsoleCommandSenderMock sender = new ConsoleCommandSenderMock();
        mainCommand.onCommand(sender, command, "", args);
        TestUtils.assertMessages(sender, expectedMessages);
    }

    @Test
    public void OPCanCreateSimpleHardcoreSimpleWorld() {
        String worldName = "hardcore_world";
        String[] args = {"create", worldName};
        String[] expectedMessages = {
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.BLUE + "Starting creation of world(s)..." + ChatColor.RESET,
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.GREEN + "World " + ChatColor.DARK_GREEN + worldName + ChatColor.GREEN + " created!" + ChatColor.RESET,
        };
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessages(player, expectedMessages);
    }

    @Test
    public void OPCanCreateHardcoreWorldWithNetherAndTheEnd() {
        String worldName = "hardcore_world";
        String[] args = {"create", worldName, "true", "true", "true", "true", "true", "0", "true", "true", "null"};
        String[] expectedMessages = {
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.BLUE + "Starting creation of world(s)..." + ChatColor.RESET,
                ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " " + ChatColor.GREEN + "World " + ChatColor.DARK_GREEN + worldName + ChatColor.GREEN + " created!" + ChatColor.RESET,
        };
        PlayerMock player = TestUtils.addOP(server);
        mainCommand.onCommand(player, command, "", args);
        TestUtils.assertMessages(player, expectedMessages);
        assert plugin.getMVWorldManager().isMVWorld(worldName) &&
                plugin.getMVWorldManager().isMVWorld(worldName + "_nether") &&
                plugin.getMVWorldManager().isMVWorld(worldName + "_the_end");
    }
}
