package result;

/**
 * Error Result Object of a Error Request
 */
public class ErrorResult extends Result{
    String message;
    boolean success;

    /**
     * Constructor of Error Result Object
     * @param message String Feedback Message from Server and Database
     * @param success Boolean Success Status
     */
    public ErrorResult(String message, boolean success){
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
    public boolean getSuccess() {
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
