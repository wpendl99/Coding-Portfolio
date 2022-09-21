package DataAccess;


import model.Person;
import rescources.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonDao {
    private final Connection conn;

    public PersonDao(Connection conn){
        this.conn = conn;
    }

    /**
     * Given a Person object, this will INSERT a person into the Persons Table
     * @param person Person person
     * @throws DataAccessException Database Exception
     */
    public void fillPerson(Person person) throws DataAccessException{
        // Prepare sql query
        String query = "INSERT INTO Persons (PersonID, AssociatedUsername, FirstName, LastName, Gender, " +
                "FatherID, MotherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";

        try(PreparedStatement statement = conn.prepareStatement(query)) {

            // Set the '?'s of the query
            statement.setString(1, person.getPersonID());
            statement.setString(2, person.getAssociatedUsername());
            statement.setString(3, person.getFirstName());
            statement.setString(4, person.getLastName());
            statement.setString(5, person.getGender());
            statement.setString(6, person.getFatherID());
            statement.setString(7, person.getMotherID());
            statement.setString(8, person.getSpouseID());

            // Run Query
            statement.executeUpdate();

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error encountered while inserting a person into the database");
        }
    }

    /**
     * Given a personID, returns corresponding Person Object from the Persons table
     * @param personID String PersonID
     * @return Person person
     * @throws DataAccessException Database Error
     */
    public Person getPerson(String personID) throws DataAccessException{
        // Prepare sql query
        Person person;
        ResultSet set;
        String query = "SELECT * FROM Persons WHERE PersonID = ?;";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Fill in the e?'s
            statement.setString(1, personID);
            // Get the Results
            set = statement.executeQuery();
            // If there is a results (There should only be one)
            if (set.next()) {
                person = new Person(set.getString("PersonID"), set.getString("AssociatedUsername"),
                        set.getString("FirstName"), set.getString("LastName"), set.getString("Gender"),
                        set.getString("FatherID"), set.getString("MotherID"), set.getString("SpouseID"));
                return person;
            // If the personID doesn't exist, return null
            } else {
                return null;
            }

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error while finding a Person with that ID in the database");
        }
    }

    /**
     * Given a username, returns an ArrayList of all the Person objects (if any) corresponding to the username from the Persons Table
     * @param AssociatedUsername String Associated Username
     * @return Person ArrayList
     * @throws DataAccessException Database Error
     */
    public ArrayList<Person> getPersons(String AssociatedUsername) throws DataAccessException {
        // Prepare SQL query
        ArrayList<Person> persons = new ArrayList<>();
        Person person;
        ResultSet set;
        String query = "SELECT * FROM Persons WHERE AssociatedUsername = ?;";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Fill in the ?'s
            statement.setString(1, AssociatedUsername);
            // Get the results
            set = statement.executeQuery();
            // If there are any results, create new Peron objects and add them to a list
            while (set.next()) {

                person = new Person(set.getString("PersonID"), set.getString("AssociatedUsername"),
                        set.getString("FirstName"), set.getString("LastName"), set.getString("Gender"),
                        set.getString("FatherID"), set.getString("MotherID"), set.getString("SpouseID"));

                persons.add(person);
            }
            // If there are no corresponding Persons, return null
            if(persons.size() == 0) {
                return null;
            }
            return persons;

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error while finding a Person with that ID in the database");
        }
    }

    /**
     * Given a username, Clears every corresponding Person from the Persons Table
     * @param AssociatedUsername String Associated Username
     * @throws DataAccessException Database Error
     */
    public void clearUserPersons(String AssociatedUsername) throws DataAccessException {
        // Prepare SQL query
        String query = "DELETE FROM Persons WHERE AssociatedUsername = ?;";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Fill in the ?'s
            statement.setString(1, AssociatedUsername);
            // Run the Query
            statement.executeUpdate();

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error while clearing the persons table");
        }
    }

    /**
     * Clears Every Person from every user in the Persons Table
     * @throws DataAccessException Database Error
     */
    public void clearPersons() throws DataAccessException {
        // Prepare the Query
        String query = "DELETE FROM Persons";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Run the Query
            statement.executeUpdate();

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error while clearing the persons table");
        }
    }
}
