package life.lluis.multiversehardcore.files;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PlayersList extends life.lluis.multiversehardcore.files.List {

    private static final String FILE_NAME = "players.yml";

    public PlayersList() {
        super(FILE_NAME);
    }

    private void update(World world, ArrayList<ArrayList<String>> newList) {
        listFile.set(world.getName(), newList);
        save();
    }

    private void updatePlayer(World world, ArrayList<String> player, int index) {
        ArrayList<ArrayList<String>> players = players(world);
        if (index < 0 || index >= players.size()) return;
        players.set(index, player);
        update(world, players);
    }

    private ArrayList<ArrayList<String>> players(World world) {
        ArrayList<ArrayList<String>> players = (ArrayList<ArrayList<String>>) listFile.getList(world.getName());
        if (players == null) players = new ArrayList<>();
        return players;
    }

    private boolean playerExists(Player player, World world) {
        ArrayList<ArrayList<String>> players = players(world);
        String playerUID = player.getUniqueId().toString();
        for (List<String> pl : players) {
            if (pl.get(0).equals(playerUID)) return true;
        }
        return false;
    }

    private int getPlayer(Player player, World world, ArrayList<String> playerInfo) {
        ArrayList<ArrayList<String>> players = players(world);
        String playerUID = player.getUniqueId().toString();
        int index = 0;
        for (ArrayList<String> pl : players) {
            if (pl.get(0).equals(playerUID)) {
                playerInfo.addAll(pl);
                return index;
            }
            index++;
        }
        return -1;
    }

    private boolean validDeathBan(Player player, World world) {
        HardcoreWorldsList worlds = plugin.getHardcoreWorldsList();
        HardcoreWorldsList.HardcoreWorldInfo worldInfo = worlds.getHardcoreWorldInfo(world);
        return worlds.isHardcore(world) && (!player.isOp() || worldInfo.banOps);
    }

    public PlayerInfo getPlayerInfo(Player player, World world) {
        world = plugin.getNormalWorld(world);
        ArrayList<String> playerInfo = new ArrayList<>();
        int index = getPlayer(player, world, playerInfo);
        if (index == -1) return null;
        int numDeaths = Integer.parseInt(playerInfo.get(3));
        DeathBanInfo[] deaths = new DeathBanInfo[numDeaths];
        for (int j = 0, i = 4; i < 4 + numDeaths * 3; j++, i += 3) {
            Date startDate = new Date(Long.parseLong(playerInfo.get(i + 1)));
            Date endDate = new Date(Long.parseLong(playerInfo.get(i + 2)));
            deaths[j] = new DeathBanInfo(player, world, playerInfo.get(i), startDate, endDate);
        }
        Date joinDate = new Date(Long.parseLong(playerInfo.get(2)));
        return new PlayerInfo(player, world, joinDate, deaths);
    }

    public void createPlayer(Player player, World world) {
        world = plugin.getNormalWorld(world);
        if (playerExists(player, world)) return;
        ArrayList<ArrayList<String>> players = players(world);
        ArrayList<String> pl = new ArrayList<>(Arrays.asList(player.getUniqueId().toString(), player.getName(),
                Long.toString(new Date().getTime()), "0"));
        players.add(pl);
        update(world, players);
    }

    public void deleteWorld(String path) {
        listFile.set(path, null);
        save();
    }

    public DeathBanInfo addDeathBan(Player player, World world, String message) {
        world = plugin.getNormalWorld(world);
        if (!validDeathBan(player, world)) return null;
        ArrayList<String> pl = new ArrayList<>();
        int index = getPlayer(player, world, pl);
        if (index == -1) return null;
        long startDate = new Date().getTime();
        long endDate = plugin.getHardcoreWorldsList().getHardcoreWorldInfo(world).getEndDate(new Date(startDate)).getTime();
        pl.set(3, Integer.toString(Integer.parseInt(pl.get(3)) + 1));
        pl.add(message);
        pl.add(Long.toString(startDate));
        pl.add(Long.toString(endDate));
        updatePlayer(world, pl, index);
        return new DeathBanInfo(player, world, message, new Date(startDate), new Date(endDate));
    }

    public static class PlayerInfo {
        private final Player player;
        private final World world;
        private final Date joinDate;
        private final DeathBanInfo[] deaths;

        public PlayerInfo(Player player, World world, Date joinDate, DeathBanInfo[] deaths) {
            this.player = player;
            this.world = world;
            this.joinDate = joinDate;
            this.deaths = deaths;
        }

        public Player getPlayer() {
            return player;
        }

        public World getWorld() {
            return world;
        }

        public Date getJoinDate() {
            return joinDate;
        }

        public int getNumDeaths() {
            return deaths.length;
        }

        public DeathBanInfo[] getDeaths() {
            return deaths;
        }

        public Date getUnBanDate() {
            if (deaths.length == 0) return null;
            return deaths[deaths.length - 1].endDate;
        }

        public boolean isDeathBanned() {
            if (deaths.length == 0) return false;
            return deaths[deaths.length - 1].isActive();
        }

        public boolean isBannedForever() {
            if (deaths.length == 0) return false;
            return deaths[deaths.length - 1].isForever();
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder("- Join Date: " + joinDate + "\n" +
                    "- Death banned: " + (isBannedForever() ? "YES" : "NO") + "\n" +
                    "- Deaths: " + deaths.length + "\n");
            for (DeathBanInfo death : deaths) {
                str.append("  ");
                str.append(death.toString());
                str.append("\n");
            }
            return str.toString();
        }
    }

    public static class DeathBanInfo {

        public static final Date FOREVER = new Date(Long.MAX_VALUE);

        private final Player player;
        private final World world;
        private final String message;
        private final Date startDate;
        private final Date endDate;

        public DeathBanInfo(Player player, World world, String message, Date startDate, Date endDate) {
            this.player = player;
            this.world = world;
            this.message = message;
            this.startDate = startDate;
            this.endDate = endDate;
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
            return startDate + " - " + (isForever() ? "FOREVER" : endDate);
        }
    }
}
