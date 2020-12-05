package utils;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.api.WorldPurger;
import com.onarandombox.MultiverseCore.utils.PurgeWorlds;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockMVWorldManager implements MVWorldManager {

    private final ServerMock server;
    private final Map<String, MultiverseWorld> MVWorldsMap;

    public MockMVWorldManager(ServerMock server) {
        this.server = server;
        this.MVWorldsMap = new HashMap<>();
    }

    public boolean addWorld(String worldName) {
        return addWorld(worldName, World.Environment.NORMAL);
    }

    public boolean addWorld(String worldName, World.Environment environment) {
        return addWorld(worldName, environment, "", WorldType.NORMAL, true, "");
    }

    @Override
    public boolean addWorld(String s, World.Environment environment, String s1, WorldType worldType, Boolean aBoolean, String s2) {
        if (MVWorldsMap.containsKey(s)) return false;
        WorldMock world = server.addSimpleWorld(s);
        world.setEnvironment(environment);
        MVWorldsMap.put(s, new MockMVWorld(world));
        return true;
    }

    @Override
    public boolean addWorld(String s, World.Environment environment, String s1, WorldType worldType, Boolean aBoolean, String s2, boolean b) {
        if (MVWorldsMap.containsKey(s)) return false;
        WorldMock world = server.addSimpleWorld(s);
        world.setEnvironment(environment);
        MVWorldsMap.put(s, new MockMVWorld(world));
        return true;
    }

    @Override
    public boolean cloneWorld(String s, String s1, String s2) {
        throw new NotImplementedException();
    }

    @Override
    public boolean cloneWorld(String s, String s1) {
        throw new NotImplementedException();
    }

    @Override
    public boolean deleteWorld(String s) {
        return MVWorldsMap.remove(s) != null;
    }

    @Override
    public boolean deleteWorld(String s, boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public boolean deleteWorld(String s, boolean b, boolean b1) {
        throw new NotImplementedException();
    }

    @Override
    public boolean unloadWorld(String s) {
        throw new NotImplementedException();
    }

    @Override
    public boolean unloadWorld(String s, boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public boolean loadWorld(String s) {
        throw new NotImplementedException();
    }

    @Override
    public void removePlayersFromWorld(String s) {
        throw new NotImplementedException();
    }

    @Override
    public ChunkGenerator getChunkGenerator(String s, String s1, String s2) {
        throw new NotImplementedException();
    }

    @Override
    public Collection<MultiverseWorld> getMVWorlds() {
        return MVWorldsMap.values();
    }

    @Override
    public MultiverseWorld getMVWorld(String s) {
        return MVWorldsMap.get(s);
    }

    @Override
    public MultiverseWorld getMVWorld(World world) {
        return getMVWorld(world.getName());
    }

    @Override
    public boolean isMVWorld(String s) {
        return MVWorldsMap.containsKey(s);
    }

    @Override
    public boolean isMVWorld(World world) {
        return isMVWorld(world.getName());
    }

    @Override
    public void loadWorlds(boolean b) {
        throw new NotImplementedException();
    }

    @Override
    public void loadDefaultWorlds() {
        throw new NotImplementedException();
    }

    @Override
    public PurgeWorlds getWorldPurger() {
        throw new NotImplementedException();
    }

    @Override
    public WorldPurger getTheWorldPurger() {
        throw new NotImplementedException();
    }

    @Override
    public MultiverseWorld getSpawnWorld() {
        throw new NotImplementedException();
    }

    @Override
    public List<String> getUnloadedWorlds() {
        throw new NotImplementedException();
    }

    @Override
    public void getDefaultWorldGenerators() {
        throw new NotImplementedException();
    }

    @Override
    public FileConfiguration loadWorldConfig(File file) {
        throw new NotImplementedException();
    }

    @Override
    public boolean saveWorldsConfig() {
        throw new NotImplementedException();
    }

    @Override
    public boolean removeWorldFromConfig(String s) {
        throw new NotImplementedException();
    }

    @Override
    public MultiverseWorld getFirstSpawnWorld() {
        throw new NotImplementedException();
    }

    @Override
    public void setFirstSpawnWorld(String s) {
        throw new NotImplementedException();
    }

    @Override
    public boolean regenWorld(String s, boolean b, boolean b1, String s1) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isKeepingSpawnInMemory(World world) {
        throw new NotImplementedException();
    }

    @Override
    public boolean hasUnloadedWorld(String s, boolean b) {
        throw new NotImplementedException();
    }
}
