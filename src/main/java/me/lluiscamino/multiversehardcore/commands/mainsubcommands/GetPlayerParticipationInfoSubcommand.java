package me.lluiscamino.multiversehardcore.commands.mainsubcommands;

import me.lluiscamino.multiversehardcore.commands.HelpCommand;
import me.lluiscamino.multiversehardcore.commands.MainSubcommand;
import me.lluiscamino.multiversehardcore.exceptions.InvalidCommandInputException;
import me.lluiscamino.multiversehardcore.exceptions.PlayerNotParticipatedException;
import me.lluiscamino.multiversehardcore.exceptions.WorldIsNotHardcoreException;
import me.lluiscamino.multiversehardcore.models.PlayerParticipation;
import me.lluiscamino.multiversehardcore.utils.MessageSender;
import me.lluiscamino.multiversehardcore.utils.WorldUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class GetPlayerParticipationInfoSubcommand extends MainSubcommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        try {
            initProperties(sender, args);
            checkConsoleHasSpecifiedArgs();
            World world = getCommandWorld();
            Player player = getCommandPlayer(world);
            PlayerParticipation participation = new PlayerParticipation(player, world);
            sender.sendMessage(participation.toString());
        } catch (PlayerNotParticipatedException | InvalidCommandInputException | WorldIsNotHardcoreException e) {
            MessageSender.sendError(sender, e.getMessage());
        }
        return true;
    }

    private void checkConsoleHasSpecifiedArgs() throws InvalidCommandInputException {
        if (args.length < 3 && !(sender instanceof Player)) {
            throw new InvalidCommandInputException(getWrongUsageMessage(HelpCommand.PLAYER_COMMAND_OP));
        }
    }

    protected World getCommandWorld() throws InvalidCommandInputException {
        World world = args.length > 1 ? plugin.getServer().getWorld(args[1]) : ((Player) sender).getWorld();
        checkWorldExists(world);
        world = WorldUtils.getNormalWorld(world);
        return world;
    }

    private Player getCommandPlayer(@NotNull World world) throws InvalidCommandInputException {
        Player player = args.length > 2 ? plugin.getServer().getPlayer(args[2]) : (Player) sender;
        checkPlayerExists(player);
        checkCanGetPlayerInfo(player, world);
        return player;
    }

    private void checkCanGetPlayerInfo(@NotNull Player player, @NotNull World world)
            throws InvalidCommandInputException {
        if ((!sender.hasPermission("multiversehardcore.player.self") || !commandPlayerEqualsSender(player))
                && !sender.hasPermission("multiversehardcore.player.others")) {
            throw new InvalidCommandInputException(MainSubcommand.PERMISSION_ERROR);
        }
    }

    private boolean commandPlayerEqualsSender(@NotNull Player player) {
        return player.getName().equals(sender.getName());
    }
}
