package client.utils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LanguageSwitchTests {
    private Translation translation;
    private File dir;
    private LanguageSwitchUtils utils;

    @BeforeEach
    public void setup() {
        this.translation = mock(Translation.class);
        this.dir = mock(File.class);
        utils = new LanguageSwitchUtils(translation, dir);
    }

    /**
     * When the property changes, then the language should be changed.
     */
    @Test
    public void changeLanguage() {
        SimpleObjectProperty<Locale> property = new SimpleObjectProperty<>(Locale.ENGLISH);
        utils.setBehavior(property);
        property.setValue(Locale.GERMAN);
        verify(translation).changeLanguage(Locale.GERMAN);
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
        ObservableList<Locale> expected = FXCollections.observableArrayList(Locale.ENGLISH, Locale.GERMAN);
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
}
