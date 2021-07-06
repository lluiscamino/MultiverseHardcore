package me.lluiscamino.multiversehardcore.commands.mainsubcommands;

import me.lluiscamino.multiversehardcore.commands.MainSubcommand;
import me.lluiscamino.multiversehardcore.exceptions.InvalidCommandInputException;
import me.lluiscamino.multiversehardcore.models.HardcoreWorld;
import me.lluiscamino.multiversehardcore.utils.MessageSender;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetWorldsListSubcommand extends MainSubcommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender.hasPermission("multiversehardcore.list")) {
            initProperties(sender, args);
            MessageSender.sendInfo(sender, "Worlds list: ");
            List<HardcoreWorld> hcWorlds = HardcoreWorld.getHardcoreWorlds();
            StringBuilder worldsListMessage = new StringBuilder();
            for (HardcoreWorld hcWorld : hcWorlds) {
                appendWorldInfo(worldsListMessage, hcWorld);
            }
            sender.sendMessage(worldsListMessage.toString());
        } else {
            MessageSender.sendError(sender, MainSubcommand.PERMISSION_ERROR);
        }
        return true;
    }

    private void appendWorldInfo(@NotNull StringBuilder worldsListMessage, @NotNull HardcoreWorld hcWorld) {
        String worldName = hcWorld.getConfiguration().getWorld().getName();
        worldsListMessage.append(worldName);
        worldsListMessage.append("\n");
        if (hcWorld.getConfiguration().hasNether()) {
            worldsListMessage.append("\t").append(worldName).append("_nether\n");
        }
        if (hcWorld.getConfiguration().hasTheEnd()) {
            worldsListMessage.append("\t").append(worldName).append("_the_end\n");
        }
    }
}
