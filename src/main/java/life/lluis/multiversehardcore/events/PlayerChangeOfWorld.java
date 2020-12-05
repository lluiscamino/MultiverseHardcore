package life.lluis.multiversehardcore.events;

import life.lluis.multiversehardcore.utils.WorldUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangeOfWorld implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        WorldUtils.handlePlayerEnterWorld(event);
    }
}
