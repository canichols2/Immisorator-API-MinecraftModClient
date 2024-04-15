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
    
    public ImmisoratorAPI(){
        LOGGER.info("Immisorator API is starting");
        MinecraftForge.EVENT_BUS.register(this);
        // TODO: Create class to call API
        // TODO: Create class to read config (which will have api url and server key)
        // TODO: Instantiate class to call API
        // TODO: Instantiate class to read config
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event){
        // TODO: Send a message to the API
        LOGGER.info("Server is starting");
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event){
        // TODO: Send message to API
        LOGGER.info("Server is stopping");
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event){
        // TODO: Send message to API
        LOGGER.info("Player {} has joined the server", event.getPlayer().getName().getString());
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerLoggedOutEvent event){
        // TODO: Send message to API
        LOGGER.info("Player {} has left the server", event.getPlayer().getName().getString());
    }
}
