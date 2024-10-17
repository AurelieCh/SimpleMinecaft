package exceptions.shaders;

public class ShadersValidatingException extends Exception {
    public ShadersValidatingException() {
        super();
    }

    public ShadersValidatingException(String message) {
        super(message);
    }

    public ShadersValidatingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShadersValidatingException(Throwable cause) {
        super(cause);
    }
}
