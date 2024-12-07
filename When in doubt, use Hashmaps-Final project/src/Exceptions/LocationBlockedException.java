package Exceptions;
/**
 * This class represents an exception that is thrown when a location is blocked.
 * It extends the RuntimeException class.
 */
public class LocationBlockedException extends RuntimeException {
    /**
     * Constructs a new LocationBlockedException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public LocationBlockedException(String message) {
        super(message);
    }
}
