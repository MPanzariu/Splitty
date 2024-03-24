package client.utils;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.Locale;

public class LanguageSwitchUtils {
    private final ObservableList<Locale> languages = FXCollections.observableArrayList();
    private final Translation translation;
    private final File dir;

    @Inject
    public LanguageSwitchUtils(Translation translation, @Named("dir") File dir) {
        this.translation = translation;
        this.dir = dir;
    }

    /**
     * Change the language of the client when selecting a new value.
     * @param property Selected property
     */
    public void setBehavior(ReadOnlyObjectProperty<Locale> property) {
        property.addListener((observable, oldValue, newValue) -> {
            // Languages will be emptied in refresh() method, which should be ignored.
            if(newValue != null)
                translation.changeLanguage(newValue);
        });
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
                languages.add(Locale.of(parts[1]));
            }
        }
    }

    public ObservableList<Locale> getLanguages() {
        return languages;
    }

    public void setLanguages(Locale... languages) {
        this.languages.setAll(languages);
    }
}
