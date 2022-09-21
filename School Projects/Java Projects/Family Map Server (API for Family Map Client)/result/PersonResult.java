package result;

import model.Person;

/**
 * Person Result Object of an Person Request and Persons Request
 */
public class PersonResult extends Result {
    String associatedUsername;
    String personID;
    String firstName;
    String lastName;
    String gender;
    String fatherID;
    String motherID;
    String spouseID;
    Person[] data;
    boolean success;

    /**
     * Constructor of Persons Result Object
     * @param data Person Array
     * @param success Boolean Success Status
     */
    public PersonResult(Person[] data, boolean success) {
        this.data = data;
        this.success = success;
    }

    /**
     * Constructor of Person Result Object
     * @param person Person person
     * @param success Boolean Success Status
     */
    public PersonResult(Person person, boolean success) {
        this.associatedUsername = person.getAssociatedUsername();
        this.personID = person.getPersonID();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.gender = person.getGender();
        this.fatherID = person.getFatherID();
        this.motherID = person.getMotherID();
        this.spouseID = person.getSpouseID();
        this.success = success;
    }

    /**
     * Returns the Associated Username of the Person Result
     * @return String Associated Username
     */
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    /**
     * Sets the Associated Username of the Person Result
     * @param associatedUsername String Associated Username
     */
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    /**
     * Returns the PersonID of the Person Result
     * @return String PersonID
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Sets the PersonID of the Person Result
     * @param personID String PersonID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * Returns the First name of the Person Result
     * @return String First name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the First name of the Person Result
     * @param firstName String First name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the Last name of the Person Result
     * @return String Last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the Last name of the Person Result
     * @param lastName String Last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the Gender of the Person Result
     * @return String Gender (m/f)
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the Gender of the Person Result
     * @param gender String Gender (m/f)
     */
    public void setGender(String gender) {
        this.gender = gender;
    }


    /**
     * Returns the FatherID of the Person Result
     * @return String FatherID
     */
    public String getFatherID() {
        return fatherID;
    }

    /**
     * Sets the FatherID of the Person Result
     * @param fatherID String FatherID
     */
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    /**
     * Returns the MotherID of the Person Result
     * @return String MotherID
     */
    public String getMotherID() {
        return motherID;
    }

    /**
     * Sets the MotherID of the Person Result
     * @param motherID String MotherID
     */
    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    /**
     * Returns the SpouseID of the Person Result
     * @return String SpouseID
     */
    public String getSpouseID() {
        return spouseID;
    }

    /**
     * Sets the SpouseID of the Person Result
     * @param spouseID String SpouseID
     */
    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    /**
     * Returns the Array of Persons from the Persons Request
     * @return Person Array
     */
    public Person[] getData() {
        return data;
    }

    /**
     * Sets the Array of Persons from the Persons Request
     * @param data Person Array
     */
    public void setData(Person[] data) {
        this.data = data;
    }

    /**
     * Returns the Success Status of the Person Result
     * @return Boolean Success Status
     */
    public boolean getSuccess() {
        return success;
    }

    /**
     * Sets the Success Status of the Person Result
     * @param success Boolean Success Status
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
