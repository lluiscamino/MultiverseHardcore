package life.lluis.multiversehardcore.commands.mainsubcommands;

import life.lluis.multiversehardcore.commands.HelpCommand;
import life.lluis.multiversehardcore.commands.MainSubcommand;
import life.lluis.multiversehardcore.exceptions.InvalidCommandInputException;
import life.lluis.multiversehardcore.exceptions.WorldIsNotHardcoreException;
import life.lluis.multiversehardcore.models.HardcoreWorld;
import life.lluis.multiversehardcore.utils.MessageSender;
import life.lluis.multiversehardcore.utils.WorldUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class GetWorldInfoSubcommand extends MainSubcommand {
    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        try {
            initProperties(sender, args);
            checkConsoleHasSpecifiedArgs();
            World world = getCommandWorld();
            checkCanGetWorldInfo(world);
            HardcoreWorld hcWorld = new HardcoreWorld(world.getName());
            sender.sendMessage(hcWorld.toString());
        } catch (InvalidCommandInputException | WorldIsNotHardcoreException e) {
            MessageSender.sendError(sender, e.getMessage());
        }
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

    private void checkCanGetWorldInfo(@NotNull World world)
            throws InvalidCommandInputException {
        if (!sender.isOp() && !WorldUtils.playerIsInWorld((Player) sender, world)) {
            throw new InvalidCommandInputException(getWrongUsageMessage(HelpCommand.WORLD_COMMAND));
        }
    }
}
