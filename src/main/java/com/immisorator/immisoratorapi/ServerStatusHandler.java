package com.immisorator.immisoratorapi;

import com.mojang.logging.LogUtils;

import java.util.UUID;

import org.slf4j.Logger;

public class ServerStatusHandler {
    // #region Singleton
    private static ServerStatusHandler instance;

    public static ServerStatusHandler getInstance() {
        LOGGER.info("ServerStatusHandler.getInstance() called");
        if (instance == null) {
            LOGGER.info("Instance was null, creating new instance");
            instance = new ServerStatusHandler();
        }
        return instance;
    }
    // #endregion

    private static final Logger LOGGER = LogUtils.getLogger();
    private static MinecraftServerStatusUpdate ServerStatus;
    private static final FileParser mainConfig = Config.getMainConfig();
    private ApiClient apiClient;

    private ServerStatusHandler() {
        LOGGER.info("ServerStatusHandler is starting");
        // private constructor to prevent instantiation
        // know about it
        apiClient = new ApiClient();
        GetOrUpdateServerFromAPI();
    }

    private void GetOrUpdateServerFromAPI() {
        try {
            LOGGER.info("Getting or updating server from API");
            UUID serverId = GetServerId();
            LOGGER.info("Server ID: " + serverId);
            if(serverId == null) {
                serverId = UUID.randomUUID();
            }
            String serverName = GetServerName();
            LOGGER.info("Server Name: " + serverName);
            LOGGER.info("Calling API Client CreateOrUpdateServer");
            apiClient.CreateOrUpdateServer(new MinecraftServerInstance(serverId, serverName), apiClient.new ServerInstanceCallback() {
                @Override
                public void onSuccess(MinecraftServerInstance server) {
                    LOGGER.info("Server created or updated in api: " + server.getName());
                    // TODO: Save server id to config
                    mainConfig.setValue(Config.ConfigKey.SERVER_ID, server.getId().toString());
                    mainConfig.saveConfigFile();

                    // Was going to call this from the constructor, 
                    // but because these calls are non-blocking, 
                    // this must be called from the callback
                    UpdateOrCreateServerStatus();

                }

                @Override
                public void onFailure(Exception e) {
                    LOGGER.error("Error creating or updating server in api: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            LOGGER.error("Error creating or updating server in api: " + e.getMessage());
        }
    }

    private UUID GetServerId() {
        var serverId = mainConfig.getValue(Config.ConfigKey.SERVER_ID);
        if (serverId != null && !serverId.isEmpty()) {
            return UUID.fromString(serverId);
        }
        return null;
    }
    private String GetServerName() {
        return mainConfig.getValue(Config.ConfigKey.SERVER_NAME);
    }
    private void UpdateOrCreateServerStatus(){
        try {
            LOGGER.info("Getting current status from API");
            // TODO: Get ServerID from config
            UUID serverId = GetServerId();
            apiClient.GetCurrentStatus(serverId, apiClient.new CurrentStatusCallback() {
                @Override
                public void onSuccess(MinecraftServerStatusUpdate status) {
                    ServerStatus = status;
                    if (status.getStatus() == MinecraftServerStatusUpdate.Status.OFFLINE) {
                        ServerStatus.setStatus(MinecraftServerStatusUpdate.Status.ONLINE);
                        SendStatusUpdate();
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    ServerStatus = new MinecraftServerStatusUpdate(
                            java.util.UUID.randomUUID(),
                            GetServerId(),
                            MinecraftServerStatusUpdate.Status.ONLINE,
                            new java.util.ArrayList<String>(),
                            new java.util.ArrayList<String>());
                    SendStatusUpdate();
                }
            });
        } catch (Exception e) {
            LOGGER.error("Error getting server status from API: " + e.getMessage());
        }
    }

    private void SendStatusUpdate() {
        try {
            this.apiClient.SendPlayerUpdate(ServerStatus);
        } catch (Exception e) {
            LOGGER.error("Error sending server status update to API: " + e.getMessage());
        }
    }

    public void ServerStarting() {
        // Need to wait until we have a ServerStatus object,
        if (ServerStatus == null) {
            return; // No need to do anything if we don't have a status object. constructor will
                    // call the API
        }
        // then update the status
        ServerStatus.setStatus(MinecraftServerStatusUpdate.Status.ONLINE);
        // then send the status to the API
        SendStatusUpdate();
    }

    public void ServerStopping() {
        if (ServerStatus == null) {
            return; // No need to do anything if we don't have a status object. constructor will
                    // call the API
        }
        // then update the status
        ServerStatus.setStatus(MinecraftServerStatusUpdate.Status.OFFLINE);
        // then send the status to the API
        SendStatusUpdate();
    }

    public void PlayerJoin(String playerName) {
        if (ServerStatus == null) {
            return; // No need to do anything if we don't have a status object. constructor will
                    // call the API
        }
        // update the status
        ServerStatus.AddPlayer(playerName);
        // then send the status to the API
        SendStatusUpdate();
    }

    public void PlayerLeave(String playerName) {
        if (ServerStatus == null) {
            return; // No need to do anything if we don't have a status object. constructor will
                    // call the API
        }
        // update the status
        ServerStatus.RemovePlayer(playerName);
        // then send the status to the API
        SendStatusUpdate();
    }

}
