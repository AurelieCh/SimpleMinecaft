package exceptions.shaders;

public class ShadersLinkingException extends Exception {
    public ShadersLinkingException() {
        super();
    }

    public ShadersLinkingException(String message) {
        super(message);
    }

    public ShadersLinkingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShadersLinkingException(Throwable cause) {
        super(cause);
    }
}
