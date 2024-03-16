package com.immisorator.apiclient;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.stream.Collectors;

@Mod("apiclient")
public class apiclient {
    private static final java.util.logging.Logger LOGGER = LogManager.getLogger();

    public apiclient() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://immisorator-api.azurewebsites.net/api/discord?code=nqCORyCbgcM-FhsIPTAoSfsBcs10uOhnXk1YPep30Z9NAzFulqhseg=="))
                .PUT(/* json-ify a server object with updated data */)
                .build();

        HttpClient client = HttpClient.newBuilder()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(z -> {
                    LOGGER.info("Returned response from Immisorator API: {}", z);
                    System.out.println(z);
                })
                .exceptionally(t -> {
                    LOGGER.warning("Exception: {}", t.getMessage());
                    System.out.println("Exception: " + t.getMessage());
                    return null;
                });

    }

    // private void doClientStuff(final FMLClientSetupEvent event) {
    // // do something that can only be done on the client
    // }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}",
                event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // // You can use EventBusSubscriber to automatically subscribe events on the
    // // contained class (this is subscribing to the MOD
    // // Event bus for receiving Registry Events)
    // @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    // public static class RegistryEvents {
    // @SubscribeEvent
    // public static void onBlocksRegistry(final RegistryEvent.Register<Block>
    // blockRegistryEvent) {
    // // register a new block here
    // LOGGER.info("HELLO from Register Block");
    // }
    // }
}
