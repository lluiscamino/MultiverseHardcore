package life.lluis.multiversehardcore;

import com.onarandombox.MultiverseCore.MultiverseCore;
import life.lluis.multiversehardcore.commands.HelpCommand;
import life.lluis.multiversehardcore.commands.MainCommand;
import life.lluis.multiversehardcore.events.PlayerChangeOfWorld;
import life.lluis.multiversehardcore.events.PlayerDeath;
import life.lluis.multiversehardcore.events.PlayerJoin;
import life.lluis.multiversehardcore.events.PlayerRespawn;
import life.lluis.multiversehardcore.files.HardcoreWorldsList;
import life.lluis.multiversehardcore.files.PlayersList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MultiverseHardcore extends JavaPlugin {

    private static MultiverseHardcore instance;
    private PlayersList playersList;
    private HardcoreWorldsList hardcoreWorldsList;

    public static MultiverseHardcore getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        playersList = new PlayersList();
        hardcoreWorldsList = new HardcoreWorldsList();
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new PlayerChangeOfWorld(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawn(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

        getCommand("mvhc").setExecutor(new MainCommand());
        getCommand("mvhchelp").setExecutor(new HelpCommand());

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                hardcoreWorldsList.cleanWorlds();
            }
        }, 1);
    }

    public boolean spawnPlayer(Player player) {
        World spawnWorld = getHardcoreWorldsList().getHardcoreWorldInfo(player.getWorld()).getSpawnWorld();
        if (spawnWorld == null) {
            getLogger().warning("Respawn world does not exist!");
            return false;
        }
        return player.teleport(spawnWorld.getSpawnLocation());
    }

    public PlayersList getPlayersList() {
        return playersList;
    }

    public HardcoreWorldsList getHardcoreWorldsList() {
        return hardcoreWorldsList;
    }

    public MultiverseCore getMultiverseCore() {
        getServer().getPluginManager().getPlugins().toString();
        Plugin plugin = getServer().getPluginManager().getPlugin("Multiverse-Core");

        if (plugin instanceof MultiverseCore) {
            return (MultiverseCore) plugin;
        }

        throw new RuntimeException("Multiverse-Core not found!");
    }

    public String getPrefix() {
        return ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " ";
    }

    public void sendError(CommandSender sender, String error) {
        sender.sendMessage(getPrefix() + ChatColor.RED + error + ChatColor.RESET);
    }

    public void sendNormal(CommandSender sender, String error) {
        sender.sendMessage(getPrefix() + error);
    }

    public void sendInfo(CommandSender sender, String error) {
        sender.sendMessage(getPrefix() + ChatColor.BLUE + error + ChatColor.RESET);
    }

    public void sendSuccess(CommandSender sender, String error) {
        sender.sendMessage(getPrefix() + ChatColor.GREEN + error + ChatColor.RESET);
    }

    public boolean worldExists(String worldName) {
        return getServer().getWorld(worldName) != null;
    }

    public World getNormalWorld(World world) {
        if (world == null) return null;
        if (world.getEnvironment() == World.Environment.NORMAL) return world;

        if (world.getEnvironment() == World.Environment.NETHER) {
            String worldName = world.getName();
            if (worldName.endsWith("_nether")) {
                worldName = worldName.substring(0, worldName.length() - "_nether".length());
                World normalWorld = getServer().getWorld(worldName);
                HardcoreWorldsList.HardcoreWorldInfo worldInfo = hardcoreWorldsList.getHardcoreWorldInfo(normalWorld);
                return normalWorld == null || !worldInfo.includeNether ? world : normalWorld;
            }
            return world;
        }
        if (world.getEnvironment() == World.Environment.THE_END) {
            String worldName = world.getName();
            if (worldName.endsWith("_the_end")) {
                worldName = worldName.substring(0, worldName.length() - "_the_end".length());
                World normalWorld = getServer().getWorld(worldName);
                HardcoreWorldsList.HardcoreWorldInfo worldInfo = hardcoreWorldsList.getHardcoreWorldInfo(normalWorld);
                return normalWorld == null || !worldInfo.includeNether ? world : normalWorld;
            }
            return world;
        }
        return world;
    }

    public void handlePlayerEnterWorld(PlayerEvent event) {
        final Player player = event.getPlayer();
        World world = getNormalWorld(player.getWorld());

        if (getHardcoreWorldsList().isHardcore(world)) {
            getPlayersList().createPlayer(player, world);
        } else {
            return;
        }
        HardcoreWorldsList.HardcoreWorldInfo worldInfo = getHardcoreWorldsList().getHardcoreWorldInfo(world);
        PlayersList.PlayerInfo playerInfo = getPlayersList().getPlayerInfo(player, world);
        if (playerInfo.isDeathBanned() && (!player.isOp() || worldInfo.banOps)) {
            if (playerInfo.isBannedForever()) {
                player.sendMessage(getPrefix() + "You can't play in this world since you died");
            } else {
                player.sendMessage(getPrefix() + "You can't play in this world now. You'll be able to join again" +
                        " on " + playerInfo.getUnBanDate());
            }

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
                    if (worldInfo.spectatorMode) {
                        player.setGameMode(GameMode.SPECTATOR);
                    } else {
                        if (!spawnPlayer(player)) {
                            player.setGameMode(GameMode.SPECTATOR);
                        }
                    }
                }
            }, 2);
        } else {
            player.sendMessage(getPrefix() + "You are entering a HARDCORE world, be careful!");
        }
    }

    public boolean isOp(CommandSender sender) {
        return !(sender instanceof Player) || sender.isOp();
    }
}
