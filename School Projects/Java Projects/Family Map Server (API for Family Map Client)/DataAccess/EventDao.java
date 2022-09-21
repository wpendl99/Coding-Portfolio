package DataAccess;

import model.Event;
import rescources.DataAccessException;

import java.sql.*;
import java.util.ArrayList;

public class EventDao {
    private final Connection conn;
    
    public EventDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Given an Event object, this will INSERT into Events table
     * @param event Event event
     * @throws DataAccessException Database Error
     */
    public void insert(Event event) throws DataAccessException {
        // Prepare sql query
        String query = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        
        try(PreparedStatement statement = conn.prepareStatement(query)) {
            
            // Set the '?'s of the query
            statement.setString(1, event.getEventID());
            statement.setString(2, event.getAssociatedUsername());
            statement.setString(3, event.getPersonID());
            statement.setFloat(4, event.getLatitude());
            statement.setFloat(5, event.getLongitude());
            statement.setString(6, event.getCountry());
            statement.setString(7, event.getCity());
            statement.setString(8, event.getEventType());
            statement.setInt(9, event.getYear());

            // If all worked, send statement
            statement.executeUpdate();

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error encountered while inserting  an event into the database");
        }
    }

    /**
     * Given an eventID, it will return the corresponding Event object (if any) from the Events table
     * @param eventID String EventID
     * @return Event Event
     * @throws DataAccessException Database Error
     */
    public Event find(String eventID) throws DataAccessException {
        // Prepare SQL query
        Event event;
        ResultSet set;
        String query = "SELECT * FROM Events WHERE EventID = ?;";
        
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Fill in the ?'s
            statement.setString(1, eventID);
            set = statement.executeQuery();
            // If there is a results (There should only be one)
            if (set.next()) {
                event = new Event(set.getString("EventID"), set.getString("AssociatedUsername"),
                        set.getString("PersonID"), set.getFloat("Latitude"), set.getFloat("Longitude"),
                        set.getString("Country"), set.getString("City"), set.getString("EventType"),
                        set.getInt("Year"));

                return event;
            // If the token doesn't exist return null
            } else {
                return null;
            }
        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error while finding an event with that ID in the database");
        }
    }

    /**
     * Given a username, it will return all the corresponding Event objects (if any) from the Events table
     * @param AssociatedUsername String Associated Username
     * @return Event ArrayList
     * @throws DataAccessException Database Error
     */
    public ArrayList<Event> getEvents(String AssociatedUsername) throws DataAccessException {
        // Prepare sql query
        ArrayList<Event> events = new ArrayList<>();
        Event event;
        ResultSet set;
        String query = "SELECT * FROM Events WHERE AssociatedUsername = ?;";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Fill in the ?'s
            statement.setString(1, AssociatedUsername);
            // get query results
            set = statement.executeQuery();
            // If there is a results (There could be multiple)
            while(set.next()) {
                event = new Event(set.getString("EventID"), set.getString("AssociatedUsername"),
                        set.getString("PersonID"), set.getFloat("Latitude"), set.getFloat("Longitude"),
                        set.getString("Country"), set.getString("City"), set.getString("EventType"),
                        set.getInt("Year"));

                events.add(event);
            }
            // If there are no events associated with that username, return null
            if (events.size() == 0) {
                return null;
            }
            return events;

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error while finding a Events with that ID in the database");
        }
    }

    /**
     * Given a username, Clears every Event from that associated username the Events Table
     * @param AssociatedUsername String Associated Username
     * @throws DataAccessException Database Error
     */
    public void clearUserEvents(String AssociatedUsername) throws DataAccessException {
        // Prepare sql query
        String query = "DELETE FROM Events WHERE AssociatedUsername = ?;";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Fill in the ?'s
            statement.setString(1, AssociatedUsername);
            // Run Query
            statement.executeUpdate();

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error while clearing the persons table");
        }
    }

    /**
     * Clears every Event from every user in the Events Table
     * @throws DataAccessException Database Error
     */
    public void clear() throws DataAccessException {
        // Prepare sql query
        String query = "DELETE FROM Events";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Run Statement
            statement.executeUpdate();

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error while clearing the event table");
        }
    }
}
