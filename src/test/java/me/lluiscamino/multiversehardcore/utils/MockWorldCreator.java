package me.lluiscamino.multiversehardcore.utils;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import me.lluiscamino.multiversehardcore.exceptions.HardcoreWorldCreationException;
import me.lluiscamino.multiversehardcore.models.HardcoreWorld;
import me.lluiscamino.multiversehardcore.models.HardcoreWorldConfiguration;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

public class MockWorldCreator {

    private final ServerMock server;
    private final MockMVWorldManager worldManager;
    private UUID lastUUID;

    public MockWorldCreator(@NotNull ServerMock server, @NotNull MockMVWorldManager worldManager) {
        this.server = server;
        this.worldManager = worldManager;
    }

    public void makeWorldHardcore(@NotNull WorldMock world) {
        try {
            HardcoreWorldConfiguration worldConfiguration = new HardcoreWorldConfiguration(
                    world,
                    null,
                    new Date(),
                    true,
                    0,
                    true,
                    true,
                    true
            );
            HardcoreWorld.createHardcoreWorld(worldConfiguration);
        } catch (HardcoreWorldCreationException e) {
            throw new RuntimeException("World could not be made Hardcore!");
        }
    }

    public WorldMock createHardcoreWorld() {
        WorldMock world = createNormalWorld();
        makeWorldHardcore(world);
        return world;
    }

    public WorldMock createHardcoreWorld(@NotNull String worldName) {
        WorldMock world = createNormalWorld(worldName);
        makeWorldHardcore(world);
        return world;
    }

    public WorldMock createNormalWorld() {
        lastUUID = UUID.randomUUID();
        WorldMock world = server.addSimpleWorld(lastUUID + "world");
        worldManager.addWorld(world.getName());
        return world;
    }

    public WorldMock createNormalWorld(@NotNull String worldName) {
        return server.addSimpleWorld(worldName);
    }

    public WorldMock createNetherWorld() {
        return createEnvironmentWorld(World.Environment.NETHER);
    }

    public WorldMock createTheEndWorld() {
        return createEnvironmentWorld(World.Environment.THE_END);
    }

    private WorldMock createEnvironmentWorld(@NotNull World.Environment environment) {
        UUID uuid = lastUUID != null ? lastUUID : UUID.randomUUID();
        WorldMock world = server.addSimpleWorld(uuid + "world" + getEnvironmentPrefix(environment));
        world.setEnvironment(environment);
        worldManager.addWorld(world.getName());
        return world;
    }

    private String getEnvironmentPrefix(@NotNull World.Environment environment) {
        return "_" + environment.name().toLowerCase();
    }
}
