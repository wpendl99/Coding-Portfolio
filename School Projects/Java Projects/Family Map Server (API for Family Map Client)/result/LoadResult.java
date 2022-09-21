package result;

/**
 * Load Result Object of a Load Request
 */
public class LoadResult extends Result {
    String message;
    boolean success;

    /**
     * Constructor of Load Result Object
     * @param message String Feedback Message from Server and Database
     * @param success Boolean Success Status
     */
    public LoadResult(String message, Boolean success) {
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

    /**
     * Returns the Success Status From the Server and Database
     * @return Boolean Success Status
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the Success Status From the Server and Database
     * @param success Boolean Success Status
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
