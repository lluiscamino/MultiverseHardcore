package me.lluiscamino.multiversehardcore.models;

import me.lluiscamino.multiversehardcore.MultiverseHardcore;
import me.lluiscamino.multiversehardcore.utils.WorldUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HardcoreWorldConfiguration {
    private final World world;
    private final World spawnWorld;
    private final Date startDate;
    private final boolean banOps;
    private final boolean banForever;
    private final long banLength;
    private final boolean spectatorMode;
    private final boolean includeNether;
    private final boolean includeEnd;

    public HardcoreWorldConfiguration(World world, World spawnWorld, Date startDate, boolean banOps,
                                      boolean banForever, long banLength, boolean spectatorMode,
                                      boolean includeNether, boolean includeEnd) {
        this.world = world;
        this.spawnWorld = spawnWorld;
        this.startDate = startDate;
        this.banOps = banOps;
        this.banForever = banForever;
        this.banLength = banLength;
        this.spectatorMode = spectatorMode;
        this.includeNether = includeNether;
        this.includeEnd = includeEnd;
    }

    public HardcoreWorldConfiguration(@NotNull String worldName, @NotNull Map<String, Object> worldConfig) throws IllegalArgumentException {
        if (!worldConfigMapIsValid(worldConfig))
            throw new IllegalArgumentException("World config for map " + worldName + " is not valid");
        this.world = MultiverseHardcore.getInstance().getServer().getWorld(worldName);
        this.spawnWorld = MultiverseHardcore.getInstance().getServer().getWorld((String) worldConfig.get("respawn_world"));
        this.startDate = new Date(Long.parseLong((String) worldConfig.get("start_date")));
        this.banOps = Boolean.parseBoolean((String) worldConfig.get("ban_ops"));
        this.banForever = Boolean.parseBoolean((String) worldConfig.get("ban_forever"));
        this.banLength = Long.parseLong((String) worldConfig.get("ban_length"));
        this.spectatorMode = Boolean.parseBoolean((String) worldConfig.get("spectator_mode"));
        this.includeNether = Boolean.parseBoolean((String) worldConfig.get("include_nether"));
        this.includeEnd = Boolean.parseBoolean((String) worldConfig.get("include_end"));
    }

    public World getWorld() {
        return world;
    }

    public World getSpawnWorld() {
        return spawnWorld;
    }

    public Date getStartDate() {
        return startDate;
    }

    public boolean isSpectatorMode() {
        return spectatorMode;
    }

    public boolean hasNether() {
        return includeEnd && WorldUtils.worldExists(world.getName() + "_nether");
    }

    public boolean hasTheEnd() {
        return includeEnd && WorldUtils.worldExists(world.getName() + "_the_end");
    }

    public boolean isIncludeNether() {
        return includeNether;
    }

    public boolean isIncludeEnd() {
        return includeEnd;
    }

    public boolean isBanOps() {
        return banOps;
    }

    public boolean isBanForever() {
        return banForever;
    }

    public long getBanLength() {
        return banLength;
    }

    public List<String> getErrors() {
        List<String> errors = new ArrayList<>();
        if (world == null) {
            errors.add("World does not exist");
        } else if (WorldUtils.worldIsHardcore(world)) {
            errors.add("World is already hardcore");
        } else if (!spectatorMode && world == spawnWorld) {
            errors.add("World and spawn world cannot be equal");
        }
        if (spawnWorld == null && !spectatorMode) {
            errors.add("Respawn world does not exist");
        }
        if (spawnWorld != null && WorldUtils.worldIsHardcore(spawnWorld)) {
            errors.add("Respawn world cannot be hardcore");
        }
        if (!banForever && banLength < 0) {
            errors.add("Ban length cannot be less than 0");
        }
        return errors;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> worldConfig = new HashMap<>();
        worldConfig.put("start_date", Long.toString(startDate.getTime()));
        worldConfig.put("respawn_world", !isSpectatorMode() ? spawnWorld.getName() : "null");
        worldConfig.put("ban_ops", Boolean.toString(banOps));
        worldConfig.put("ban_forever", Boolean.toString(banForever));
        worldConfig.put("ban_length", Long.toString(banLength * 1000));
        worldConfig.put("spectator_mode", Boolean.toString(spectatorMode));
        worldConfig.put("include_nether", Boolean.toString(includeNether));
        worldConfig.put("include_end", Boolean.toString(includeEnd));
        return worldConfig;
    }

    @Override
    public String toString() {
        String result = ChatColor.DARK_BLUE + world.getName() + ChatColor.BLUE + " info:\n" + ChatColor.RESET +
                ChatColor.BOLD + "- Start date: " + ChatColor.RESET + startDate + "\n" +
                ChatColor.BOLD + "- Ban OPs: " + ChatColor.RESET + banOps + "\n" +
                ChatColor.BOLD + "- Ban Duration: " + ChatColor.RESET
                + (banForever ? "FOREVER" : banLength / 1000 + "s") + "\n" +
                ChatColor.BOLD + "- Spectator Mode: " + ChatColor.RESET
                + (spectatorMode ? "Activated" : "Not activated") + "\n" +
                ChatColor.BOLD + "- Include Nether: " + ChatColor.RESET + includeNether + "\n" +
                ChatColor.BOLD + "- Include The End: " + ChatColor.RESET + includeEnd + "\n";
        if (!spectatorMode && spawnWorld != null) {
            result += ChatColor.BOLD + "- Spawn world: " + ChatColor.RESET + spawnWorld.getName();
        }
        return result;
    }

    private boolean worldConfigMapIsValid(@NotNull Map<String, Object> worldConfig) {
        return worldConfig.containsKey("respawn_world") && worldConfig.containsKey("start_date") &&
                worldConfig.containsKey("ban_ops") && worldConfig.containsKey("ban_forever") &&
                worldConfig.containsKey("ban_length") && worldConfig.containsKey("spectator_mode") &&
                worldConfig.containsKey("include_nether") && worldConfig.containsKey("include_end");
    }
}
