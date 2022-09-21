package result;

import model.Event;

/**
 * Event Result Object of an Event Request
 */
public class EventResult extends Result{
    String associatedUsername;
    String eventID;
    String personID;
    float latitude;
    float longitude;
    String country;
    String city;
    String eventType;
    int year;
    boolean success;

    /**
     * Constructor of Event Result Object
     * @param event Event event
     * @param success Boolean Success Status
     */
    public EventResult(Event event, boolean success) {
        this.associatedUsername = event.getAssociatedUsername();
        this.eventID = event.getEventID();
        this.personID = event.getPersonID();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.country = event.getCountry();
        this.city = event.getCity();
        this.eventType = event.getEventType();
        this.year = event.getYear();
        this.success = success;
    }

    /**
     * Returns the Associated Username of the Event Result
     * @return String Associated Username
     */
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    /**
     * Sets the Associated Username of the Event Result
     * @param associatedUsername String Associated Username
     */
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    /**
     * Returns the EventID of the Event Result
     * @return String EventID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Sets the EventID of the Event Result
     * @param eventID String EventID
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Returns the PersonID of the Event Result
     * @return String PersonID
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Sets the PersonID of the Event Result
     * @param personID String PersonID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * Returns the Latitudinal Coordinate of the Event Result
     * @return Float Latitudinal Coordinate
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Sets the Latitudinal Coordinate of the Event Result
     * @param latitude Float Latitudinal Coordinate
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the Longitudinal Coordinate of the Event Result
     * @return Float Longitudinal Coordinate
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Sets the Longitudinal Coordinate of the Event Result
     * @param longitude Float Longitudinal Coordinate
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the Country name of the Event Result
     * @return String Country name
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the Country name of the Event Result
     * @param country String Country name
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns the City name of the Event Result
     * @return String City name
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the City name of the Event Result
     * @param city String City name
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the Description of the Event Type of the Event Result
     * @return String Event Type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Sets the Description of the Event Type of the Event Result
     * @param eventType String Event Type
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Returns the Year of the Event Result
     * @return Integer Year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the Year of the Event Result
     * @param year Integer Year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Returns the Success Status of the Event Result
     * @return Boolean Success Status
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the Success Status of the Event Result
     * @param success Boolean Success Status
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
