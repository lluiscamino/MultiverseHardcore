package me.lluiscamino.multiversehardcore;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.lluiscamino.multiversehardcore.exceptions.HardcoreWorldCreationException;
import me.lluiscamino.multiversehardcore.models.HardcoreWorld;
import me.lluiscamino.multiversehardcore.models.HardcoreWorldConfiguration;
import me.lluiscamino.multiversehardcore.utils.MockMVWorldManager;
import me.lluiscamino.multiversehardcore.utils.MockWorldCreator;
import me.lluiscamino.multiversehardcore.utils.TestUtils;
import me.lluiscamino.multiversehardcore.utils.WorldUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class WorldUtilsTest {

    private ServerMock server;
    private MockWorldCreator mockWorldCreator;

    @Before
    public void setUp() {
        server = MockBukkit.mock();
        MultiverseHardcore plugin = MockBukkit.load(MultiverseHardcore.class);
        MockMVWorldManager worldManager = new MockMVWorldManager(server);
        mockWorldCreator = new MockWorldCreator(server, worldManager);
        plugin.setMVWorldManager(worldManager);
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testWorldExists() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        assert WorldUtils.worldExists(world.getName());
        assert !WorldUtils.worldExists("non_existent_world");
    }

    @Test
    public void testWorldIsHardcore() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        assert !WorldUtils.worldIsHardcore(world);
        mockWorldCreator.makeWorldHardcore(world);
        assert WorldUtils.worldIsHardcore(world);
    }

    @Test
    public void testPlayerIsInWorld() {
        WorldMock world1 = mockWorldCreator.createNormalWorld();
        WorldMock world2 = mockWorldCreator.createNormalWorld();
        PlayerMock player = server.addPlayer();
        TestUtils.teleportPlayer(player, world1);
        assert WorldUtils.playerIsInWorld(player, world1);
        assert !WorldUtils.playerIsInWorld(player, world2);
        TestUtils.teleportPlayer(player, world2);
        assert WorldUtils.playerIsInWorld(player, world2);
        assert !WorldUtils.playerIsInWorld(player, world1);
    }

    @Test
    public void testGetNormalWorldOnHardcoreWorldWithNetherAndTheEnd() {
        WorldMock world = mockWorldCreator.createHardcoreWorld();
        assert WorldUtils.getNormalWorld(world) == world;
        WorldMock nether = mockWorldCreator.createNetherWorld();
        assert WorldUtils.getNormalWorld(nether) == world;
        WorldMock theEnd = mockWorldCreator.createTheEndWorld();
        assert WorldUtils.getNormalWorld(theEnd) == world;
    }

    @Test
    public void testGetNormalWorldIOnHardcoreWorldWithNoNetherOrTheEnd() throws HardcoreWorldCreationException {
        WorldMock world = mockWorldCreator.createNormalWorld();
        HardcoreWorldConfiguration hcWorldConfig =
                new HardcoreWorldConfiguration(
                        world,
                        null,
                        new Date(),
                        true,
                        true,
                        0,
                        true,
                        false,
                        false
                );
        HardcoreWorld.createHardcoreWorld(hcWorldConfig);
        assert WorldUtils.getNormalWorld(world) == world;
        WorldMock nether = mockWorldCreator.createNetherWorld();
        assert WorldUtils.getNormalWorld(nether) == nether;
        WorldMock theEnd = mockWorldCreator.createTheEndWorld();
        assert WorldUtils.getNormalWorld(theEnd) == theEnd;
    }

    @Test
    public void testGetNormalWorldOnNonHardcoreWorld() {
        mockWorldCreator.createNormalWorld();
        WorldMock nether = mockWorldCreator.createNetherWorld();
        assert WorldUtils.getNormalWorld(nether) == nether;
    }

    @Test
    public void testRespawnPlayerOnHardcoreWorldWithRespawn() throws HardcoreWorldCreationException {
        WorldMock world = mockWorldCreator.createNormalWorld();
        WorldMock respawnWorld = mockWorldCreator.createNormalWorld();
        HardcoreWorldConfiguration hcWorldConfig =
                new HardcoreWorldConfiguration(
                        world,
                        respawnWorld,
                        new Date(),
                        true,
                        true,
                        200,
                        false,
                        true,
                        true
                );
        HardcoreWorld.createHardcoreWorld(hcWorldConfig);
        PlayerMock player = server.addPlayer();
        TestUtils.teleportPlayer(player, world);
        assert WorldUtils.respawnPlayer(player);
        assert player.getWorld() == respawnWorld;
    }

    @Test
    public void testRespawnPlayerOnHardcoreWorldWithNoRespawn() throws HardcoreWorldCreationException {
        WorldMock world = mockWorldCreator.createNormalWorld();
        HardcoreWorldConfiguration hcWorldConfig =
                new HardcoreWorldConfiguration(
                        world,
                        null,
                        new Date(),
                        true,
                        true,
                        200,
                        true,
                        true,
                        true
                );
        HardcoreWorld.createHardcoreWorld(hcWorldConfig);
        PlayerMock player = server.addPlayer();
        TestUtils.teleportPlayer(player, world);
        assert !WorldUtils.respawnPlayer(player);
        assert player.getWorld() == world;
    }

    @Test
    public void testRespawnPlayerOnNonHardcoreWorld() {
        WorldMock world = mockWorldCreator.createNormalWorld();
        PlayerMock player = server.addPlayer();
        TestUtils.teleportPlayer(player, world);
        assert !WorldUtils.respawnPlayer(player);
        assert player.getWorld() == world;
    }

}
