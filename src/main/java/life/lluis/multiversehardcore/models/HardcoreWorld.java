package life.lluis.multiversehardcore.models;

import life.lluis.multiversehardcore.exceptions.HardcoreWorldCreationException;
import life.lluis.multiversehardcore.exceptions.WorldIsNotHardcoreException;
import life.lluis.multiversehardcore.files.HardcoreWorldsList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HardcoreWorld {

    private final HardcoreWorldConfiguration configuration;

    public HardcoreWorld(String worldName) throws WorldIsNotHardcoreException {
        configuration = HardcoreWorldsList.instance.getHardcoreWorldConfiguration(worldName);
    }

    public static List<HardcoreWorld> getHardcoreWorlds() {
        String[] worldNames = HardcoreWorldsList.instance.getHardcoreWorldNames();
        List<HardcoreWorld> hcWorlds = new ArrayList<>();
        for (String worldName : worldNames) {
            try {
                hcWorlds.add(new HardcoreWorld(worldName));
            } catch (WorldIsNotHardcoreException ignored) {
            }
        }
        return hcWorlds;
    }

    public static void createHardcoreWorld(@NotNull HardcoreWorldConfiguration configuration)
            throws HardcoreWorldCreationException {
        List<String> errors = configuration.getErrors();
        if (errors.size() > 0) throw new HardcoreWorldCreationException(errors.get(0));
        HardcoreWorldsList.instance.addHardcoreWorld(configuration);
    }

    public HardcoreWorldConfiguration getConfiguration() {
        return configuration;
    }

    public Date getDeathBanEndDate(Date date) {
        if (configuration.isBanForever()) return DeathBan.FOREVER;
        return new Date(date.getTime() + configuration.getBanLength());
    }

    @Override
    public String toString() {
        return configuration.toString();
    }
}
