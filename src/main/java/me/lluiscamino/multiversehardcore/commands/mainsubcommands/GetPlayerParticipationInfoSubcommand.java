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

    private Player player;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        try {
            initProperties(sender, args);
            checkConsoleHasSpecifiedArgs();
            World world = getCommandWorld();
            player = getCommandPlayer();
            checkPlayerExists(player);
            checkSenderHasPermission();
            PlayerParticipation participation = new PlayerParticipation(player, world);
            sender.sendMessage(participation.toString());
        } catch (PlayerNotParticipatedException | InvalidCommandInputException | WorldIsNotHardcoreException e) {
            MessageSender.sendError(sender, e.getMessage());
        }
        return true;
    }

    @Override
    protected String getRequiredPermission() {
        return commandPlayerEqualsSender() ? "multiversehardcore.player.self" : "multiversehardcore.player.others";
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

    private Player getCommandPlayer() throws InvalidCommandInputException {
        return args.length > 2 ? plugin.getServer().getPlayer(args[2]) : (Player) sender;
    }

    private boolean commandPlayerEqualsSender() {
        return player.getName().equals(sender.getName());
    }
}
