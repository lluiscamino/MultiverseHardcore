package me.lluiscamino.multiversehardcore.models;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.lluiscamino.multiversehardcore.MultiverseHardcore;
import me.lluiscamino.multiversehardcore.exceptions.PlayerNotParticipatedException;
import me.lluiscamino.multiversehardcore.exceptions.WorldIsNotHardcoreException;
import me.lluiscamino.multiversehardcore.utils.MockMVWorldManager;
import me.lluiscamino.multiversehardcore.utils.MockWorldCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class DeathBanTest {

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

    @Test(expected = PlayerNotParticipatedException.class)
    public void addFakeDeathBan() throws PlayerNotParticipatedException, WorldIsNotHardcoreException {
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createHardcoreWorld();
        DeathBan.addDeathBan(player, hcWorld, new Date(), "Fake death ban");
    }

    @Test(expected = WorldIsNotHardcoreException.class)
    public void addDeathBanOnNonHardcoreWorld() throws PlayerNotParticipatedException, WorldIsNotHardcoreException {
        PlayerMock player = server.addPlayer();
        WorldMock world = mockWorldCreator.createNormalWorld();
        DeathBan.addDeathBan(player, world, new Date(), "Fake death ban");
    }

    @Test
    public void testGetPlayer() {
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        DeathBan deathBan = new DeathBan(player, hcWorld, player.getDisplayName() + " blew up", new Date(), new Date());
        assert deathBan.getPlayer() == player;
    }

    @Test
    public void testGetWorld() {
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        DeathBan deathBan = new DeathBan(player, hcWorld, player.getDisplayName() + " blew up", new Date(), new Date());
        assert deathBan.getWorld() == hcWorld;
    }

    @Test
    public void testGetMessage() {
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        DeathBan deathBan = new DeathBan(player, hcWorld, player.getDisplayName() + " blew up", new Date(), new Date());
        assert deathBan.getMessage().equals(player.getDisplayName() + " blew up");
    }

    @Test
    public void testGetStartDate() {
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        Date startDate = new Date();
        DeathBan deathBan = new DeathBan(player, hcWorld, player.getDisplayName() + " blew up", startDate, new Date());
        assert deathBan.getStartDate().equals(startDate);
    }

    @Test
    public void testGetEndDate() {
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        Date endDate = new Date();
        DeathBan deathBan = new DeathBan(player, hcWorld, player.getDisplayName() + " blew up", new Date(), endDate);
        assert deathBan.getEndDate().equals(endDate);
    }

    @Test
    public void testIsActive() {
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        c.add(Calendar.MONTH, 1);
        Date endDate = c.getTime();
        DeathBan deathBan1 = new DeathBan(player, hcWorld, player.getDisplayName() + " blew up", new Date(), endDate);
        assert deathBan1.isActive();

        c.add(Calendar.MONTH, -2);
        endDate = c.getTime();
        DeathBan deathBan2 = new DeathBan(player, hcWorld, player.getDisplayName() + " blew up", new Date(), endDate);
        assert !deathBan2.isActive();
    }

    @Test
    public void testIsForever() {
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();

        DeathBan deathBan1 = new DeathBan(player, hcWorld, player.getDisplayName() + " blew up", new Date(), DeathBan.FOREVER);
        assert deathBan1.isForever();

        DeathBan deathBan2 = new DeathBan(player, hcWorld, player.getDisplayName() + " blew up", new Date(), new Date());
        assert !deathBan2.isForever();
    }

    @Test
    public void testToString() {
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createNormalWorld();
        Date startDate = new Date();
        Date endDate = new Date();

        String expectedMessage1 = startDate + " - " + endDate + "  > " + player.getDisplayName() + " blew up";
        DeathBan deathBan1 = new DeathBan(player, hcWorld, player.getDisplayName() + " blew up", startDate, endDate);
        assert deathBan1.toString().equals(expectedMessage1);

        String expectedMessage2 = startDate + " - FOREVER  > " + player.getDisplayName() + " blew up";
        DeathBan deathBan2 = new DeathBan(player, hcWorld, player.getDisplayName() + " blew up", startDate, DeathBan.FOREVER);
        assert deathBan2.toString().equals(expectedMessage2);
    }

}
