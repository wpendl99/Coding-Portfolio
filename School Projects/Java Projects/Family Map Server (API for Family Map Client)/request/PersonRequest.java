package request;

import model.AuthToken;

/**
 * Person Request Object for Getting Persons from Person Database
 */
public class PersonRequest {
    public AuthToken authToken;
    public String personID;

    /**
     * Default Constructor
     */
    public PersonRequest() {
    }

    /**
     * Preferred Constructor for generating a request to get a specific Persons for an Associated Username
     * @param authToken AuthToken authToken for User
     * @param personID String of PersonID for Person
     */
    public PersonRequest(AuthToken authToken, String personID) {
        this.authToken = authToken;
        this.personID = personID;
    }

    /**
     * Preferred Constructor for generating a request to get all Persons for an Associated Username
     * @param authToken AuthToken authToken for User
     */
    public PersonRequest(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Returns the AuthToken of the Person Request
     * @return AuthToken authToken
     */
    public AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Sets the AuthToken of the Person Request
     * @param authToken AuthToken authToken
     */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Returns the PersonID of the Person Request
     * @return String PersonID
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Sets the PersonID of the Person Request
     * @param personID String PersonID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
