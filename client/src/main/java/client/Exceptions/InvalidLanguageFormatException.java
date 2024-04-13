package client.Exceptions;

public class InvalidLanguageFormatException extends RuntimeException {
    public InvalidLanguageFormatException() {
        super("Language has incorrect format");
    }
}
