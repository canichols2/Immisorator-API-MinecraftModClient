package com.immisorator.immisoratorapi;

import java.util.HashMap;

public class Config {
    public static class ConfigKey {
        public static final String SERVER_NAME = "SERVER_NAME";
        public static final String API_URL = "API_URL";
        public static final String API_KEY = "API_KEY";
        public static final String SERVER_ID = "SERVER_ID";
    }

    public static final String CONFIG_FILE_NAME = "config/ComImmisoratorApi/cfg.config";
    public static final String CONFIG_DIR_NAME = "config/ComImmisoratorApi";
    private static HashMap<String, String> mainConfigDefaults = new HashMap<String, String>(){
        {
            put(ConfigKey.API_URL, "https://immisorator-api.azurewebsites.net");
            put(ConfigKey.API_KEY, "");
            put(ConfigKey.SERVER_ID, "");
            put(ConfigKey.SERVER_NAME, "New Minecraft Server");
        }

    };

    private static FileParser mainConfig;
    public static FileParser getMainConfig() {
        if (mainConfig == null) {
            mainConfig = new FileParser(CONFIG_FILE_NAME, mainConfigDefaults);
        }
        return mainConfig;
    }
}
