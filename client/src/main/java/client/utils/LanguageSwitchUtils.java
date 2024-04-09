package client.utils;

import client.scenes.GenerateLanguageTemplate;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class LanguageSwitchUtils {
    private final ObservableList<Locale> languages = FXCollections.observableArrayList();
    private final Translation translation;
    private final File dir;
    private final Properties properties;

    /**
     * Constructor
     * @param translation
     * @param dir
     * @param properties
     */
    @Inject
    public LanguageSwitchUtils(Translation translation, @Named("dir") File dir,
                               @Named("config") Properties properties) {
        this.translation = translation;
        this.dir = dir;
        this.properties = properties;
    }

    /**
     * Writes the language to the config file.
     * @param language The newly selected language
     */
    public void persistLanguage(Locale language) {
        try {
            FileWriter writer = new FileWriter(ConfigUtils.CONFIG_NAME);
            properties.setProperty("client.language", language.getLanguage() + "_" + language.getCountry());
            properties.store(writer, ConfigUtils.CONFIG_COMMENTS);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(ConfigUtils.CONFIG_NAME + " is not found!");
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
