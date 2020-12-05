package life.lluis.multiversehardcore.commands.mainsubcommands;

import life.lluis.multiversehardcore.commands.HelpCommand;
import life.lluis.multiversehardcore.commands.MainSubcommand;
import life.lluis.multiversehardcore.exceptions.InvalidCommandInputException;
import life.lluis.multiversehardcore.exceptions.PlayerNotParticipatedException;
import life.lluis.multiversehardcore.exceptions.WorldIsNotHardcoreException;
import life.lluis.multiversehardcore.models.PlayerParticipation;
import life.lluis.multiversehardcore.utils.MessageSender;
import life.lluis.multiversehardcore.utils.WorldUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class GetPlayerParticipationInfoSubcommand extends MainSubcommand {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
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
        if (!sender.isOp()
                && (!commandPlayerEqualsSender(player) || !WorldUtils.playerIsInWorld((Player) sender, world))) {
            throw new InvalidCommandInputException("You don't have permissions to see " + player.getName() + " info!");
        }
    }

    private boolean commandPlayerEqualsSender(@NotNull Player player) {
        return player.getName().equals(sender.getName());
    }
}
