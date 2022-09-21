package DataAccess;

import model.AuthToken;
import rescources.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenDao {
    private final Connection conn;

    public AuthTokenDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Given an AuthToken object, this will REPLACE any authtoken for a user in AuthToken Table
     * @param authToken AuthToken authToken
     * @throws DataAccessException Database Error
     */
    public void registerAuthToken(AuthToken authToken) throws DataAccessException{
        // Prepare sql query
        String query = "REPLACE INTO Authtoken (AuthToken, Username) VALUES(?,?)";

        try(PreparedStatement statement = conn.prepareStatement(query)) {

            // Set the '?'s of the query
            statement.setString(1, authToken.getAuthtoken());
            statement.setString(2, authToken.getUsername());

            // If all worked, send statement
            statement.executeUpdate();

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error encountered while registering an authtoken into the database");
        }
    }

    /**
     * When given just the token string, it will return a given AuthToken Object from the AuthToken Table
     * @param token String Authorization Token
     * @return AuthToken
     * @throws DataAccessException Database Error
     */
    public AuthToken verifyAuthToken(String token) throws DataAccessException{
        // Prepare SQL query
        AuthToken authToken;
        ResultSet set;
        String query = "SELECT * FROM Authtoken WHERE AuthToken = ?;";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Fill in the ?'s
            statement.setString(1, token);
            // get query results
            set = statement.executeQuery();
            // If there is a results (There should only be one)
            if (set.next()) {
                authToken = new AuthToken(set.getString("AuthToken"), set.getString("Username"));
                // Return Authtoken Object
                return authToken;
            // If the token doesn't exist return null
            } else {
                return null;
            }

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error while verifying an authtoken in the database");
        }
    }

    /**
     * Clears every token/user from the AuthToken Table
     * @throws DataAccessException Database Error
     */
    public void clearAuthTokens() throws DataAccessException {
        // Prepare sql query
        String query = "DELETE FROM Authtoken";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Run statement
            statement.executeUpdate();

        // If there was an error with the SQL query (typically database error), this statement catches it and prints it
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("There was an error while clearing the authtoken table");
        }
    }
}
