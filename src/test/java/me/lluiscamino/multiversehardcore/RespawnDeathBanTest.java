package me.lluiscamino.multiversehardcore;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.lluiscamino.multiversehardcore.exceptions.HardcoreWorldCreationException;
import me.lluiscamino.multiversehardcore.models.HardcoreWorld;
import me.lluiscamino.multiversehardcore.models.HardcoreWorldConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import me.lluiscamino.multiversehardcore.utils.MockMVWorldManager;
import me.lluiscamino.multiversehardcore.utils.MockWorldCreator;
import me.lluiscamino.multiversehardcore.utils.TestUtils;

import java.util.Date;

public class RespawnDeathBanTest {
    private ServerMock server;
    private WorldMock world;
    private MockWorldCreator mockWorldCreator;

    @Before
    public void setUp() {
        server = MockBukkit.mock();
        MultiverseHardcore plugin = MockBukkit.load(MultiverseHardcore.class);
        MockMVWorldManager worldManager = new MockMVWorldManager(server);
        plugin.setMVWorldManager(worldManager);
        mockWorldCreator = new MockWorldCreator(server, worldManager);
        if (world == null) world = mockWorldCreator.createNormalWorld("world");
        server.getScheduler().performTicks(1L);
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void playerIsTeleportedWhenJoiningWorld() throws HardcoreWorldCreationException {
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        HardcoreWorld.createHardcoreWorld(new HardcoreWorldConfiguration(hcWorld, world, new Date(), true, true, 0, false, true, true));
        PlayerMock player = server.addPlayer(); // Join world once is set to hardcore
        TestUtils.teleportPlayer(player, hcWorld);
        TestUtils.fireJoinEvent(server, player);
        TestUtils.killPlayer(server, player);
        TestUtils.teleportPlayer(player, hcWorld);
        TestUtils.fireJoinEvent(server, player);
        WorldMock actualWorld = TestUtils.getPlayerWorld(player);
        TestUtils.assertWorldsAreEqual(actualWorld, world);
    }

    @Test
    public void OPIsNotTeleportedWhenJoiningWorld() throws HardcoreWorldCreationException {
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        WorldMock world = mockWorldCreator.createNormalWorld();
        HardcoreWorld.createHardcoreWorld(new HardcoreWorldConfiguration(hcWorld, world, new Date(), false, true, 0, false, true, true));
        PlayerMock player = TestUtils.addOP(server);
        TestUtils.killPlayer(server, player);
        TestUtils.teleportPlayer(player, hcWorld);
        TestUtils.fireJoinEvent(server, player);
        WorldMock actualWorld = TestUtils.getPlayerWorld(player);
        TestUtils.assertWorldsAreEqual(actualWorld, hcWorld);
    }

    @Test
    public void OPIsTeleportedWhenJoiningWorld() throws HardcoreWorldCreationException {
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        HardcoreWorld.createHardcoreWorld(new HardcoreWorldConfiguration(hcWorld, world, new Date(), true, true, 0, false, true, true));
        PlayerMock player = TestUtils.addOP(server);
        TestUtils.teleportPlayer(player, hcWorld);
        TestUtils.fireJoinEvent(server, player);
        TestUtils.killPlayer(server, player);
        WorldMock actualWorld = TestUtils.getPlayerWorld(player);
        TestUtils.assertWorldsAreEqual(actualWorld, world);
    }

    @Test
    public void playerIsNotTeleportedWhenDeathBanEnds() throws HardcoreWorldCreationException, InterruptedException {
        final int banLengthInSeconds = 1;
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        WorldMock world = mockWorldCreator.createNormalWorld();
        HardcoreWorld.createHardcoreWorld(new HardcoreWorldConfiguration(hcWorld, world, new Date(), true, false, banLengthInSeconds, false, true, true));
        PlayerMock player = server.addPlayer(); // Join world once is set to hardcore
        TestUtils.killPlayer(server, player);
        Thread.sleep(banLengthInSeconds * 1000);
        TestUtils.teleportPlayer(player, hcWorld);
        TestUtils.fireJoinEvent(server, player);
        WorldMock actualWorld = TestUtils.getPlayerWorld(player);
        TestUtils.assertWorldsAreEqual(actualWorld, hcWorld);
    }

    @Test
    public void playerIsNotTeleportedWhenJoiningDifferentWorld() throws HardcoreWorldCreationException {
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        WorldMock world1 = mockWorldCreator.createNormalWorld();
        WorldMock world2 = mockWorldCreator.createNormalWorld();
        HardcoreWorld.createHardcoreWorld(new HardcoreWorldConfiguration(hcWorld, world1, new Date(), true, true, 0, false, true, true));
        PlayerMock player = server.addPlayer(); // Join world once is set to hardcore
        TestUtils.killPlayer(server, player);
        TestUtils.teleportPlayer(player, hcWorld);
        TestUtils.fireJoinEvent(server, player);
        TestUtils.teleportPlayer(player, world2);
        TestUtils.fireJoinEvent(server, player);
        WorldMock actualWorld = TestUtils.getPlayerWorld(player);
        TestUtils.assertWorldsAreEqual(actualWorld, world2);
    }

    @Test
    public void playerIsTeleportedWhenDyingOnTheNether() throws HardcoreWorldCreationException {
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        WorldMock nether = mockWorldCreator.createNetherWorld();
        HardcoreWorld.createHardcoreWorld(new HardcoreWorldConfiguration(hcWorld, world, new Date(), true, true, 0, false, true, true));
        PlayerMock player = server.addPlayer(); // Join world once is set to hardcore
        TestUtils.teleportPlayer(player, nether);
        TestUtils.fireJoinEvent(server, player);
        TestUtils.killPlayer(server, player);
        TestUtils.fireJoinEvent(server, player);
        WorldMock actualWorld = TestUtils.getPlayerWorld(player);
        TestUtils.assertWorldsAreEqual(actualWorld, world);
    }

    @Test
    public void playerIsNotTeleportedWhenDyingOnTheNether() throws HardcoreWorldCreationException {
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        WorldMock nether = mockWorldCreator.createNetherWorld();
        HardcoreWorld.createHardcoreWorld(new HardcoreWorldConfiguration(hcWorld, world, new Date(), true, true, 0, false, false, true));
        PlayerMock player = server.addPlayer(); // Join world once is set to hardcore
        TestUtils.teleportPlayer(player, nether);
        TestUtils.fireJoinEvent(server, player);
        TestUtils.killPlayer(server, player);
        WorldMock actualWorld = TestUtils.getPlayerWorld(player);
        TestUtils.assertWorldsAreEqual(actualWorld, nether);
    }

    @Test
    public void playerIsTeleportedWhenDyingOnTheEnd() throws HardcoreWorldCreationException {
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        WorldMock theEnd = mockWorldCreator.createTheEndWorld();
        HardcoreWorld.createHardcoreWorld(new HardcoreWorldConfiguration(hcWorld, world, new Date(), true, true, 0, false, true, true));
        PlayerMock player = server.addPlayer(); // Join world once is set to hardcore
        TestUtils.teleportPlayer(player, theEnd);
        TestUtils.fireJoinEvent(server, player);
        TestUtils.killPlayer(server, player);
        WorldMock actualWorld = TestUtils.getPlayerWorld(player);
        TestUtils.assertWorldsAreEqual(actualWorld, world);
    }

    @Test
    public void playerIsNotTeleportedWhenDyingOnTheEnd() throws HardcoreWorldCreationException {
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        WorldMock theEnd = mockWorldCreator.createTheEndWorld();
        HardcoreWorld.createHardcoreWorld(new HardcoreWorldConfiguration(hcWorld, world, new Date(), true, true, 0, false, true, false));
        PlayerMock player = server.addPlayer(); // Join world once is set to hardcore
        TestUtils.teleportPlayer(player, theEnd);
        TestUtils.fireJoinEvent(server, player);
        TestUtils.killPlayer(server, player);
        WorldMock actualWorld = TestUtils.getPlayerWorld(player);
        TestUtils.assertWorldsAreEqual(actualWorld, theEnd);
    }
}
