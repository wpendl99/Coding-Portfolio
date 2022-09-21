package DataAccess;

import model.User;
import rescources.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private final Connection conn;

    public UserDao(Connection conn){
        this.conn = conn;
    }

    /**
     * Given a User object, INSERTs into Users table
     * @param user User user
     * @throws DataAccessException Database Error
     */
    public void registerUser(User user) throws DataAccessException{
        // Prepare SQL query
        String query = "INSERT INTO Users (PersonID, Username, Password, Email, FirstName, LastName, Gender" +
                ") VALUES(?,?,?,?,?,?,?)";

        try(PreparedStatement statement = conn.prepareStatement(query)) {

            // Set the '?'s of the query
            statement.setString(1, user.getPersonID());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getFirstName());
            statement.setString(6, user.getLastName());
            statement.setString(7, user.getGender());

            // Run the Query
            statement.executeUpdate();

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error encountered while inserting a user into the database");
        }
    }

    /**
     * Given a username, returns corresponding User object from Users Table
     * @param username String Username
     * @return User user
     * @throws DataAccessException Database Error
     */
    public User loginUser(String username) throws DataAccessException{
        // Prepare SQL Query
        User user;
        ResultSet set;
        String query = "SELECT * FROM Users WHERE Username = ?;";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Fill in the ?'s
            statement.setString(1, username);
            // Get the Results
            set = statement.executeQuery();
            // If there are any results (Should only be one) return User
            if (set.next()) {
                user = new User(set.getString("Username"), set.getString("Password"), set.getString("Email"), set.getString("FirstName"), set.getString("LastName"), set.getString("Gender"), set.getString("PersonID"));

                return user;

            // If there were no found users, return null
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
     * Clears every User from the Users Table
     * @throws DataAccessException Database Error
     */
    public void clearUsers() throws DataAccessException {
        // Prepare SQL Query
        String query = "DELETE FROM Users";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Run Query
            statement.executeUpdate();

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error while clearing the users table");
        }
    }
}
