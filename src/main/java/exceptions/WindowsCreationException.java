package exceptions;

public class WindowsCreationException extends Exception {
    public WindowsCreationException() {
        super();
    }

    public WindowsCreationException(String message) {
        super(message);
    }

    public WindowsCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public WindowsCreationException(Throwable cause) {
        super(cause);
    }
}