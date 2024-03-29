package me.lluiscamino.multiversehardcore.commands.mainsubcommands;

import me.lluiscamino.multiversehardcore.commands.HelpCommand;
import me.lluiscamino.multiversehardcore.commands.MainSubcommand;
import me.lluiscamino.multiversehardcore.exceptions.InvalidCommandInputException;
import me.lluiscamino.multiversehardcore.exceptions.WorldIsNotHardcoreException;
import me.lluiscamino.multiversehardcore.models.HardcoreWorld;
import me.lluiscamino.multiversehardcore.utils.MessageSender;
import me.lluiscamino.multiversehardcore.utils.WorldUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class GetWorldInfoSubcommand extends MainSubcommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        try {
            initProperties(sender, args);
            checkSenderHasPermission();
            checkConsoleHasSpecifiedArgs();
            World world = getCommandWorld();
            HardcoreWorld hcWorld = new HardcoreWorld(world.getName());
            sender.sendMessage(hcWorld.toString());
        } catch (InvalidCommandInputException | WorldIsNotHardcoreException e) {
            MessageSender.sendError(sender, e.getMessage());
        }
        return true;
    }

    @Override
    protected String getRequiredPermission() {
        return "multiversehardcore.world";
    }

    private void checkConsoleHasSpecifiedArgs() throws InvalidCommandInputException {
        if (args.length < 2 && !(sender instanceof Player)) {
            throw new InvalidCommandInputException(getWrongUsageMessage(HelpCommand.WORLD_COMMAND_OP));
        }
    }

    protected World getCommandWorld() throws InvalidCommandInputException {
        World world = args.length > 1 ? plugin.getServer().getWorld(args[1]) : ((Player) sender).getWorld();
        checkWorldExists(world);
        world = WorldUtils.getNormalWorld(world);
        return world;
    }
}
