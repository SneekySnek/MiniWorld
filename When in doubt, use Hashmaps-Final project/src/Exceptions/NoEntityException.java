package Exceptions;
/**
 * This class represents an exception that is thrown when no entity is found.
 * It extends the RuntimeException class.
 */
public class NoEntityException extends RuntimeException {
    /**
     * Constructs a new NoEntityException with the specified detail message.
     *
     * @param Message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public NoEntityException(String Message) {
        super(Message);
    }
}
