package model;

/**
 * Authorization Token Object
 */
public class AuthToken {
    private String authtoken;
    private String username;

    /**
     * AuthToken object that contains an authorization token that can be used to identify a logged-in user
     * @param authtoken String of the Authorization Token
     * @param username String of the Username of a user
     */
    public AuthToken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
    }

    /**
     * Returns the Authorization Token of a user
     * @return authToken String of the Authorization Token
     */
    public String getAuthtoken() {
        return authtoken;
    }

    /**
     * Sets the Authorization of a user
     * @param authtoken String of the Authorization Token
     */
    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    /**
     * Returns the Username of a user
     * @return String of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the Username of a user
     * @param username String of the Username of a user
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
