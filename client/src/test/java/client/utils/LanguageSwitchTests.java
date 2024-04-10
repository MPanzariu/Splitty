package client.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LanguageSwitchTests {
    private Translation translation;
    private File dir;
    private LanguageSwitchUtils utils;
    private Properties properties;
    private Properties languageProperties;
    private ReaderUtils readerUtils;

    /**
     * Test setup
     */
    @BeforeEach
    public void setup() {
        this.translation = mock(Translation.class);
        this.dir = mock(File.class);
        this.properties = mock(Properties.class);
        this.languageProperties = new Properties();
        this.readerUtils = mock(ReaderUtils.class);
        utils = new LanguageSwitchUtils(dir, properties, languageProperties, readerUtils);
    }

    /**
     * Languages in the language directory should be added to the language observable list.
     */
    @Test
    public void findLanguages() {
        File english = mock(File.class);
        File german = mock(File.class);
        when(dir.listFiles()).thenReturn(new File[] {english, german});
        when(english.getName()).thenReturn("en_GB.properties");
        when(german.getName()).thenReturn("de_DE.properties");
        utils.refreshLanguages();
        ObservableList<Locale> expected = FXCollections.observableArrayList(Locale.of("en", "GB"), Locale.of("de", "DE"), Locale.ROOT);
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
        Locale german = Locale.of("de", "DE");
        utils.persistLanguage(german);
        verify(properties).setProperty("client.language", "de_DE");
        verify(properties).store(any(FileWriter.class), anyString());
    }

    /**
     * Should return true when a language template has one empty value for some key.
     * @throws IOException I don't understand how this could be raised by Mockito.verify().
     */
    @Test
    public void langHasEmptyProperty() throws IOException {
        FileReader reader = mock(FileReader.class);
        doAnswer(answer -> {
            languageProperties.setProperty("header", "Test");
            languageProperties.setProperty("error", "You're wrong!");
            languageProperties.setProperty("A", "");
            return null;
        }).when(readerUtils).loadProperties(languageProperties, reader);
        when(readerUtils.createReader("lang/en_US.properties")).thenReturn(reader);
        assertTrue(utils.hasEmptyProperty(Locale.of("en", "US")));
        verify(reader).close();
    }

    /**
     * Should return true when the language template has all its keys non-empty.
     * @throws IOException I don't understand how this could be raised by Mockito.verify().
     */
    @Test
    public void langHasNoEmptyProperty() throws IOException {
        FileReader reader = mock(FileReader.class);
        doAnswer(answer -> {
            languageProperties.setProperty("header", "Test");
            languageProperties.setProperty("error", "You're wrong!");
            return null;
        }).when(readerUtils).loadProperties(languageProperties, reader);
        when(readerUtils.createReader("lang/en_US.properties")).thenReturn(reader);
        assertFalse(utils.hasEmptyProperty(Locale.of("en", "US")));
        verify(reader).close();
    }
}
