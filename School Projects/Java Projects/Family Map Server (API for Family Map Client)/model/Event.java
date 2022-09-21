package model;

import java.util.Objects;

/**
 * And Event Object Class
 */
public class Event {
    private String eventID;
    private String associatedUsername;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    /**
     * Event object that contains event data for a Person object
     * @param eventID String of a unique Event ID
     * @param associatedUsername String of the username this event is associated with
     * @param personID String of the unique Person ID this is associated with
     * @param latitude Float of the latitudinal coordinate this event toke place
     * @param longitude Float of the Longitudinal coordinate this event toke place
     * @param country String of the Country name this event toke place
     * @param city String of the City name this event toke place in
     * @param eventType String Describing the type of event this is
     * @param year Integer of the year this event toke place
     */
    public Event(String eventID, String associatedUsername, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    /**
     * Returns the unique EventID of the Event
     * @return String of Event ID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Sets the unique EventID of the Event
     * @param eventID String of Event ID
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Returns the username associated with this event
     * @return String of username
     */
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    /**
     * Sets the username associated with this event
     * @param associatedUsername String of username
     */
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    /**
     * Returns the PersonID associated with this event
     * @return String of PersonID
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Sets the PersonID associated with this event
     * @param personID String of PersonID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * Returns the Latitudinal coordinate of this event
     * @return Float of Latitudinal coordinate
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Sets the Latitudinal coordinate of this event
     * @param latitude Float of the Latitudinal coordinate
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the Longitudinal coordinate of this event
     * @return Float of Longitudinal coordinate
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Sets the Longitudinal coordinate of this event
     * @param longitude Float of the Longitudinal coordinate
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the name of the Country this event toke place in
     * @return String of the Country name
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the name of the country this event toke place in
     * @param country String of the Country name
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns the name of the City this event toke place in
     * @return String of the City name
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the name of the City this event toke place in
     * @param city String of the City name
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the description of the Event Type
     * @return String of Event Type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Sets the description of the Event Type
     * @param eventType String of Event Type
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Returns the year this event toke place in
     * @return Integer year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the year this event toke place in
     * @param year Integer year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * A function used to determine if two or more events are the same
     * @param o Event Object
     * @return Boolean statement, True means they are the same event
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventID, event.eventID) && Objects.equals(associatedUsername, event.associatedUsername) && Objects.equals(personID, event.personID) && Objects.equals(latitude, event.latitude) && Objects.equals(longitude, event.longitude) && Objects.equals(country, event.country) && Objects.equals(city, event.city) && Objects.equals(eventType, event.eventType) && Objects.equals(year, event.year);
    }
}
