package life.lluis.multiversehardcore.events;

import life.lluis.multiversehardcore.MultiverseHardcore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        MultiverseHardcore.getInstance().handlePlayerEnterWorld(event);
    }
}
