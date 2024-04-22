package com.immisorator.immisoratorapi;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;

/**
 * This Class is used to parse Config Files.
 * It is used to read and write Config Files.
 * Stolen from https://github.com/Hydrocynus/DiscordPlayerListWebhook/tree/main 
 */
public class FileParser {

    private HashMap<String, String> defaults;
    protected HashMap<String, String> values;
    protected String configFileName;
    public FileParser(String configFileName, HashMap<String, String> defaults) {
        this.configFileName = configFileName;
        this.defaults = defaults;
        this.values = new HashMap<>();

        loadFile();
    }

    public FileParser(String configFileName) {
        this.configFileName = configFileName;
        this.defaults = new HashMap<String, String>();
        defaults.put("key", "value");
        this.values = new HashMap<String, String>();

        loadFile();
    }

    /**
     * This Method returns the Config File Name of the config that this class should parse.
     * @return The Config File Name of the config that this class should parse.
     */
    protected String getConfigFileName() {
        return configFileName;
    }

    /**
     *
     */
    public void loadFile() {
        File confDir = new File(Config.CONFIG_DIR_NAME);

        //create the config directory if it doesn't exist
        if (!confDir.exists()) {
            confDir.mkdir();
        }

        //if the file doesnt Exist, create the default file
        File file = new File(getConfigFileName());
        if (!file.exists()){
            createDefaultFile(file);
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(getConfigFileName()));
            String line;
            values = new HashMap<>();
            while ((line = reader.readLine()) != null){
                if (line.contains(": ")){
                    var split = line.split(": ");
                    if(split.length == 0) continue;

                    String setting = split[0];
                    if(split.length == 1){
                        values.put(setting, null);
                        continue;
                    }
                    
                    String value = split[1];
                    values.put(setting, value);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally{
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This Method returns the Configvalue corresponding to the key
     * @param key The Key of the Configvalue
     * @return The Configvalue corresponding to the key
     */
    public String getValue(String key) {
        return values.get(key);
    }

    /**
     * This Method sets a Value within the Config
     * WARNING: Value is lost if the config is reloaded after a value was added
     * @param key The Key of the Value
     * @param value The Value to be set
     */
    public void setValue(String key, String value) {
        values.put(key, value);
    }

    /**
     * This Method returns all values of the Config
     *
     * @return All values of the Config
     */
    public Collection<String> getValues() {
        return values.values();
    }


    /**
     * This Method returns the hasmap of all values of the Config
     * @return
     */
    public HashMap<String, String> getMap() {
        return values;
    }

    /**
     * This Method saves the Config to the File
     * Useful if new Values were added during runtime, that should be saved.
     */
    public void saveConfigFile() {
        File file = new File(getConfigFileName());
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String key : values.keySet()) {
                writer.write(key + ": " + values.get(key));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This Method creates a default Config File.
     * @param file The File to be created.
     */
    private void createDefaultFile(File file) {
        try {
            file.createNewFile();
            values = defaults;
            saveConfigFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
