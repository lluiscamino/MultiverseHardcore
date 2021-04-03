package me.lluiscamino.multiversehardcore.models;

import me.lluiscamino.multiversehardcore.exceptions.PlayerNotParticipatedException;
import me.lluiscamino.multiversehardcore.exceptions.WorldIsNotHardcoreException;
import me.lluiscamino.multiversehardcore.files.PlayersList;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class DeathBan {

    public static final Date FOREVER = new Date(Long.MAX_VALUE);

    private final Player player;
    private final World world;
    private final String message;
    private final Date startDate;
    private final Date endDate;

    public DeathBan(Player player, World world, String message, Date startDate, Date endDate) {
        this.player = player;
        this.world = world;
        this.message = message;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static void addDeathBan(@NotNull Player player, @NotNull World world, @NotNull Date startDate, @NotNull String message)
            throws PlayerNotParticipatedException, WorldIsNotHardcoreException {
        HardcoreWorld hcWorld = new HardcoreWorld(world.getName());
        PlayersList.instance.addDeathBan(player, world, startDate, hcWorld.getDeathBanEndDate(startDate), message);
    }

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public String getMessage() {
        return message;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        return endDate.compareTo(new Date()) > 0;
    }

    public boolean isForever() {
        return endDate.compareTo(FOREVER) == 0;
    }

    @Override
    public String toString() {
        return startDate + " - " + (isForever() ? "FOREVER" : endDate) + "  > " + message;
    }
}
