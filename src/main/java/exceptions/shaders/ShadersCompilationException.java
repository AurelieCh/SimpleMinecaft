package exceptions.shaders;

public class ShadersCompilationException extends Exception {
    public ShadersCompilationException() {
        super();
    }

    public ShadersCompilationException(String message) {
        super(message);
    }

    public ShadersCompilationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShadersCompilationException(Throwable cause) {
        super(cause);
    }
}
