package result;

/**
 * Register Result of a Register Request
 */
public class RegisterResult extends Result {
    String authtoken;
    String username;
    String personID;
    boolean success;

    /**
     * Constructor of Register Result Object
     * @param authtoken String Authorization Token
     * @param username String Username
     * @param personID String PersonID
     * @param success Boolean Success Status
     */
    public RegisterResult(String authtoken, String username, String personID, boolean success) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.success = success;
    }

    /**
     * Returns the Authorization Token of the Register Result
     * @return String Authorization Token
     */
    public String getAuthToken() {
        return authtoken;
    }

    /**
     * Sets the Authorization Token of the Register Result
     * @param authtoken String Authorization Token
     */
    public void setAuthToken(String authtoken) {
        this.authtoken = authtoken;
    }

    /**
     * Returns the Username of the Register Request
     * @return String Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the Username of the Register Request
     * @param username String Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the PersonID of the Register Request
     * @return String PersonID
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Sets the PersonID of the Register Request
     * @param personID String PersonID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
