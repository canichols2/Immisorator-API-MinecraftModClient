package com.immisorator.apiclient;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;
import com.google.gson.Gson;  

public class ApiClientMethods {
    public static final MediaType JSON
    = MediaType.get("application/json; charset=utf-8");

    public ApiClientMethods() {
    }

    public void SendPlayerUpdate(PlayerStatusUpdate status) throws IOException {
        Gson gson = new Gson();
        String statusString = gson.toJson(status);
        
        OkHttpClient client = new OkHttpClient();
        RequestBody playerUpdatePostBody = RequestBody.create(JSON, statusString);
        Request playerStatusUpdatePost = new Request.Builder()
                .url("http://localhost:7071/api/StatusUpdate")
                .post(playerUpdatePostBody)
                .build();
        try(Response response = client.newCall(playerStatusUpdatePost).execute()){
            String responseString = response.body().string();
        }
    }

}