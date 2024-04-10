package client.utils;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.Locale;
import java.util.Properties;

public class LanguageSwitchUtils {
    private final ObservableList<Locale> languages = FXCollections.observableArrayList();
    private final File dir;
    private final Properties configProperties;
    private final Properties languageProperties;
    private final ReaderUtils readerUtils;

    /**
     * Constructor
     * @param dir Language template directory
     * @param configProperties Properties from the config file
     * @param languageProperties Properties from the currently selected language
     * @param readerUtils Utilities for reading (properties)
     */
    @Inject
    public LanguageSwitchUtils(@Named("dir") File dir,
                               @Named("config") Properties configProperties, Properties languageProperties,
                               ReaderUtils readerUtils) {
        this.dir = dir;
        this.configProperties = configProperties;
        this.languageProperties = languageProperties;
        this.readerUtils = readerUtils;
    }

    /**
     * Writes the language to the config file.
     * @param language The newly selected language
     */
    public void persistLanguage(Locale language) {
        try {
            FileWriter writer = new FileWriter(ConfigUtils.CONFIG_NAME);
            configProperties.setProperty("client.language", language.getLanguage() + "_" + language.getCountry());
            configProperties.store(writer, ConfigUtils.CONFIG_COMMENTS);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(ConfigUtils.CONFIG_NAME + " is not found!");
        }
    }

    /**
     * Check if any of the properties has an empty value.
     * @param locale Locale of selected properties file
     * @return True iff any property has an empty value, else false.
     */
    public boolean hasEmptyProperty(Locale locale) {
        try {
            FileReader reader = readerUtils.createReader("lang/" + locale.getLanguage() + "_" +
                    locale.getCountry() + ".properties");
            readerUtils.loadProperties(languageProperties, reader);
            reader.close();
            return languageProperties.contains("");
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Clears the language list and adds all languages that this application supports.
     */
    public void refreshLanguages() {
        languages.clear();
        File[] files = dir.listFiles();
        if(files != null) {
            for(File properties : files) {
                String name = properties.getName();
                if(name.equals("template.properties"))
                    continue;
                String[] parts = name.split("_|\\.");
                languages.add(Locale.of(parts[0], parts[1]));
            }
        }
        languages.add(Locale.ROOT);
    }

    /**
     * Getter for the list of languages.
     * @return List of languages
     */
    public ObservableList<Locale> getLanguages() {
        return languages;
    }

    /**
     * Setter for the list of languages.
     * @param languages List of languages
     */
    public void setLanguages(Locale... languages) {
        this.languages.setAll(languages);
    }
}
