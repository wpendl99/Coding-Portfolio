package result;

/**
 * Fill Result Object of a Fill Request
 */
public class FillResult extends Result{
    String message;
    boolean success;

    /**
     * Constructor of Fill Result Object
     * @param message String Feedback Message from Server and Database
     * @param success Boolean Success Status
     */
    public FillResult(String message, boolean success) {
        this.message = message;
        this. success = success;
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

    /**
     * Returns the Success Status From the Server and Database
     * @return Boolean Success Status
     */
    public boolean isStatus() {
        return success;
    }

    /**
     * Sets the Success Status From the Server and Database
     * @param success Boolean Success Status
     */
    public void setStatus(boolean success) {
        this.success = success;
    }
}
