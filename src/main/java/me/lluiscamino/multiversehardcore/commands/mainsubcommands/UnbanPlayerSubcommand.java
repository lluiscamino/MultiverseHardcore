package me.lluiscamino.multiversehardcore.commands.mainsubcommands;

import me.lluiscamino.multiversehardcore.commands.HelpCommand;
import me.lluiscamino.multiversehardcore.commands.MainSubcommand;
import me.lluiscamino.multiversehardcore.exceptions.InvalidCommandInputException;
import me.lluiscamino.multiversehardcore.exceptions.PlayerNotParticipatedException;
import me.lluiscamino.multiversehardcore.exceptions.WorldIsNotHardcoreException;
import me.lluiscamino.multiversehardcore.models.PlayerParticipation;
import me.lluiscamino.multiversehardcore.utils.MessageSender;
import me.lluiscamino.multiversehardcore.utils.WorldUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class UnbanPlayerSubcommand extends MainSubcommand {

    private Player player;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender.hasPermission("multiversehardcore.unban")) {
            try {
                initProperties(sender, args);
                checkSenderHasSpecifiedArgs();
                attemptPlayerUnban();
                sendSuccessMessage();
            } catch (InvalidCommandInputException | PlayerNotParticipatedException | WorldIsNotHardcoreException e) {
                MessageSender.sendError(sender, e.getMessage());
            }
        } else {
            MessageSender.sendError(sender, MainSubcommand.PERMISSION_ERROR);
        }
        return true;
    }

    private void checkSenderHasSpecifiedArgs() throws InvalidCommandInputException {
        if (args.length < 3) {
            throw new InvalidCommandInputException(getWrongUsageMessage(HelpCommand.UNBAN_COMMAND));
        }
    }

    private void attemptPlayerUnban()
            throws InvalidCommandInputException, PlayerNotParticipatedException, WorldIsNotHardcoreException {
        World world = getCommandWorld();
        player = getCommandPlayer();
        PlayerParticipation participation = new PlayerParticipation(player, world);
        checkPlayerIsDeathBanned(participation);
        participation.unban();
        setPlayerGameModeToSurvivalIfNeeded(world);
    }

    protected World getCommandWorld() throws InvalidCommandInputException {
        World world = plugin.getServer().getWorld(args[1]);
        checkWorldExists(world);
        world = WorldUtils.getNormalWorld(world);
        return world;
    }

    private Player getCommandPlayer() throws InvalidCommandInputException {
        Player player = plugin.getServer().getPlayer(args[2]);
        checkPlayerExists(player);
        return player;
    }

    private void checkPlayerIsDeathBanned(@NotNull PlayerParticipation participation) throws InvalidCommandInputException {
        if (!participation.isDeathBanned()) {
            throw new InvalidCommandInputException("Player is not deathbanned!");
        }
    }

    private void sendSuccessMessage() {
        MessageSender.sendSuccess(sender, "Player " + ChatColor.DARK_GREEN + player.getName() + ChatColor.GREEN + " has been unbanned!");
    }

    private void setPlayerGameModeToSurvivalIfNeeded(@NotNull World world) {
        World normalWorld = WorldUtils.getNormalWorld(player.getWorld());
        if (player.isOnline() && normalWorld.equals(world)) {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}
