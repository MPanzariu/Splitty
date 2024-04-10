package client.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class ReaderUtils {
    /**
     * This factory is made for testing purposes.
     * @param path Path to read from
     * @return FileReader from this path.
     */
    public FileReader createReader(String path) {
        try {
            return new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This factory is made for testing purposes.
     * @param properties Properties to be loaded
     * @param reader Reader to load from
     */
    public void loadProperties(Properties properties, Reader reader) {
        try {
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
