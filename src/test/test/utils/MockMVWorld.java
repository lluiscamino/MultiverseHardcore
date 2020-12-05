package utils;

import be.seeseemelk.mockbukkit.WorldMock;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.enums.AllowedPortalType;
import com.onarandombox.MultiverseCore.exceptions.PropertyDoesNotExistException;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.*;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MockMVWorld implements MultiverseWorld {

    private final WorldMock world;
    private GameMode gameMode;
    private Difficulty difficulty;

    public MockMVWorld(WorldMock world) {
        this.world = world;
        this.gameMode = GameMode.SURVIVAL;
        this.difficulty = Difficulty.NORMAL;
    }

    @Override
    public World getCBWorld() {
        throw new NotImplementedException();
    }

    @Override
    public String getName() {
        return world.getName();
    }

    @Override
    public WorldType getWorldType() {
        return world.getWorldType();
    }

    @Override
    public World.Environment getEnvironment() {
        return world.getEnvironment();
    }

    @Override
    public void setEnvironment(World.Environment environment) {
        world.setEnvironment(environment);
    }

    @Override
    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public boolean setDifficulty(String s) {
        switch (s.toLowerCase()) {
            case "peaceful":
                return setDifficulty(Difficulty.PEACEFUL);
            case "easy":
                return setDifficulty(Difficulty.EASY);
            case "normal":
                return setDifficulty(Difficulty.NORMAL);
            case "hard":
                return setDifficulty(Difficulty.HARD);
        }
        return false;
    }

    @Override
    public boolean setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return true;
    }

    @Override
    public long getSeed() {
        throw new NotImplementedException();
    }

    @Override
    public void setSeed(long l) {
        throw new NotImplementedException();
    }

    @Override
    public String getGenerator() {
        throw new NotImplementedException();
    }

    @Override
    public void setGenerator(String s) {
        throw new NotImplementedException();
    }

    @Override
    public String getPropertyHelp(String s) throws PropertyDoesNotExistException {
        throw new NotImplementedException();
    }

    @Override
    public String getPropertyValue(String s) throws PropertyDoesNotExistException {
        throw new NotImplementedException();
    }

    @Override
    public boolean setPropertyValue(String s, String s1) throws PropertyDoesNotExistException {
        throw new NotImplementedException();
    }

    @Override
    public boolean addToVariable(String s, String s1) {
        throw new NotImplementedException();
    }

    @Override
    public boolean removeFromVariable(String s, String s1) {
        throw new NotImplementedException();
    }

    @Override
    public boolean clearVariable(String s) {
        throw new NotImplementedException();
    }

    @Override
    public boolean clearList(String s) {
        throw new NotImplementedException();
    }

    @Override
    public String getPermissibleName() {
        throw new NotImplementedException();
    }

    @Override
    public Permission getAccessPermission() {
        throw new NotImplementedException();
    }

    @Override
    public Permission getExemptPermission() {
        throw new NotImplementedException();
    }

    @Override
    public String getAlias() {
        throw new NotImplementedException();
    }

    @Override
    public void setAlias(String s) {
        throw new NotImplementedException();
    }

    @Override
    public ChatColor getColor() {
        throw new NotImplementedException();
    }

    @Override
    public boolean setColor(String s) {
        return true;
    }

    @Override
    public ChatColor getStyle() {
        throw new NotImplementedException();
    }

    @Override
    public boolean setStyle(String s) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isValidAliasColor(String s) {
        throw new NotImplementedException();
    }

    @Override
    public String getColoredWorldString() {
        throw new NotImplementedException();
    }

    @Override
    public boolean canAnimalsSpawn() {
        throw new NotImplementedException();
    }

    @Override
    public void setAllowAnimalSpawn(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public List<String> getAnimalList() {
        throw new NotImplementedException();
    }

    @Override
    public boolean canMonstersSpawn() {
        throw new NotImplementedException();
    }

    @Override
    public void setAllowMonsterSpawn(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public List<String> getMonsterList() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isPVPEnabled() {
        throw new NotImplementedException();
    }

    @Override
    public void setPVPMode(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public boolean getFakePVP() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isHidden() {
        throw new NotImplementedException();
    }

    @Override
    public void setHidden(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isWeatherEnabled() {
        throw new NotImplementedException();
    }

    @Override
    public void setEnableWeather(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isKeepingSpawnInMemory() {
        throw new NotImplementedException();
    }

    @Override
    public void setKeepSpawnInMemory(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public Location getSpawnLocation() {
        throw new NotImplementedException();
    }

    @Override
    public void setSpawnLocation(Location location) {
        throw new NotImplementedException();
    }

    @Override
    public boolean getHunger() {
        throw new NotImplementedException();
    }

    @Override
    public void setHunger(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public boolean setGameMode(String s) {
        throw new NotImplementedException();
    }

    @Override
    public boolean setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        return true;
    }

    @Override
    public double getPrice() {
        throw new NotImplementedException();
    }

    @Override
    public void setPrice(double v) {
        throw new NotImplementedException();
    }

    @Override
    public @Nullable Material getCurrency() {
        throw new NotImplementedException();
    }

    @Override
    public void setCurrency(@Nullable Material material) {
        throw new NotImplementedException();
    }

    @Override
    public World getRespawnToWorld() {
        throw new NotImplementedException();
    }

    @Override
    public boolean setRespawnToWorld(String s) {
        throw new NotImplementedException();
    }

    @Override
    public double getScaling() {
        throw new NotImplementedException();
    }

    @Override
    public boolean setScaling(double v) {
        throw new NotImplementedException();
    }

    @Override
    public boolean getAutoHeal() {
        throw new NotImplementedException();
    }

    @Override
    public void setAutoHeal(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public boolean getAdjustSpawn() {
        throw new NotImplementedException();
    }

    @Override
    public void setAdjustSpawn(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public boolean getAutoLoad() {
        throw new NotImplementedException();
    }

    @Override
    public void setAutoLoad(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public boolean getBedRespawn() {
        throw new NotImplementedException();
    }

    @Override
    public void setBedRespawn(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public int getPlayerLimit() {
        throw new NotImplementedException();
    }

    @Override
    public void setPlayerLimit(int i) {
        throw new NotImplementedException();
    }

    @Override
    public String getTime() {
        throw new NotImplementedException();
    }

    @Override
    public boolean setTime(String s) {
        throw new NotImplementedException();
    }

    @Override
    public void allowPortalMaking(AllowedPortalType allowedPortalType) {
        throw new NotImplementedException();
    }

    @Override
    public AllowedPortalType getAllowedPortals() {
        throw new NotImplementedException();
    }

    @Override
    public List<String> getWorldBlacklist() {
        throw new NotImplementedException();
    }

    @Override
    public String getAllPropertyNames() {
        throw new NotImplementedException();
    }

    @Override
    public boolean getAllowFlight() {
        throw new NotImplementedException();
    }

    @Override
    public void setAllowFlight(boolean b) {
        throw new NotImplementedException();
    }
}
