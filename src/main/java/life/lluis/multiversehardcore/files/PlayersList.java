package life.lluis.multiversehardcore.files;

import life.lluis.multiversehardcore.exceptions.PlayerNotParticipatedException;
import life.lluis.multiversehardcore.exceptions.PlayerParticipationAlreadyExistsException;
import life.lluis.multiversehardcore.models.DeathBan;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PlayersList extends life.lluis.multiversehardcore.files.List {

    public static final PlayersList instance = new PlayersList();
    private static final String FILE_NAME = "players.yml";

    private static final int DEATH_BANS_COUNT_INDEX = 3;
    private static final int FIRST_DEATH_BAN_INDEX = 4;

    private static final int DEATH_BAN_LINES_LENGTH = 3;

    public PlayersList() {
        super(FILE_NAME);
    }

    public DeathBan[] getPlayerDeathBans(@NotNull Player player, @NotNull World world)
            throws PlayerNotParticipatedException {
        List<String> participation = getPlayerParticipation(player, world);
        int numDeathBans = Integer.parseInt(participation.get(DEATH_BANS_COUNT_INDEX));
        int lastIndex = FIRST_DEATH_BAN_INDEX + numDeathBans * DEATH_BAN_LINES_LENGTH;
        DeathBan[] deathBans = new DeathBan[numDeathBans];
        for (int j = 0, i = FIRST_DEATH_BAN_INDEX; i < lastIndex; j++, i += DEATH_BAN_LINES_LENGTH) {
            String message = participation.get(i);
            Date startDate = new Date(Long.parseLong(participation.get(i + 1)));
            Date endDate = new Date(Long.parseLong(participation.get(i + 2)));
            deathBans[j] = new DeathBan(player, world, message, startDate, endDate);
        }
        return deathBans;
    }

    public Date getPlayerJoinDate(@NotNull Player player, @NotNull World world) throws PlayerNotParticipatedException {
        List<String> participation = getPlayerParticipation(player, world);
        return new Date(Long.parseLong(participation.get(2)));
    }

    public void addPlayerParticipation(@NotNull Player player, @NotNull World world, @NotNull Date joinDate)
            throws PlayerParticipationAlreadyExistsException {
        if (playerParticipationExists(player, world))
            throw new PlayerParticipationAlreadyExistsException("Player already exists in world");
        List<List<String>> participations = getPlayerParticipations(world);
        List<String> pl = new ArrayList<>(Arrays.asList(player.getUniqueId().toString(), player.getName(),
                Long.toString(joinDate.getTime()), "0"));
        participations.add(pl);
        update(world, participations);
    }

    public void addDeathBan(@NotNull Player player, @NotNull World world, @NotNull Date startDate,
                            @NotNull Date endDate, @NotNull String message) throws PlayerNotParticipatedException {
        List<String> participation = getPlayerParticipation(player, world);
        int numDeathBans = Integer.parseInt(participation.get(DEATH_BANS_COUNT_INDEX));
        participation.set(DEATH_BANS_COUNT_INDEX, Integer.toString(numDeathBans + 1));
        participation.add(message);
        participation.add(Long.toString(startDate.getTime()));
        participation.add(Long.toString(endDate.getTime()));
        updatePlayerParticipation(player, world, participation);
    }

    // numDeathBan: 0-base index
    public void deleteDeathBan(@NotNull Player player, @NotNull World world, int numDeathBan) throws PlayerNotParticipatedException {
        List<String> participation = getPlayerParticipation(player, world);
        int numDeathBans = Integer.parseInt(participation.get(DEATH_BANS_COUNT_INDEX));
        int startingIndex = FIRST_DEATH_BAN_INDEX + numDeathBan * DEATH_BAN_LINES_LENGTH;
        participation.set(DEATH_BANS_COUNT_INDEX, Integer.toString(numDeathBans - 1));
        for (int i = 0; i < 3; i++) {
            participation.remove(startingIndex);
        }
        updatePlayerParticipation(player, world, participation);
    }

    public void deleteWorldDeathBans(@NotNull String worldName) {
        listFile.set(worldName, null);
        save();
    }

    private void update(@NotNull World world, @NotNull List<List<String>> participations) {
        listFile.set(world.getName(), participations);
        save();
    }

    private void updatePlayerParticipation(@NotNull Player player, @NotNull World world,
                                           @NotNull List<String> participation) throws PlayerNotParticipatedException {
        List<List<String>> players = getPlayerParticipations(world);
        int index = getPlayerParticipationIndex(player, world);
        players.set(index, participation);
        update(world, players);
    }

    private List<List<String>> getPlayerParticipations(@NotNull World world) {
        List<List<String>> participations = (List<List<String>>) listFile.getList(world.getName());
        if (participations == null) participations = new ArrayList<>();
        return participations;
    }

    private List<String> getPlayerParticipation(@NotNull Player player, @NotNull World world)
            throws PlayerNotParticipatedException {
        return getPlayerParticipations(world).get(getPlayerParticipationIndex(player, world));
    }

    private boolean playerParticipationExists(@NotNull Player player, @NotNull World world) {
        try {
            getPlayerParticipationIndex(player, world);
            return true;
        } catch (PlayerNotParticipatedException e) {
            return false;
        }
    }

    private int getPlayerParticipationIndex(@NotNull Player player, @NotNull World world)
            throws PlayerNotParticipatedException {
        List<List<String>> players = getPlayerParticipations(world);
        String playerUID = player.getUniqueId().toString();
        int index = 0;
        for (List<String> pl : players) {
            if (pl.get(0).equals(playerUID)) return index;
            index++;
        }
        throw new PlayerNotParticipatedException(
                "Player " + player.getName() + " has not participated in the world " + world.getName());
    }
}
