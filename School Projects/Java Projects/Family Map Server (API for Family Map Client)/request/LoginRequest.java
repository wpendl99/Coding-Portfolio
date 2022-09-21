package request;

/**
 * Login Request for Verifying Users in database
 */
public class LoginRequest {
    String username;
    String password;

    /**
     * Constructor for generating a Login Request for a User
     * @param username String Username
     * @param password String Password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the Username of the Login Request
     * @return String Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the Username of the Login Request
     * @param username String Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the Password of the Login Request
     * @return String Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the Password of the Login Request
     * @param password String Password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
