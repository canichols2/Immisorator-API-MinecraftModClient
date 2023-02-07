package com.immisorator.apiclient;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent; //To track entering/leaving the vault
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

@Mod("apiclient")
public class apiclient {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<String> Active_Players = new ArrayList<String>();
    private final ApiClientMethods statusUpdater = new ApiClientMethods();

    public apiclient() {
        MinecraftForge.EVENT_BUS.addListener(this::ProcessPlayerLogin);
        MinecraftForge.EVENT_BUS.addListener(this::ProcessPlayerLogout);
    }

    private void ProcessPlayerLogin(PlayerLoggedInEvent loginEvent) {
        String playername = loginEvent.getPlayer().getDisplayName().toString();
        Active_Players.add(playername);
        SendPlayerUpdate();
    }

    private void ProcessPlayerLogout(PlayerLoggedOutEvent loginEvent) {
        String playername = loginEvent.getPlayer().getDisplayName().toString();
        Active_Players.removeIf((arrName) -> arrName == playername);
        SendPlayerUpdate();
    }

    private void SendPlayerUpdate(){
        PlayerStatusUpdate statusUpdate = new PlayerStatusUpdate();
        statusUpdate.OnlinePlayers = Active_Players.toArray(new String[0]);
        try {
            statusUpdater.SendPlayerUpdate(statusUpdate);            
        } catch (Exception e) {
            LOGGER.info("Unable to send message");
        }

    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        // LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the
    // contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            // LOGGER.info("HELLO from Register Block");
        }
    }
}
