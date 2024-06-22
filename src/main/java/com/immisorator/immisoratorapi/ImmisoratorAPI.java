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
    private static final FileParser mainConfig = Config.getMainConfig();

    public ImmisoratorAPI() {
        LOGGER.info("ImmisoratorAPI: Immisorator API is starting");
        MinecraftForge.EVENT_BUS.register(this);
    }

    private boolean isConfigured() {
        // get api from config
        String apiUrl = mainConfig.getValue(Config.ConfigKey.API_URL);
        String apiKey = mainConfig.getValue(Config.ConfigKey.API_KEY);
        LOGGER.info("ImmisoratorAPI: API URL: {}", apiUrl);
        LOGGER.info("ImmisoratorAPI: API Key: {}", apiKey);
        boolean isConfigured = !apiUrl.isEmpty() && !apiKey.isEmpty();
        if (!isConfigured) {
            LOGGER.error("ImmisoratorAPI: API URL or API Key is not configured");
        }
        return isConfigured;
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("ImmisoratorAPI: Server is starting");
        if (!isConfigured())
            return;
        serverStatusHandler.ServerStarting();
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        LOGGER.info("ImmisoratorAPI: Server is stopping");
        if (!isConfigured())
            return;
        serverStatusHandler.ServerStopping();
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        LOGGER.info("ImmisoratorAPI: Player {} has joined the server", event.getPlayer().getName().getString());
        if (!isConfigured())
            return;
        serverStatusHandler.PlayerJoin(event.getPlayer().getName().getString());
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerLoggedOutEvent event) {
        LOGGER.info("ImmisoratorAPI: Player {} has left the server", event.getPlayer().getName().getString());
        if (!isConfigured())
            return;
        serverStatusHandler.PlayerLeave(event.getPlayer().getName().getString());
    }
}
