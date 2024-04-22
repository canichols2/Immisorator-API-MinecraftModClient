package com.immisorator.immisoratorapi;

import java.util.List;
import java.util.UUID;

public class MinecraftServerStatusUpdate {
    public UUID id;
    public UUID serverId;
    public Status status;
    public List<String> onlinePlayers;
    public List<String> allPlayers;

    // Constructor
    public MinecraftServerStatusUpdate() {
    }

    public MinecraftServerStatusUpdate(UUID id, UUID serverId, Status status, List<String> onlinePlayers,
            List<String> allPlayers) {
        this.id = id;
        this.serverId = serverId;
        this.status = status;
        this.onlinePlayers = onlinePlayers;
        this.allPlayers = allPlayers;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getServerId() {
        return serverId;
    }

    public void setServerId(UUID serverId) {
        this.serverId = serverId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(List<String> onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public List<String> getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(List<String> allPlayers) {
        this.allPlayers = allPlayers;
    }

    // Enum for Status
    public enum Status {
        OFFLINE,
        ONLINE,
        ERROR
    }

    public void AddPlayer(String playerName) {
        if (!onlinePlayers.contains(playerName))
            onlinePlayers.add(playerName);
        if (!allPlayers.contains(playerName))
            allPlayers.add(playerName);
    }

    public void RemovePlayer(String playerName) {
        onlinePlayers.remove(playerName);
    }
}
