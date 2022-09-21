package service;

import DataAccess.EventDao;
import DataAccess.PersonDao;
import DataAccess.UserDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.*;
import request.FillRequest;
import rescources.Database;
import result.ErrorResult;
import result.FillResult;
import result.Result;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class FillService {
    ArrayList<Person> family;
    ArrayList<Event> events;
    int totalGenerations;

    public FillService(){
        this.family = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    /**
     * Populates the server's database with generated data for the specified username. The required "username" parameter must be a user already registered with the server. If there is any data in the database already associated with the given username, it is deleted.
     * The optional "generations" parameter lets the caller specify the number of generations of ancestors to be generated, and must be a non-negative integer (the default is 4, which results in 31 new persons each with associated events).
     * More details can be found in the earlier section titled “Family History Information Generation”
     * @param fillRequest FillRequest request
     * @return Result result
     */
    public Result fill(FillRequest fillRequest) {
        // Gets the username and generations from request
        String username = fillRequest.getUsername();
        int generations = fillRequest.getGenerations();
        totalGenerations = generations;
        Database db = new Database();
        try {
            System.out.println("Computing Fill Request");

            db.openConnection();

            // Clear Previous Persons and get user
            new PersonDao(db.getConnection()).clearUserPersons(username);
            new EventDao(db.getConnection()).clearUserEvents(username);
            User user = new UserDao(db.getConnection()).loginUser(username);

            // Generate User's Person & family
            Person person = generateFamily(user, generations);

            // Add Family to DB
            for(Person member: family){
                new PersonDao(db.getConnection()).fillPerson(member);
            }

            // Add Events to DB
            for(Event event: events){
                new EventDao(db.getConnection()).insert(event);
            }

            // Generate message
            String message = "Successfully added " + family.size() + " persons and " + events.size() + " Events to the database.";

            // Record and Return
            FillResult result = new FillResult(message, true);
            db.closeConnection(true);
            System.out.println("Filling of " + user.getUsername() + " successfully completed");
            System.out.println(message);
            return result;
        // If there was an error, record and return
        } catch (Exception e){
            db.closeConnection(false);
            e.printStackTrace();
            return new ErrorResult(e.getMessage(), false);
        }
    }

    // Used as a starting function to generate a family tree like structure similar to the spelling corrector assignment.
    // This one generates the initial users Person (every User gets one
    private Person generateFamily(User user, Integer generations){
        // Initialize User data
        String personID = user.getPersonID();
        String associatedUsername = user.getUsername();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String gender = user.getGender();
        String spouseID = null;
        String fatherID;
        String motherID;

        // Process different generations (if any)
        if(generations == 0){
            fatherID = null;
            motherID = null;
        } else {
            String[] parents = getParents(user, generations - 1);
            fatherID = parents[0];
            motherID = parents[1];
        }

        // Creates initial User Person and puts in list for adding to Database
        Person person = new Person(personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID);
        family.add(person);

        // Generates 3 random events for the User Person
        generateRandomEvent(person, "Birth", generations);

        return person;
    }

    // User to generate the two Persons representing mother and father for any Person
    private String[] getParents(User user, Integer generations){
        // Father stats
        String F_personID = generateRandomUUID();
        String F_associatedUsername = user.getUsername();
        String F_firstName = getRandomMname();
        String F_lastName = getRandomSname();
        String F_gender = "m";
        String F_spouseID;
        String F_fatherID;
        String F_motherID;

        // Mother stats
        String M_personID = generateRandomUUID();
        String M_associatedUsername = user.getUsername();
        String M_firstName = getRandomFname();
        String M_lastName = F_lastName;
        String M_gender = "f";
        String M_spouseID;
        String M_fatherID;
        String M_motherID;

        // Link Spouses
        F_spouseID = M_personID;
        M_spouseID = F_personID;

        // Get More Family
        if(generations == 0){
            F_fatherID = null;
            F_motherID = null;
            M_fatherID = null;
            M_motherID = null;
        } else {
            String[] parents = getParents(user, generations - 1);
            F_fatherID = parents[0];
            F_motherID = parents[1];

            parents = getParents(user, generations - 1);
            M_fatherID = parents[0];
            M_motherID = parents[1];
        }

        // Create Person objects for mother and father and add to list to be added to database
        Person father = new Person(F_personID, F_associatedUsername, F_firstName, F_lastName, F_gender, F_fatherID, F_motherID, F_spouseID);
        Person mother = new Person(M_personID, M_associatedUsername, M_firstName, M_lastName, M_gender, M_fatherID, M_motherID, M_spouseID);
        family.add(father);
        family.add(mother);

        //Generate 3 random events for each parent
        for(String eventType : new String[] {"Birth", "Marriage", "Death"}){
            generateRandomEvent(father, eventType, generations);
            // Make sure to link the marriage dates and places
            if(!eventType.equals("Marriage")){
                generateRandomEvent(mother, eventType, generations);
            }
        }

        return new String[] {father.getPersonID(), mother.getPersonID()};
    }

    /**
     * Generates Random UUID String 8 chars long
     * @return UUID String Unique UUID
     */
    private String generateRandomUUID(){
        return UUID.randomUUID().toString().substring(0,8);
    }

    // Get a random female name from the snames json
    private String getRandomFname() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Reader reader = new FileReader("json/fnames.json");

            fnameData fnames = gson.fromJson(reader, fnameData.class);

            int rnd = new Random().nextInt(fnames.data.length);
            return fnames.data[rnd];

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // Gets a random male name from the mnames json
    private String getRandomMname(){
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Reader reader = new FileReader("json/mnames.json");

            mnameData mnames = gson.fromJson(reader, mnameData.class);

            int rnd = new Random().nextInt(mnames.data.length);
            return mnames.data[rnd];

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // Gets a random surname name from the snames json
    private String getRandomSname(){
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Reader reader = new FileReader("json/snames.json");

            snameData snames = gson.fromJson(reader, snameData.class);

            int rnd = new Random().nextInt(snames.data.length);
            return snames.data[rnd];

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // generates a random event for a Person Object to be added in the Event database
    private void generateRandomEvent(Person person, String eventType, int generation){
        String eventID = generateRandomUUID();
        String associatedUsername = person.getAssociatedUsername();
        String personID = person.getPersonID();

        Location location =  getRandomLocation();

        assert location != null;
        float latitude = Float.parseFloat(location.latitude);
        float longitude = Float.parseFloat(location.longitude);
        String country = location.country;
        String city = location.city;
        int year;
        if(Objects.equals(eventType, "Birth")){
            year = 2000 - 25*(totalGenerations - generation);
        } else if (Objects.equals(eventType, "Marriage")){
            year =  2025 - 25*(totalGenerations - generation);
        } else if (Objects.equals(eventType, "Death")) {
            year = 2050 - 25 * (totalGenerations - generation);
        }  else {
            year = -1;
        }

        events.add(new Event(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year));
        if(Objects.equals(eventType, "Marriage")){
            events.add(new Event(generateRandomUUID(), associatedUsername, person.getSpouseID(), latitude, longitude, country, city, eventType, year));
        }
    }

    // Gets a random location from the location json
    private Location getRandomLocation(){
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Reader reader = new FileReader("json/locations.json");

            LocationData locations = gson.fromJson(reader, LocationData.class);

            int rnd = new Random().nextInt(locations.data.length);
            return locations.data[rnd];

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
