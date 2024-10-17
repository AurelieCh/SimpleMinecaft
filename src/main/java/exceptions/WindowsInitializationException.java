package exceptions;

public class WindowsInitializationException extends Exception {
    public WindowsInitializationException() {
        super();
    }

    public WindowsInitializationException(String message) {
        super(message);
    }

    public WindowsInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public WindowsInitializationException(Throwable cause) {
        super(cause);
    }
}
