package life.lluis.multiversehardcore.utils;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import life.lluis.multiversehardcore.MultiverseHardcore;
import life.lluis.multiversehardcore.exceptions.PlayerNotParticipatedException;
import life.lluis.multiversehardcore.exceptions.PlayerParticipationAlreadyExistsException;
import life.lluis.multiversehardcore.exceptions.WorldIsNotHardcoreException;
import life.lluis.multiversehardcore.models.HardcoreWorld;
import life.lluis.multiversehardcore.models.PlayerParticipation;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.logging.Logger;

public final class WorldUtils {

    private WorldUtils() {
    }

    public static boolean worldExists(@NotNull String worldName) {
        return getServer().getWorld(worldName) != null;
    }

    public static boolean worldIsHardcore(@NotNull World world) {
        try {
            new HardcoreWorld(world.getName());
            return true;
        } catch (WorldIsNotHardcoreException e) {
            return false;
        }
    }

    public static boolean playerIsInWorld(Player player, World world) {
        String worldName = world.getName();
        String playerWorldName = player.getWorld().getName();
        return worldName.equals(playerWorldName);
    }

    public static World getNormalWorld(@NotNull World world) {
        try {
            if (world.getEnvironment() == World.Environment.NORMAL) return world;
            if (hasSuffix(world)) {
                World normalWorld = getServer().getWorld(removeWorldSuffix(world));
                if (normalWorld == null) return world;
                HardcoreWorld hcWorld = new HardcoreWorld(normalWorld.getName());
                boolean includeNetherOrEnd = world.getEnvironment() == World.Environment.NETHER ?
                        hcWorld.getConfiguration().isIncludeNether() : hcWorld.getConfiguration().isIncludeEnd();
                return !includeNetherOrEnd ? world : normalWorld;
            }
            return world;
        } catch (WorldIsNotHardcoreException e) {
            return world;
        }
    }

    public static boolean respawnPlayer(@NotNull Player player) {
        try {
            String worldName = getNormalWorld(player.getWorld()).getName();
            World respawnWorld = new HardcoreWorld(worldName).getConfiguration().getSpawnWorld();
            if (respawnWorld == null) {
                getLogger().warning("Respawn world does not exist!");
                return false;
            }
            return player.teleport(respawnWorld.getSpawnLocation());
        } catch (WorldIsNotHardcoreException e) {
            return false; // This cannot happen. If player is not in a hardcore world, this function will not be called.
        }
    }

    public static void handlePlayerEnterWorld(@NotNull Event event) {
        try {
            Player player = event instanceof PlayerEvent ? ((PlayerEvent) event).getPlayer() : ((PlayerDeathEvent) event).getEntity();
            World world = getNormalWorld(player.getWorld());
            if (!worldIsHardcore(world)) {
                setGameModeBackToDefaultIfNecessary(player, world);
                return;
            }
            addPlayerParticipationIfNotExists(player, world);
            PlayerParticipation participation = new PlayerParticipation(player, world);
            if (participation.isDeathBanned()) {
                sendYouCantPlayMessage(participation);
                preventPlayerEnterWorld(participation);
            } else {
                setGameModeBackToDefaultIfNecessary(player, world);
                sendEnteringWorldMessage(player);
            }
        } catch (PlayerNotParticipatedException | WorldIsNotHardcoreException ignored) {
        } // This cannot happen.
    }

    private static FileConfiguration getConfig() {
        return MultiverseHardcore.getInstance().getConfig();
    }

    private static Logger getLogger() {
        return MultiverseHardcore.getInstance().getLogger();
    }

    private static Server getServer() {
        return MultiverseHardcore.getInstance().getServer();
    }

    private static boolean hasSuffix(@NotNull World world) {
        if (world.getEnvironment() == World.Environment.NORMAL) return false;
        String suffix = getWorldSuffix(world.getEnvironment());
        String worldName = world.getName();
        return worldName.endsWith(suffix);
    }

    private static String removeWorldSuffix(@NotNull World world) {
        if (world.getEnvironment() == World.Environment.NORMAL) return world.getName();
        String suffix = getWorldSuffix(world.getEnvironment());
        String worldName = world.getName();
        return worldName.substring(0, worldName.length() - suffix.length());
    }

    private static String getWorldSuffix(@NotNull World.Environment environment) {
        return environment == World.Environment.NETHER ? "_nether" : "_the_end";
    }

    private static void addPlayerParticipationIfNotExists(@NotNull Player player, @NotNull World world) {
        try {
            PlayerParticipation.addPlayerParticipation(player, world, new Date());
        } catch (PlayerParticipationAlreadyExistsException ignored) {
        }
    }

    private static void sendYouCantPlayMessage(@NotNull PlayerParticipation participation) {
        String message = participation.isBannedForever() ? "You can't play in this world since you died" :
                "You can't play in this world now. You'll be able to join again on " + participation.getUnBanDate();
        MessageSender.sendNormal(participation.getPlayer(), message);
    }

    private static void sendEnteringWorldMessage(@NotNull Player player) {
        MessageSender.sendNormal(player, "You are entering a HARDCORE world, be careful!");
    }

    private static void preventPlayerEnterWorld(@NotNull PlayerParticipation participation) {
        int enterWorldTicks = getConfig().getInt("enter_world_ticks");
        HardcoreWorld hcWorld = participation.getHcWorld();
        Player player = participation.getPlayer();
        MultiverseHardcore plugin = MultiverseHardcore.getInstance();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (hcWorld.getConfiguration().isSpectatorMode()) {
                player.setGameMode(GameMode.SPECTATOR);
            } else {
                respawnPlayer(player);
            }
        }, enterWorldTicks);
    }

    private static GameMode getDefaultGameMode(World world) {
        MVWorldManager worldManager = MultiverseHardcore.getInstance().getMVWorldManager();
        MultiverseWorld multiverseWorld = worldManager.getMVWorld(world);
        return multiverseWorld.getGameMode();
    }

    private static void setGameModeBackToDefaultIfNecessary(Player player, World world) {
        if (player.getGameMode() == GameMode.SPECTATOR) {
            player.setGameMode(getDefaultGameMode(world));
        }
    }
}
