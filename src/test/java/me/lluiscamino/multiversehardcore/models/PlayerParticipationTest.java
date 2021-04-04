package me.lluiscamino.multiversehardcore.models;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.lluiscamino.multiversehardcore.MultiverseHardcore;
import me.lluiscamino.multiversehardcore.exceptions.PlayerNotParticipatedException;
import me.lluiscamino.multiversehardcore.exceptions.PlayerParticipationAlreadyExistsException;
import me.lluiscamino.multiversehardcore.exceptions.WorldIsNotHardcoreException;
import me.lluiscamino.multiversehardcore.utils.MockMVWorldManager;
import me.lluiscamino.multiversehardcore.utils.MockWorldCreator;
import org.bukkit.ChatColor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerParticipationTest {

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
    public void testInstantiateFakePlayerParticipation() throws PlayerNotParticipatedException, WorldIsNotHardcoreException {
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createHardcoreWorld();
        new PlayerParticipation(player, hcWorld);
    }

    @Test(expected = WorldIsNotHardcoreException.class)
    public void testInstantiatePlayerParticipationWithNonHardcoreWorld() throws PlayerNotParticipatedException, WorldIsNotHardcoreException {
        PlayerMock player = server.addPlayer();
        WorldMock world = mockWorldCreator.createNormalWorld();
        new PlayerParticipation(player, world);
    }

    @Test(expected = PlayerParticipationAlreadyExistsException.class)
    public void addRepeatedPlayerParticipation() throws PlayerParticipationAlreadyExistsException {
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createHardcoreWorld();
        PlayerParticipation.addPlayerParticipation(player, hcWorld, new Date());
        PlayerParticipation.addPlayerParticipation(player, hcWorld, new Date());
    }

    @Test
    public void testGetJoinDate() throws PlayerParticipationAlreadyExistsException, PlayerNotParticipatedException, WorldIsNotHardcoreException {
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createHardcoreWorld();
        Date joinDate = new Date();
        PlayerParticipation.addPlayerParticipation(player, hcWorld, joinDate);
        PlayerParticipation playerParticipation = new PlayerParticipation(player, hcWorld);
        assert playerParticipation.getJoinDate().equals(joinDate);
    }

    @Test
    public void testGetNumDeathBans() throws PlayerParticipationAlreadyExistsException, PlayerNotParticipatedException, WorldIsNotHardcoreException {
        final int numDeathBans = ThreadLocalRandom.current().nextInt(0, 101);
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createHardcoreWorld();
        PlayerParticipation.addPlayerParticipation(player, hcWorld, new Date());
        PlayerParticipation playerParticipation = new PlayerParticipation(player, hcWorld);
        for (int i = 0; i < numDeathBans; i++) {
            playerParticipation.addDeathBan(new Date(), "");
        }
        assert playerParticipation.getNumDeathBans() == numDeathBans;
    }

    @Test
    public void testGetDeathBans() throws PlayerParticipationAlreadyExistsException, PlayerNotParticipatedException, WorldIsNotHardcoreException {
        final int numDeathBans = ThreadLocalRandom.current().nextInt(0, 101);
        PlayerMock player = server.addPlayer();
        WorldMock hcWorld = mockWorldCreator.createHardcoreWorld();
        PlayerParticipation.addPlayerParticipation(player, hcWorld, new Date());
        PlayerParticipation playerParticipation = new PlayerParticipation(player, hcWorld);
        for (int i = 0; i < numDeathBans; i++) {
            playerParticipation.addDeathBan(new Date(), "");
        }
        assert playerParticipation.getDeathBans().length == numDeathBans;
    }

    @Test
    public void testToString() throws PlayerParticipationAlreadyExistsException, PlayerNotParticipatedException, WorldIsNotHardcoreException {
        PlayerMock player = server.addPlayer();
        String playerName = player.getDisplayName();
        WorldMock hcWorld = mockWorldCreator.createHardcoreWorld();
        Date joinDate = new Date();
        Date ban1StartDate = new Date();
        Date ban2StartDate = new Date();
        String expectedMessage = ChatColor.BLUE + player.getName() + ChatColor.RESET + " info:\n" + ChatColor.RESET +
                ChatColor.BOLD + "- Join Date: " + ChatColor.RESET + joinDate + "\n" +
                ChatColor.BOLD + "- Death banned: " + ChatColor.RESET + "YES\n" +
                ChatColor.BOLD + "- Deaths: " + ChatColor.RESET + "2\n" +
                "  " + ban1StartDate + " - FOREVER  > " + playerName + " blew up\n" +
                "  " + ban2StartDate + " - FOREVER  > " + playerName + " hit the ground too hard\n";
        PlayerParticipation.addPlayerParticipation(player, hcWorld, new Date());
        PlayerParticipation playerParticipation = new PlayerParticipation(player, hcWorld);
        playerParticipation.addDeathBan(ban1StartDate, playerName + " blew up");
        playerParticipation.addDeathBan(ban2StartDate, playerName + " hit the ground too hard");
        assert playerParticipation.toString().equals(expectedMessage);
    }

}
