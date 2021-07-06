package me.lluiscamino.multiversehardcore.events;

import me.lluiscamino.multiversehardcore.exceptions.PlayerNotParticipatedException;
import me.lluiscamino.multiversehardcore.exceptions.WorldIsNotHardcoreException;
import me.lluiscamino.multiversehardcore.models.DeathBan;
import me.lluiscamino.multiversehardcore.models.PlayerParticipation;
import me.lluiscamino.multiversehardcore.utils.MessageSender;
import me.lluiscamino.multiversehardcore.utils.WorldUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Date;

public class PlayerDeath implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event) {
        try {
            Player player = event.getEntity();
            World world = getDeathBanWorld(player);
            if (player.hasPermission("multiversehardcore.bypass." + world.getName())) {
                return; // ignore the death if the player has bypass permissions
            }
            PlayerParticipation participation = new PlayerParticipation(player, world);
            participation.addDeathBan(new Date(), event.getDeathMessage());
            sendPlayerDiedMessage(participation);
            player.setHealth(20);
            WorldUtils.handlePlayerEnterWorld(event);
        } catch (PlayerNotParticipatedException | WorldIsNotHardcoreException ignored) {
        }
    }

    private World getDeathBanWorld(Player player) throws WorldIsNotHardcoreException {
        World world = player.getWorld();
        World normalWorld = WorldUtils.getNormalWorld(world);

        if (!WorldUtils.worldIsHardcore(world) && !WorldUtils.worldIsHardcore(normalWorld)) {
            throw new WorldIsNotHardcoreException("");
        } else if (!WorldUtils.worldIsHardcore(world)) {
            world = normalWorld;
        }
        return world;
    }

    private void sendPlayerDiedMessage(PlayerParticipation participation) {
        DeathBan deathBan = participation.getLastDeathBan(); // last death ban
        Player player = participation.getPlayer();
        World world = participation.getWorld();
        String message = deathBan.isForever() ?
                player.getDisplayName() + " died and won't be able to play in the world " +
                        ChatColor.RED + world.getName() + ChatColor.RESET + " again!" :
                player.getDisplayName() + " died and won't be able to play in the world " +
                        ChatColor.RED + world.getName() + ChatColor.RESET + " until " + deathBan.getEndDate();
        MessageSender.broadcast(message);
    }
}
