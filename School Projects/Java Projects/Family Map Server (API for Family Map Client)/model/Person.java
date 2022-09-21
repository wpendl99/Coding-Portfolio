package model;

/**
 * Person Object
 */
public class Person {
    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    /**
     * A Person Object containing person data that are assigned to a User
     * @param personID Unique String of PersonID of the person
     * @param associatedUsername String of the Associated Username of the person
     * @param firstName String of the First name of the person
     * @param lastName String of the Last name of the person
     * @param gender String of the Gender of the person
     * @param fatherID String of the FatherID of the person
     * @param motherID String of the MotherID of the person
     * @param spouseID String of the SpouseID of the person
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    /**
     * Returns the PersonID of the Person
     * @return String of PersonID
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Sets the PersonID of the Person
     * @param personID String of PersonID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * Returns the associated Username of the Person
     * @return String of the Associated Username
     */
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    /**
     * Sets the associated Username of the Person
     * @param associatedUsername String of the Associated Username
     */
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    /**
     * Returns the First name of the Person
     * @return String of First name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the First name of the person
     * @param firstName String of First Name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the Last name of the Person
     * @return String of Last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the Last name of the person
     * @param lastName String of First Name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the gender of a person
     * @return String of gender (m/f)
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of a  person
     * @param gender String of gender (m/f)
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Returns the FatherID of the Person
     * @return String of FatherID
     */
    public String getFatherID() {
        return fatherID;
    }

    /**
     * Sets the FatherId of the Person
     * @param fatherID String of FatherID
     */
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    /**
     * Returns the MotherID of the Person
     * @return String of MotherID
     */
    public String getMotherID() {
        return motherID;
    }

    /**
     * Sets the MotherID of the Person
     * @param motherID String of MotherID
     */
    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    /**
     * Returns the SpouseID of the Person
     * @return String of SpouseID
     */
    public String getSpouseID() {
        return spouseID;
    }

    /**
     * Sets the SpouseID of the Person
     * @param spouseID String of SpouseID
     */
    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }
}
