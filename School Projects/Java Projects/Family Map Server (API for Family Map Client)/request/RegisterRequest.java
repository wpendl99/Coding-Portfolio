package request;

/**
 * Register Request Object for putting User in Database
 */
public class RegisterRequest {
    String username;
    String password;
    String email;
    String firstName;
    String lastName;
    String gender;
    boolean success;

    /**
     * Constructor for the Register Request of a User
     * @param username Unique String Username of the User
     * @param password String Password of the User
     * @param email String Email of the User
     * @param firstName String First Name of the User
     * @param lastName String Last Name of the User
     * @param gender String Genderr  of the User
     */
    public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    /**
     * Returns the Username of the Register Request Object
     * @return String Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the Username of the Register Request Object
     * @param username String Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the Password of the Register Request Object
     * @return String Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the Password of the Register Request Object
     * @param password String Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the Email of the Register Request Object
     * @return String Email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the Email of the Register Request Object
     * @param email String Email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the First name of the Register Request Object
     * @return String First name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the First name of the Register Request Object
     * @param firstName String First name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the Last name of the Register Request Object
     * @return String Last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the Last name of the Register Request Object
     * @param lastName String Last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the Gender of the Register Request Object
     * @return String Gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the Gender of the Register Request Object
     * @param gender String Gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
}
