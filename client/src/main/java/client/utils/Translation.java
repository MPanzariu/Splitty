package client.utils;

import com.google.inject.Inject;
import javafx.beans.value.ObservableValue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class Translation {
    private final ObservableResourceFactory resourceFactory;
    private Locale locale;
    public static String LANGUAGE_PATH = "lang/";
    public static String LANGUAGE_PREFIX = "lang_";

    @Inject
    public Translation(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    public ObservableValue<String> getStringBinding(String key) {
        return resourceFactory.getStringBinding(key);
    }

    public void changeLanguage(Locale locale) {
        String languageCode = locale.getLanguage();
        String fileName = LANGUAGE_PREFIX.concat(languageCode).concat(".properties");

        Properties languageProperties = new Properties();
        File file = new File(LANGUAGE_PATH, fileName);
        try {
            languageProperties.load(new FileReader(file));
        } catch (IOException e) {
            // no such localization file
            if(ConfigUtils.DEFAULT_PROPS_LANGUAGE.equals(locale.getLanguage())){
                // a default language is missing!!!
                throw new RuntimeException("Bundled language files could not be found");
            }
            else{
                // try to load default instead
                changeLanguage(Locale.forLanguageTag(ConfigUtils.DEFAULT_PROPS_LANGUAGE));
                return;
            }
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        Map<String, String> map = new HashMap<String, String>((Map) languageProperties);
        resourceFactory.setResources(map);

        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }


}
