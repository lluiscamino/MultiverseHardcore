package maincommand;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import life.lluis.multiversehardcore.MultiverseHardcore;
import life.lluis.multiversehardcore.commands.MainCommand;
import org.bukkit.command.PluginCommand;
import org.junit.After;
import org.junit.Before;
import utils.MockMVWorldManager;
import utils.MockWorldCreator;

public abstract class MainCommandTest {
    protected final MainCommand mainCommand = new MainCommand();
    protected ServerMock server;
    protected MultiverseHardcore plugin;
    protected MockWorldCreator mockWorldCreator;
    protected PluginCommand command;

    @Before
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(MultiverseHardcore.class);
        MockMVWorldManager worldManager = new MockMVWorldManager(server);
        plugin.setMVWorldManager(worldManager);
        mockWorldCreator = new MockWorldCreator(server, worldManager);
        if (command == null) command = MultiverseHardcore.getInstance().getCommand("mvhc");
        server.getScheduler().performTicks(1L);
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
