package request;

import model.Event;
import model.Person;
import model.User;

import java.util.List;

/**
 * Fill Request Object for Loading Users, Persons and Events into respective Databases
 */
public class LoadRequest {
    public User[] users;
    public Person[] persons;
    public Event[] events;

    /**
     * Constructor for generating a request to load specified Users, Persons and Events into respective Databases
     * @param users User Array of Users to add to database
     * @param persons Person Array of Persons to add to database
     * @param events Event Array of Events to add to database
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    /**
     * Returns the User Array of the Load Request
     * @return User Array
     */
    public User[] getUsers() {
        return users;
    }

    /**
     * Sets the User Array of the Load Request
     * @param users User Array
     */
    public void setUsers(User[] users) {
        this.users = users;
    }

    /**
     * Returns the Person Array of the Load Request
     * @return Person Array
     */
    public Person[] getPersons() {
        return persons;
    }

    /**
     * Sets the Person Array of the Load Request
     * @param persons Person Array
     */
    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    /**
     * Returns the Event Array of the Load Request
     * @return Event Array
     */
    public Event[] getEvents() {
        return events;
    }

    /**
     * Sets the Event Array of the Load Request
     * @param events Event Array
     */
    public void setEvents(Event[] events) {
        this.events = events;
    }
}
