package request;

import model.AuthToken;

/**
 * Event Request Object for Getting Events from Event Database
 */
public class EventRequest {
    public String eventID;
    public AuthToken authToken;

    /**
     * Default Constructor
     */
    public EventRequest() {
    }

    /**
     * Preferred Constructor for generating a request to get all Events for an Associated Username
     * @param authToken AuthToken authToken for User
     */
    public EventRequest(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Preferred Constructor for generating a request to get a specific Events for an Associated Username
     * @param authToken AuthToken authToken for User
     * @param eventID String of EventID for Event
     */
    public EventRequest(AuthToken authToken, String eventID) {
        this.eventID = eventID;
        this.authToken = authToken;
    }

    /**
     * Returns the EventID of the Event Request
     * @return String EventID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Sets the EventID of the Event Request
     * @param eventID String EventID
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Returns the AuthToken of the Event Request
     * @return AuthToken authToken
     */
    public AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Sets the AuthToken of the Event Request
     * @param authToken AuthToken authToken
     */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
