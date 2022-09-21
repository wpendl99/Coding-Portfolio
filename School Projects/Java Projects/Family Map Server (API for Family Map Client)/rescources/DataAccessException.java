package rescources;

/**
 * Database Error Exception
 */
public class DataAccessException extends Exception {
    /**
     * Constructor for Database Error Exceptions
     * @param message
     */
    public DataAccessException(String message) {
        super(message);
    }
}
