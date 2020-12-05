import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import life.lluis.multiversehardcore.MultiverseHardcore;
import life.lluis.multiversehardcore.commands.HelpCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.MockMVWorldManager;
import utils.TestUtils;

public class HelpCommandTest {

    private final HelpCommand helpCommand = new HelpCommand();
    private ServerMock server;
    private PluginCommand command;

    @Before
    public void setUp() {
        server = MockBukkit.mock();
        MultiverseHardcore plugin = MockBukkit.load(MultiverseHardcore.class);
        MockMVWorldManager worldManager = new MockMVWorldManager(server);
        plugin.setMVWorldManager(worldManager);
        if (command == null) command = MultiverseHardcore.getInstance().getCommand("mvhchelp");
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void helpDialogIsCorrect() {
        String expectedMessage =
                ChatColor.BOLD + "Available commands: " + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " player" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " world" + ChatColor.RESET;
        PlayerMock player = server.addPlayer();
        helpCommand.onCommand(player, command, "", new String[]{});
        TestUtils.assertMessage(player, expectedMessage);
    }

    @Test
    public void helpDialogIsCorrectAsOP() {
        String expectedMessage =
                ChatColor.BOLD + "Available commands: " + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " create" + ChatColor.RED
                        + " <world>" + ChatColor.GOLD + " <spectator_mode> <create_nether> <create_end> <ban_ops> <ban_forever> " +
                        "<ban_length> <include_nether> <include_end> <respawn_world>" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " makehc" + ChatColor.RED +
                        " <world>" + ChatColor.GOLD + " <spectator_mode> <ban_ops> <ban_forever> <ban_length> " +
                        "<include_nether> <include_end> <respawn_world>" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " player" + ChatColor.RESET + ChatColor.GOLD + " <world> <player>" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " world" + ChatColor.RESET + ChatColor.GOLD + " <world>" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " worlds" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " unban" + ChatColor.RESET +
                        ChatColor.RED + " <world> <player>" + ChatColor.RESET + "\n" +
                        ChatColor.BLUE + "/mvhc" + ChatColor.GREEN + " version" + ChatColor.RESET + "\n";
        PlayerMock op = TestUtils.addOP(server);
        helpCommand.onCommand(op, command, "", new String[]{});
        TestUtils.assertMessage(op, expectedMessage);
    }

}
