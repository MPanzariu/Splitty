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
    public static final String LANGUAGE_PATH = "lang/";
    private static final String LANGUAGE_PREFIX = "lang_";

    /***
     * Constructor that takes the resourceFactory the Translation encapsulates
     * @param resourceFactory - the ObservableResourceFactory to be used
     */
    @Inject
    public Translation(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    /***
     * Provides a binding for the translated value corresponding to a text key
     * @param key - the text key (e.g. "startup.label.create")
     * @return - a live-updating binding for the translated value
     */
    public ObservableValue<String> getStringBinding(String key) {
        return resourceFactory.getStringBinding(key);
    }

    /***
     * Changes the language resources supplied to those from a corresponding language file
     * @param locale - the Local corresponding to the language file to switch to
     */
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
        this.locale = locale;
        @SuppressWarnings({"unchecked", "rawtypes"})
        Map<String, String> map = new HashMap<String, String>((Map) languageProperties);
        resourceFactory.setResources(map);
    }

    /***
     * Returns the Locale currently in use
     * @return the Locale currently in use
     */
    public Locale getLocale() {
        return locale;
    }


}
