package result;

/**
 * Clear Result Object of a Clear Request
 */
public class ClearResult extends Result{
    String message;
    boolean success;

    /**
     * Constructor of Clear Result Object
     * @param message String Feedback Message from Server and Database
     * @param success Boolean Success Status
     */
    public ClearResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    /**
     * Returns the Feedback Message From the Server and Database
     * @return String Feedback message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the Feedback Message From the Server and Database
     * @param message String Feedback message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
