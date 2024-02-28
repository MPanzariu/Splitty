package client.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class ConfigUtils {
    public static final String CONFIG_NAME = "splitty.properties";
    public static final int CONFIG_VERSION = 2; // update this whenever you add/remove/change default properties
    public static final String DEFAULT_PROPS_SERVER_URL = "http://localhost:8080/";
    public static final String DEFAULT_PROPS_LANGUAGE = Locale.ENGLISH.getLanguage();

    public static Properties loadProperties(){
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(CONFIG_NAME));
            if(properties.get("configVersion")!=String.valueOf(CONFIG_VERSION)){
                loadDefaults(properties); // reload if our config has been updated in code
            }
        } catch (IOException ex) {
            loadDefaults(properties);
        }

        return properties;
    }

    private static void loadDefaults(Properties properties){
        properties.setProperty("configVersion", String.valueOf(CONFIG_VERSION));
        properties.setProperty("serverURL", DEFAULT_PROPS_SERVER_URL);
        properties.setProperty("language", DEFAULT_PROPS_LANGUAGE);

        try {
            properties.store(new FileWriter(CONFIG_NAME), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
