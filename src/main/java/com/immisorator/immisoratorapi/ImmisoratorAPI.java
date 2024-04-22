package com.immisorator.immisoratorapi;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import org.slf4j.Logger;

// import java.util.stream.Collectors;

@Mod("immisoratorapi")
public class ImmisoratorAPI {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ServerStatusHandler serverStatusHandler = ServerStatusHandler.getInstance();
    public ImmisoratorAPI(){
        LOGGER.info("Immisorator API is starting");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event){
        LOGGER.info("Server is starting");
        serverStatusHandler.ServerStarting();
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event){
        LOGGER.info("Server is stopping");
        serverStatusHandler.ServerStopping();
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event){
        LOGGER.info("Player {} has joined the server", event.getPlayer().getName().getString());
        serverStatusHandler.PlayerJoin(event.getPlayer().getName().getString());
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerLoggedOutEvent event){
        LOGGER.info("Player {} has left the server", event.getPlayer().getName().getString());
        serverStatusHandler.PlayerLeave(event.getPlayer().getName().getString());
    }
}
