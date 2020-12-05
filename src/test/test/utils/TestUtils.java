package utils;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public final class TestUtils {

    private TestUtils() {
    }

    public static void assertMessage(@NotNull ConsoleCommandSenderMock consoleCommandSender, @NotNull String expectedMessage) {
        assert consoleCommandSender.nextMessage().equals(expectedMessage);
    }

    public static void assertMessage(@NotNull PlayerMock player, @NotNull String expectedMessage) {
        assert player.nextMessage().equals(expectedMessage);
    }

    public static void assertMessages(@NotNull ConsoleCommandSenderMock consoleCommandSender, @NotNull String[] expectedMessages) {
        String message = consoleCommandSender.nextMessage();
        int i = 0;
        while (message != null && i < expectedMessages.length) {
            assert message.equals(expectedMessages[i++]);
            message = consoleCommandSender.nextMessage();
        }
        assert message == null && i == expectedMessages.length;
    }

    public static void assertMessages(@NotNull PlayerMock player, @NotNull String[] expectedMessages) {
        String message = player.nextMessage();
        int i = 0;
        while (message != null && i < expectedMessages.length) {
            assert message.equals(expectedMessages[i++]);
            message = player.nextMessage();
        }
        assert message == null && i == expectedMessages.length;
    }

    public static void assertWorldsAreEqual(@NotNull WorldMock world1, @NotNull WorldMock world2) {
        assert world1.getName().equals(world2.getName());
    }

    public static PlayerMock addOP(ServerMock server) {
        PlayerMock player = server.addPlayer();
        player.setOp(true);
        return player;
    }

    public static void killPlayer(@NotNull ServerMock server, @NotNull PlayerMock player) {
        player.setHealth(0);
        server.getScheduler().performTicks(2L);
    }

    public static void teleportPlayer(@NotNull PlayerMock player, @NotNull WorldMock world) {
        player.setLocation(world.getSpawnLocation());
    }

    public static void fireJoinEvent(@NotNull ServerMock server, @NotNull PlayerMock player) {
        server.getPluginManager().callEvent(new PlayerJoinEvent(player, "Someone joined"));
        server.getScheduler().performTicks(2L);
    }

    public static WorldMock getPlayerWorld(@NotNull PlayerMock player) {
        return (WorldMock) player.getLocation().getWorld();
    }

}
