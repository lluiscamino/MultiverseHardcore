package life.lluis.multiversehardcore.commands;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import life.lluis.multiversehardcore.MultiverseHardcore;
import life.lluis.multiversehardcore.files.HardcoreWorldsList;
import life.lluis.multiversehardcore.files.PlayersList;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    private MultiverseHardcore plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        plugin = MultiverseHardcore.getInstance();

        if (args == null || args.length == 0) {
            HelpCommand.printHelpDialog(sender);
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "create":
            case "createworld":
                createWorld(sender, args);
                return true;
            case "makehc":
            case "makeworldhc":
            case "makeworldhardcore":
                makeWorldHC(sender, args);
                return true;
            case "player":
            case "playerinfo":
            case "seeplayer":
                seePlayerInfo(sender, args);
                return true;
            case "world":
            case "worldinfo":
            case "seeworld":
                seeWorldInfo(sender, args);
                return true;
            case "list":
            case "worlds":
                seeWorldsList(sender, args);
                return true;
            case "version":
                printVersion(sender);
                return true;
            default:
                plugin.sendError(sender, "Invalid command!");
                return true;
        }
    }

    private void createWorld(CommandSender sender, String[] args) {
        if (!plugin.isOp(sender)) {
            plugin.sendError(sender, "Only OPs can use this command!");
            return;
        }
        if (args.length < 2) {
            plugin.sendError(sender, "Wrong usage: ./mvhc create <world> <spectator_mode> <create_nether> " +
                    "<create_end> <ban_ops> <ban_forever> <ban_length> <include_nether> <include_end> <respawn_world>");
            return;
        }
        MultiverseCore multiverse = plugin.getMultiverseCore();
        String worldName = args[1];
        boolean spectatorMode = args.length <= 2 || Boolean.parseBoolean(args[2]);
        boolean createNether = args.length > 3 && Boolean.parseBoolean(args[3]);
        boolean createEnd = args.length > 4 && Boolean.parseBoolean(args[4]);
        boolean banOps = args.length <= 5 || Boolean.parseBoolean(args[5]);
        boolean banForever = args.length <= 6 || Boolean.parseBoolean(args[6]);
        long banLength = args.length > 7 ? Long.parseLong(args[7]) : 30;
        boolean includeNether = args.length <= 8 || Boolean.parseBoolean(args[8]);
        boolean includeEnd = args.length <= 9 || Boolean.parseBoolean(args[9]);
        World respawnWorld = args.length > 10 ? plugin.getServer().getWorld(args[10]) : null;
        if (respawnWorld == null && !spectatorMode) {
            plugin.sendError(sender, "Respawn world does not exist");
            return;
        }
        if (!spectatorMode && plugin.getHardcoreWorldsList().isHardcore(respawnWorld)) {
            plugin.sendError(sender, "Respawn world cannot be hardcore");
            return;
        }
        plugin.sendInfo(sender, "Starting creation of world...");
        try {
            if (!multiverse.getMVWorldManager().addWorld(worldName, World.Environment.NORMAL, "", WorldType.NORMAL, true, "")) {
                plugin.sendError(sender, "World could not be created!");
                return;
            } else {
                MultiverseWorld world = multiverse.getMVWorldManager().getMVWorld(worldName);
                world.setColor("DARKRED");
                world.setDifficulty(Difficulty.HARD);
            }
            if (createNether) {
                if (!multiverse.getMVWorldManager().addWorld(worldName + "_nether", World.Environment.NETHER, "", WorldType.NORMAL, true, "")) {
                    plugin.sendError(sender, "Nether could not be created!");
                    return;
                } else {
                    MultiverseWorld world = multiverse.getMVWorldManager().getMVWorld(worldName + "_nether");
                    world.setColor("DARKRED");
                    world.setDifficulty(Difficulty.HARD);
                }
            }
            if (createEnd) {
                if (!multiverse.getMVWorldManager().addWorld(worldName + "_the_end", World.Environment.THE_END, "", WorldType.NORMAL, true, "")) {
                    plugin.sendError(sender, "The End could not be created!");
                } else {
                    MultiverseWorld world = multiverse.getMVWorldManager().getMVWorld(worldName + "_the_end");
                    world.setColor("DARKRED");
                    world.setDifficulty(Difficulty.HARD);
                }
            }
        } catch (IllegalArgumentException e) {
            plugin.sendError(sender, "World(s) could not be created!");
            return;
        }
        World createdWorld = plugin.getServer().getWorld(worldName);
        plugin.getHardcoreWorldsList().addHardcoreWorld(createdWorld, respawnWorld, banOps, banForever, banLength, spectatorMode,
                includeNether, includeEnd);
        plugin.sendSuccess(sender, "World " + ChatColor.DARK_GREEN + worldName + ChatColor.GREEN + " created!");
    }

    private void makeWorldHC(CommandSender sender, String[] args) {
        if (!plugin.isOp(sender)) {
            plugin.sendError(sender, "Only OPs can use this command!");
            return;
        }
        if (args.length < 2) {
            plugin.sendError(sender, "Wrong usage: ./mvhc create <world> <spectator_mode> <ban_ops> <ban_forever> " +
                    "<ban_length> <include_nether> <include_end> <respawn_world>");
            return;
        }
        MultiverseCore multiverse = plugin.getMultiverseCore();
        World world = plugin.getServer().getWorld(args[1]);
        if (world == null) {
            plugin.sendError(sender, "World does not exist!");
            return;
        }
        if (plugin.getHardcoreWorldsList().isHardcore(world)) {
            plugin.sendError(sender, "World is already hardcore!");
            return;
        }
        boolean spectatorMode = args.length <= 2 || Boolean.parseBoolean(args[2]);
        boolean banOps = args.length <= 3 || Boolean.parseBoolean(args[3]);
        boolean banForever = args.length <= 4 || Boolean.parseBoolean(args[4]);
        long banLength = args.length > 5 ? Long.parseLong(args[5]) : 30;
        boolean includeNether = args.length <= 6 || Boolean.parseBoolean(args[6]);
        boolean includeEnd = args.length <= 7 || Boolean.parseBoolean(args[7]);
        World respawnWorld = args.length > 8 ? plugin.getServer().getWorld(args[8]) : null;
        if (respawnWorld == null && !spectatorMode) {
            plugin.sendError(sender, "Respawn world does not exist");
            return;
        }
        if (!spectatorMode && plugin.getHardcoreWorldsList().isHardcore(respawnWorld)) {
            plugin.sendError(sender, "Respawn world cannot be hardcore");
            return;
        }
        plugin.getHardcoreWorldsList().addHardcoreWorld(world, respawnWorld, banOps, banForever, banLength, spectatorMode,
                includeNether, includeEnd);
        plugin.sendSuccess(sender, "World " + ChatColor.DARK_GREEN + world.getName() + ChatColor.GREEN + " is now hardcore!");
    }

    private void seePlayerInfo(CommandSender sender, String[] args) {
        if (args.length < 2 && !(sender instanceof Player)) {
            plugin.sendError(sender, "Wrong usage: ./mvhc player <world> <player>");
            return;
        }
        World world = args.length > 1 ? plugin.getServer().getWorld(args[1]) : ((Player) sender).getWorld();
        world = plugin.getNormalWorld(world);
        if (world == null) {
            plugin.sendError(sender, "World does not exist!");
            return;
        }
        if (!plugin.getHardcoreWorldsList().isHardcore(world)) {
            plugin.sendError(sender, "World is not Hardcore!");
            return;
        }
        Player player = args.length > 2 ? plugin.getServer().getPlayer(args[2]) : (Player) sender;
        if (player == null) {
            plugin.sendError(sender, "Player does not exist!");
            return;
        }
        if (!plugin.isOp(sender) && (!player.getName().equals(sender.getName()) || !world.getName().equals(((Player) sender).getWorld().getName()))) {
            plugin.sendError(sender, "You don't have permissions to see " + player.getName() + " info!");
            return;
        }
        PlayersList.PlayerInfo playerInfo = plugin.getPlayersList().getPlayerInfo(player, world);
        if (playerInfo == null) {
            plugin.sendError(sender, "No data for player " + player.getName() + " in world " + world.getName());
            return;
        }
        sender.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.RESET + " info:");
        sender.sendMessage(playerInfo.toString());
    }

    private void seeWorldInfo(CommandSender sender, String[] args) {
        World world = args.length > 1 ? plugin.getServer().getWorld(args[1]) : ((Player) sender).getWorld();
        if (world == null) {
            plugin.sendError(sender, "World does not exist!");
            return;
        }
        if (!plugin.isOp(sender) && !world.getName().equals(((Player) sender).getWorld().getName())) {
            plugin.sendError(sender, "Wrong usage: ./mvhc world");
            return;
        }
        HardcoreWorldsList.HardcoreWorldInfo hardcoreWorldInfo = plugin.getHardcoreWorldsList().getHardcoreWorldInfo(world);
        if (hardcoreWorldInfo == null) {
            plugin.sendError(sender, "World is not hardcore!");
            return;
        }
        plugin.sendInfo(sender, ChatColor.DARK_BLUE + world.getName() + ChatColor.BLUE + " info:");
        sender.sendMessage(hardcoreWorldInfo.toString());
    }

    private void seeWorldsList(CommandSender sender, String[] args) {
        if (!plugin.isOp(sender)) {
            plugin.sendError(sender, "Only OPs can use this command!");
            return;
        }
        plugin.sendInfo(sender, "Worlds list: ");
        HardcoreWorldsList.HardcoreWorldInfo[] worlds = plugin.getHardcoreWorldsList().getHardcoreWorldsList();
        for (HardcoreWorldsList.HardcoreWorldInfo world : worlds) {
            if (!world.isDeleted()) {
                sender.sendMessage(world.getWorld().getName());
                if (world.hasNether()) {
                    sender.sendMessage("   " + world.getWorld().getName() + "_nether");
                }
                if (world.hasTheEnd()) {
                    sender.sendMessage("   " + world.getWorld().getName() + "_the_end");
                }
            }
        }
    }

    private void printVersion(CommandSender sender) {
        if (!plugin.isOp(sender)) {
            plugin.sendError(sender, "Only OPs can use this command!");
            return;
        }
        plugin.sendInfo(sender, "Version: " + plugin.getConfig().getString("version"));
    }
}
