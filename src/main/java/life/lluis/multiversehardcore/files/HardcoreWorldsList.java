package life.lluis.multiversehardcore.files;

import life.lluis.multiversehardcore.MultiverseHardcore;
import org.bukkit.World;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HardcoreWorldsList extends life.lluis.multiversehardcore.files.List {

    private static final String FILE_NAME = "worlds.yml";

    public HardcoreWorldsList() {
        super(FILE_NAME);
    }

    private void update(World world, HashMap<String, String> newMap) {
        if (isHardcore(world)) {
            listFile.set(world.getName(), newMap); // Save
        } else {
            listFile.createSection(world.getName(), newMap); // Create
        }
        save();
    }

    public boolean isHardcore(World world) {
        return listFile.isConfigurationSection(world.getName());
    }

    public void cleanWorlds() {
        HardcoreWorldInfo[] hardcoreWorlds = getHardcoreWorldsList();
        for (HardcoreWorldInfo world : hardcoreWorlds) {
            String path = world.getPath();
            if (world.getWorld() == null) {
                deleteHardcoreWorld(path);
                plugin.getPlayersList().deleteWorld(path);
                plugin.getLogger().info("Removed world " + path + " from " + FILE_NAME);
            } else if (!world.isSpectatorMode() && (world.getSpawnWorld() == null || isHardcore(world.getSpawnWorld()))) {
                plugin.getLogger().warning("World " + path + " has an invalid respawn world. It will make death banned players spectators.");
            }
        }
    }

    public HardcoreWorldInfo[] getHardcoreWorldsList() {
        Map<String, Object> worlds = listFile.getValues(false);
        HardcoreWorldInfo[] worldsList = new HardcoreWorldInfo[worlds.size()];
        int index = 0;
        for (String worldName : worlds.keySet()) {
            Map<String, Object> worldInfo = listFile.getConfigurationSection(worldName).getValues(false);
            worldsList[index++] = new HardcoreWorldInfo(
                    worldName,
                    plugin.getServer().getWorld(worldName),
                    plugin.getServer().getWorld((String) worldInfo.get("respawn_world")),
                    new Date(Long.parseLong((String) worldInfo.get("start_date"))),
                    Boolean.parseBoolean((String) worldInfo.get("ban_ops")),
                    Boolean.parseBoolean((String) worldInfo.get("ban_forever")),
                    Long.parseLong((String) worldInfo.get("ban_length")),
                    Boolean.parseBoolean((String) worldInfo.get("spectator_mode")),
                    Boolean.parseBoolean((String) worldInfo.get("include_nether")),
                    Boolean.parseBoolean((String) worldInfo.get("include_end"))
            );
        }
        return worldsList;
    }

    public HardcoreWorldInfo getHardcoreWorldInfo(World world) {
        if (!isHardcore(world)) return null;
        world = plugin.getNormalWorld(world);
        Map<String, Object> worldInfo = listFile.getConfigurationSection(world.getName()).getValues(false);
        if (!worldInfo.containsKey("respawn_world") || !worldInfo.containsKey("start_date") ||
                !worldInfo.containsKey("ban_ops") || !worldInfo.containsKey("ban_forever") ||
                !worldInfo.containsKey("ban_length") || !worldInfo.containsKey("spectator_mode") ||
                !worldInfo.containsKey("include_nether") || !worldInfo.containsKey("include_end")) {
            return null;
        }
        return new HardcoreWorldInfo(
                world.getName(),
                world,
                plugin.getServer().getWorld((String) worldInfo.get("respawn_world")),
                new Date(Long.parseLong((String) worldInfo.get("start_date"))),
                Boolean.parseBoolean((String) worldInfo.get("ban_ops")),
                Boolean.parseBoolean((String) worldInfo.get("ban_forever")),
                Long.parseLong((String) worldInfo.get("ban_length")),
                Boolean.parseBoolean((String) worldInfo.get("spectator_mode")),
                Boolean.parseBoolean((String) worldInfo.get("include_nether")),
                Boolean.parseBoolean((String) worldInfo.get("include_end"))
        );

    }

    public HardcoreWorldInfo addHardcoreWorld(World world, World spawnWorld, boolean banOps, boolean banForever, long banLength,
                                              boolean spectatorMode, boolean nether, boolean end) {
        if (isHardcore(world)) return null;
        Date startDate = new Date();
        HashMap<String, String> worldInfo = new HashMap<>();
        worldInfo.put("start_date", Long.toString(startDate.getTime()));
        worldInfo.put("respawn_world", spawnWorld != null ? spawnWorld.getName() : "null");
        worldInfo.put("ban_ops", Boolean.toString(banOps));
        worldInfo.put("ban_forever", Boolean.toString(banForever));
        worldInfo.put("ban_length", Long.toString(banLength * 1000));
        worldInfo.put("spectator_mode", Boolean.toString(spectatorMode));
        worldInfo.put("include_nether", Boolean.toString(nether));
        worldInfo.put("include_end", Boolean.toString(end));
        update(world, worldInfo);
        return new HardcoreWorldInfo(world.getName(), world, spawnWorld, startDate, banOps, banForever, banLength, spectatorMode, nether, end);
    }

    public void deleteHardcoreWorld(String path) {
        listFile.set(path, null);
        save();
    }

    public static class HardcoreWorldInfo {
        private final String path;
        private final World world;
        private final World spawnWorld;
        private final Date startDate;
        public boolean banOps;
        public boolean banForever;
        public long banLength;
        public boolean spectatorMode;
        public boolean includeNether;
        public boolean includeEnd;

        public HardcoreWorldInfo(String path, World world, World spawnWorld, Date startDate, boolean banOps,
                                 boolean banForever, long banLength, boolean spectatorMode, boolean includeNether,
                                 boolean includeEnd) {
            this.path = path;
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

        public String getPath() {
            return path;
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

        public Date getEndDate(Date date) {
            if (banForever) return PlayersList.DeathBanInfo.FOREVER;
            return new Date(date.getTime() + banLength);
        }

        public boolean isDeleted() {
            return world == null;
        }

        public boolean hasNether() {
            if (isDeleted()) return false;
            return includeEnd && MultiverseHardcore.getInstance().worldExists(world.getName() + "_nether");
        }

        public boolean hasTheEnd() {
            if (isDeleted()) return false;
            return includeEnd && MultiverseHardcore.getInstance().worldExists(world.getName() + "_the_end");
        }

        @Override
        public String toString() {
            String result = "- Start date: " + startDate + "\n" +
                    "- Ban OPs: " + banOps + "\n" +
                    "- Ban Duration: " + (banForever ? "FOREVER" : banLength / 1000 + "s") + "\n" +
                    "- Spectator Mode: " + (spectatorMode ? "Activated" : "Not activated") + "\n" +
                    "- Include Nether: " + includeNether + "\n" +
                    "- Include The End: " + includeEnd + "\n";
            if (!spectatorMode && spawnWorld != null) {
                result += "- Spawn world: " + spawnWorld.getName();
            }
            return result;
        }
    }
}
