package client.Exceptions;

public class IncompleteLanguageException extends RuntimeException {
    public IncompleteLanguageException() {
        super("Language has an empty property");
    }
}
