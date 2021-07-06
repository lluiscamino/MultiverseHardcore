package me.lluiscamino.multiversehardcore.models;

import me.lluiscamino.multiversehardcore.exceptions.PlayerNotParticipatedException;
import me.lluiscamino.multiversehardcore.exceptions.PlayerParticipationAlreadyExistsException;
import me.lluiscamino.multiversehardcore.exceptions.WorldIsNotHardcoreException;
import me.lluiscamino.multiversehardcore.files.PlayersList;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class PlayerParticipation {
    private final Player player;
    private final World world;
    private final HardcoreWorld hcWorld;
    private final Date joinDate;
    private DeathBan[] deathBans;

    public PlayerParticipation(@NotNull Player player, @NotNull World world)
            throws PlayerNotParticipatedException, WorldIsNotHardcoreException {
        this.player = player;
        this.world = world;
        this.hcWorld = new HardcoreWorld(world.getName());
        this.joinDate = PlayersList.instance.getPlayerJoinDate(player, world);
        this.deathBans = PlayersList.instance.getPlayerDeathBans(player, world);
    }

    public static void addPlayerParticipation(@NotNull Player player, @NotNull World world, @NotNull Date joinDate)
            throws PlayerParticipationAlreadyExistsException {
        PlayersList.instance.addPlayerParticipation(player, world, joinDate);
    }

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public HardcoreWorld getHcWorld() {
        return hcWorld;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public int getNumDeathBans() {
        return deathBans.length;
    }

    public DeathBan[] getDeathBans() {
        return deathBans;
    }

    public Date getUnBanDate() {
        if (!isDeathBanned()) return null;
        return deathBans[deathBans.length - 1].getEndDate();
    }

    public boolean isDeathBanned() {
        if (deathBans.length == 0 || isImmuneToDeathBan()) return false;
        return deathBans[deathBans.length - 1].isActive();
    }

    public boolean isBannedForever() {
        if (deathBans.length == 0 || isImmuneToDeathBan()) return false;
        return deathBans[deathBans.length - 1].isForever();
    }

    public DeathBan getLastDeathBan() {
        return deathBans[deathBans.length - 1];
    }

    public void unban() {
        while (isDeathBanned()) {
            deleteLastDeathBan();
        }
    }

    public void addDeathBan(Date startDate, String message) {
        try {
            DeathBan.addDeathBan(player, world, startDate, message);
            reloadDeathBans();
        } catch (PlayerNotParticipatedException | WorldIsNotHardcoreException ignored) {
        }
    }

    private boolean isImmuneToDeathBan() {
        return player.hasPermission("multiverse.bypass." + hcWorld.getConfiguration().getWorld().getName());
    }

    private void reloadDeathBans() {
        try {
            deathBans = PlayersList.instance.getPlayerDeathBans(player, world);
        } catch (PlayerNotParticipatedException ignored) {
        }
    }

    private void deleteLastDeathBan() {
        try {
            int lastDeathBanIndex = deathBans.length - 1;
            PlayersList.instance.deleteDeathBan(player, world, lastDeathBanIndex);
            reloadDeathBans();
        } catch (PlayerNotParticipatedException ignored) {
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(
                ChatColor.BLUE + player.getName() + ChatColor.RESET + " info:\n" + ChatColor.RESET +
                        ChatColor.BOLD + "- Join Date: " + ChatColor.RESET + joinDate + "\n" +
                        ChatColor.BOLD + "- Death banned: " + ChatColor.RESET + (isBannedForever() ? "YES" : "NO") + "\n" +
                        ChatColor.BOLD + "- Deaths: " + ChatColor.RESET + deathBans.length + "\n");
        for (DeathBan death : deathBans) {
            str.append("  ");
            str.append(death.toString());
            str.append("\n");
        }
        return str.toString();
    }
}
