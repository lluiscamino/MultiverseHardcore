package life.lluis.multiversehardcore.events;

import life.lluis.multiversehardcore.MultiverseHardcore;
import life.lluis.multiversehardcore.files.PlayersList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        MultiverseHardcore plugin = MultiverseHardcore.getInstance();
        World world = player.getWorld();

        PlayersList.DeathBanInfo deathBanInfo = plugin.getPlayersList().addDeathBan(player, world, event.getDeathMessage());
        if (deathBanInfo == null) return;
        if (deathBanInfo.isForever()) {
            Bukkit.broadcastMessage(plugin.getPrefix() + player.getDisplayName() + " died and won't be able to join the world " + ChatColor.RED + world.getName() + ChatColor.RESET + " again!");
        } else {
            Bukkit.broadcastMessage(plugin.getPrefix() + player.getDisplayName() + " died and won't be able to join the world " + ChatColor.RED + world.getName() + ChatColor.RESET + " until " + deathBanInfo.getEndDate());
        }
    }

}
