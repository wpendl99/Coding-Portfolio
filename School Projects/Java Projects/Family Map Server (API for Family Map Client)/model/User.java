package model;

/**
 * User Object
 */
public class User {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    /**
     * A User Object containing the information of a user
     * @param username Unique String Username of the User
     * @param password String Password of the User
     * @param email String Email of the User
     * @param firstName String First Name of the User
     * @param lastName String Last Name of the User
     * @param gender String Gender of the User
     * @param personID String PersonID of the User
     */
    public User(String username, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    /**
     * Returns the Username of the User
     * @return String Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the Username of the User
     * @param username String Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the Password of the User
     * @return String Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the Password of the User
     * @param password String Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the Email of the User
     * @return String Email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the Email of the User
     * @param email String Email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the First name of the User
     * @return String First name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the First name of the User
     * @param firstName String First name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the Last name of the User
     * @return String Last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the Last name of the User
     * @param lastName String Last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the gender of the User
     * @return String gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the Gender of the User
     * @param gender String Gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Returns the PersonID of the User
     * @return String PersonID
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Sets the PersonID of the User
     * @param personID String PersonID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
