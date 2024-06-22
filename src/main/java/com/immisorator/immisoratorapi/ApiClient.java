package com.immisorator.immisoratorapi;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.MediaType;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import java.io.IOException;
import java.util.UUID;

import com.google.gson.Gson;

public class ApiClient {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final Gson gson = new Gson();
    private static final FileParser mainConfig = Config.getMainConfig();

    /**
     * Can be used  to create a new server in the api. or to update the server name in the api.
     * @param server
     * @param callback
     */
    public void CreateOrUpdateServer(MinecraftServerInstance server,ServerInstanceCallback callback){
        String serverString = gson.toJson(server);
        String apiUrl = mainConfig.getValue(Config.ConfigKey.API_URL);
        String apiKey = mainConfig.getValue(Config.ConfigKey.API_KEY);

        OkHttpClient client = new OkHttpClient();
        RequestBody serverPostBody = RequestBody.create(JSON, serverString);
        Request serverPost = new Request.Builder()
                .url( apiUrl + "/api/ServerInstance" + "?code=" + apiKey)
                .post(serverPostBody)
                .build();

        client
                .newCall(serverPost)
                .enqueue(
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                callback.onFailure(e);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (!response.isSuccessful()) {
                                    callback.onFailure(new IOException("Unexpected code " + response));
                                    return;
                                }
                                var bodyResponse = response.body().string();
                                MinecraftServerInstance server = gson.fromJson(bodyResponse,
                                        MinecraftServerInstance.class);
                                callback.onSuccess(server);
                            }
                        });

    }

    public void GetCurrentStatus(UUID serverId, CurrentStatusCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String apiUrl = mainConfig.getValue(Config.ConfigKey.API_URL);
        String apiKey = mainConfig.getValue(Config.ConfigKey.API_KEY);
        Request playerStatusUpdatePost = new Request.Builder()
                .url(apiUrl + "/api/ServerStatus/" + serverId + "?code=" + apiKey)
                .get()
                .build();

        client
                .newCall(playerStatusUpdatePost)
                .enqueue(
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                callback.onFailure(e);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (!response.isSuccessful()) {
                                    callback.onFailure(new IOException("Unexpected code " + response));
                                    return;
                                }
                                var bodyResponse = response.body().string();
                                MinecraftServerStatusUpdate status = gson.fromJson(bodyResponse,
                                        MinecraftServerStatusUpdate.class);
                                if (status == null) {
                                    callback.onFailure(new IOException("Status is null"));
                                    return;
                                }
                                callback.onSuccess(status);
                            }
                        });
    }

    public void SendPlayerUpdate(MinecraftServerStatusUpdate status) {
        String statusString = gson.toJson(status);
        String apiUrl = mainConfig.getValue(Config.ConfigKey.API_URL);
        String apiKey = mainConfig.getValue(Config.ConfigKey.API_KEY);

        OkHttpClient client = new OkHttpClient();
        RequestBody playerUpdatePostBody = RequestBody.create(JSON, statusString);
        Request playerStatusUpdatePost = new Request.Builder()
                .url(apiUrl + "/api/ServerStatus/" + status.getServerId() + "?code=" + apiKey)
                .post(playerUpdatePostBody)
                .build();

        client
                .newCall(playerStatusUpdatePost)
                .enqueue(
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (!response.isSuccessful())
                                    throw new IOException("Unexpected code " + response);
                                System.out.println(response.body().string());
                            }
                        });
    }

    public class CurrentStatusCallback {
        public void onSuccess(MinecraftServerStatusUpdate status) {
            System.out.println("Successfully sent status update to API");
        }

        public void onFailure(Exception e) {
            System.out.println("Error sending status update to API: " + e.getMessage());
        }
    }

    public class ServerInstanceCallback {
        public void onSuccess(MinecraftServerInstance instance) {
            System.out.println("Successfully sent status update to API");
        }

        public void onFailure(Exception e) {
            System.out.println("Error sending status update to API: " + e.getMessage());
        }
    }
}
