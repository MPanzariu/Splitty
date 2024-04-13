package client.Exceptions;

public class MissingLanguageTemplateException extends RuntimeException {
    public MissingLanguageTemplateException() {
        super("Language template is missing");
    }
}
