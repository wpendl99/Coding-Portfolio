package request;

/**
 * Fill Request Object for Filling (generating) random Persons and Events into respective Databases for a user
 */
public class FillRequest {
    public String username;
    public int generations;

    /**
     * Default Constructor
     */
    public FillRequest() {
    }

    /**
     * Preferred Constructor for generating a request to generating random Persons, and Events associated to a User
     * @param username String of Username
     * @param generations Integer of # of Generation to generate
     */
    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }

    /**
     * Returns the Username of the Fill Request
     * @return String Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the Username of the Fill Request
     * @param username String Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retunrs the # of generation if generate of the Fill Request
     * @return Integer of # of generations to generate
     */
    public int getGenerations() {
        return generations;
    }

    /**
     * Sets the # of generation if generate of the Fill Request
     * @param generations Integer of # of generations to generate
     */
    public void setGenerations(int generations) {
        this.generations = generations;
    }
}
