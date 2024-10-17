package exceptions.shaders;

public class ShadersCreationException extends Exception {
    public ShadersCreationException() {
        super();
    }

    public ShadersCreationException(String message) {
        super(message);
    }

    public ShadersCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShadersCreationException(Throwable cause) {
        super(cause);
    }
}
