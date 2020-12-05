package life.lluis.multiversehardcore.events;

import life.lluis.multiversehardcore.utils.WorldUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        WorldUtils.handlePlayerEnterWorld(event);
    }
}
