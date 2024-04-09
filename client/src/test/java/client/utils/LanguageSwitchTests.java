package client.utils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LanguageSwitchTests {
    private Translation translation;
    private File dir;
    private LanguageSwitchUtils utils;
    private Properties properties;

    @BeforeEach
    public void setup() {
        this.translation = mock(Translation.class);
        this.dir = mock(File.class);
        this.properties = mock(Properties.class);
        utils = new LanguageSwitchUtils(translation, dir, properties);
    }

    /**
     * Languages in the language directory should be added to the language observable list.
     */
    @Test
    public void findLanguages() {
        File english = mock(File.class);
        File german = mock(File.class);
        when(dir.listFiles()).thenReturn(new File[] {english, german});
        when(english.getName()).thenReturn("lang_en.properties");
        when(german.getName()).thenReturn("lang_de.properties");
        utils.refreshLanguages();
        ObservableList<Locale> expected = FXCollections.observableArrayList(Locale.ENGLISH, Locale.GERMAN, Locale.ROOT);
        assertEquals(expected, utils.getLanguages());
    }

    /**
     * Testing getter and setter for language observable list.
     */
    @Test
    public void getAndSetLanguages() {
        ObservableList<Locale> languages = FXCollections.observableArrayList(Locale.ENGLISH, Locale.GERMAN);
        utils.setLanguages(Locale.ENGLISH, Locale.GERMAN);
        assertEquals(languages, utils.getLanguages());
    }

    /**
     * Switched language should be written to a config file.
     * @throws IOException Thrown when I/O error occurs
     */
    @Test
    public void persistLanguageSwitch() throws IOException {
        Locale german = Locale.GERMAN;
        utils.persistLanguage(german);
        verify(properties).setProperty("client.language", "de");
        verify(properties).store(any(FileWriter.class), isNull());
    }
}
