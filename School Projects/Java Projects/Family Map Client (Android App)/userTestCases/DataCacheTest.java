package userTestCases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wkp23.familymapclient.DataCache;
import com.wkp23.familymapclient.HttpClient;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.AuthToken;
import model.Event;
import model.Person;
import request.EventRequest;
import request.LoginRequest;
import request.PersonRequest;
import result.EventsResult;
import result.LoginResult;
import result.PersonResult;

public class DataCacheTest {

    // Server Data
    private static final HttpClient client = new HttpClient();
    private static final DataCache dataCache = DataCache.getInstance();
    private static final String serverHost = "localhost";
    private static final String serverPort = "8080";

    // Useful Data
    private String personID;

    private static File getFileFromPath(Object obj, String fileName) {
        ClassLoader classLoader = obj.getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return new File(resource.getPath());
    }

    @BeforeClass
    public static void clearServer() {
        AuthToken authToken = null;

        // Load the Server (Which also clears it)
        try {
            URL loadURL = new URL("http://" + serverHost + ":" + serverPort + "/load");
            HttpURLConnection connection = (HttpURLConnection) loadURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // get JSON file data
            URL fileData = DataCacheTest.class.getClassLoader().getResource("loadData.json");
            byte[] bytes = Files.readAllBytes(Paths.get(fileData.toURI()));
            String jsonOutString = new String(bytes);

            // Set the body of the Request
            try(OutputStream os = connection.getOutputStream()) {
                byte[] output = jsonOutString.getBytes(StandardCharsets.UTF_8);
                os.write(output, 0, output.length);
            }

            // Make the Request
            connection.connect();

            assertEquals(connection.getResponseCode(), HttpURLConnection.HTTP_OK);

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        // Login the user to load the data
        try {
            URL loginURL = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            // Login user1
            LoginRequest loginRequest = new LoginRequest("wako", "Password");

            LoginResult result = (LoginResult) client.loginUser(loginURL, loginRequest);

            authToken = new AuthToken(result.getAuthToken(), result.getUsername());

            DataCache.setAuthToken(authToken);
            DataCache.setPersonID(result.getPersonID());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Fill the People Data
        try {
            URL peopleURL = new URL("http://" + serverHost + ":" + serverPort + "/person");

            // Get user1's people & put in DataCache
            PersonRequest personRequest = new PersonRequest(authToken);
            PersonResult result = (PersonResult) client.userPeople(peopleURL, personRequest);

            DataCache.setAllPeople(Arrays.asList(((PersonResult) result).getData()));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Fill the Event Data
        try {
            URL eventsURL = new URL("http://" + serverHost + ":" + serverPort + "/event");

            // Get user1's people & put in DataCache
            EventRequest eventsRequest = new EventRequest(authToken);
            EventsResult result = (EventsResult) client.userEvents(eventsURL, eventsRequest);

            DataCache.setAllEvents(Arrays.asList(((EventsResult) result).getData()));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Initiate Settings
        DataCache.updateSettings();
    }

    /**
     * Verify the Preclass Data Load Worked
     */
    @Test
    public void verifyDataLoad() {
        assertEquals(8, DataCache.getAllPeople().size());
        assertEquals(16, DataCache.getAllEvents().size());
    }

    /**
     * Make sure that DataCache Calculates Family Relationships Sucessfully
     */
    @Test
    public void familyRelationshipPass(){
        // Verify Relationship containing, Father, Mother, Spouse and Child
        Person person = DataCache.getPersonFromMap("wpendl1");
        Map<String, Person> fullRelationship = DataCache.getPersonFamily(person);
        assertEquals(4, fullRelationship.size());
        assertEquals("wpendl", fullRelationship.get("Father").getPersonID());
        assertEquals("mpendl", fullRelationship.get("Mother").getPersonID());
        assertEquals("tinkbuck", fullRelationship.get("Spouse").getPersonID());
        assertEquals("wkp23", fullRelationship.get("Child").getPersonID());

        // Verify Relationship containing Father, Mother, Spouse
        person = DataCache.getPersonFromMap("wkp23");
        Map<String, Person> mostlyRelationship = DataCache.getPersonFamily(person);
        assertEquals(3, mostlyRelationship.size());
        assertEquals("wpendl1", mostlyRelationship.get("Father").getPersonID());
        assertEquals("tinkbuck", mostlyRelationship.get("Mother").getPersonID());
        assertEquals("spendl", mostlyRelationship.get("Spouse").getPersonID());

        // Verify Relationship containing Spouse, Child
        person = DataCache.getPersonFromMap("wpendl");
        Map<String, Person> halfRelationship = DataCache.getPersonFamily(person);
        assertEquals(2, halfRelationship.size());
        assertEquals("mpendl", halfRelationship.get("Spouse").getPersonID());
        assertEquals("wpendl1", halfRelationship.get("Child").getPersonID());
    }

    /**
     * Make sure that DataCache returns null when given an invalid person
     */
    @Test
    public void familyRelationshipFail(){
        Person person = new Person("dum", "dummy", "Dum", "Dummerton", "m", "sirdum", "mademdum", null);
        assertNull(DataCache.getPersonFamily(person));
    }

    /**
     * Make sure that DataCache settings are set to the proper defaults and work as expected function
     */
    @Test
    public void settingsFamilySidePass(){
        // Verify Father's Side
        // Verify Father's Side default is on
        List<Event> activeEvents = DataCache.getActiveEvents();
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("wpendl1-1")));
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("mpendl-1")));

        // Verify Father's Side Works when turned off
        DataCache.setSetting("Father's Side", false);
        DataCache.updateSettings();
        activeEvents = DataCache.getActiveEvents();
        assertFalse(activeEvents.contains(DataCache.getEventFromMap("wpendl1-1")));
        assertFalse(activeEvents.contains(DataCache.getEventFromMap("mpendl-1")));

        // Verify Father's Side Works when turned on again
        DataCache.setSetting("Father's Side", true);
        DataCache.updateSettings();
        activeEvents = DataCache.getActiveEvents();
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("wpendl1-1")));
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("mpendl-1")));


        // Verify Mother's Side
        // Verify Mother's Side default is on
        activeEvents = DataCache.getActiveEvents();
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("tinkbuck-1")));
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("vroos-1")));

        // Verify Mother's Side Works when turned off
        DataCache.setSetting("Mother's Side", false);
        DataCache.updateSettings();
        activeEvents = DataCache.getActiveEvents();
        assertFalse(activeEvents.contains(DataCache.getEventFromMap("tinkbuck-1")));
        assertFalse(activeEvents.contains(DataCache.getEventFromMap("vroos-1")));

        // Verify Mother's Side Works when turned on again
        DataCache.setSetting("Mother's Side", true);
        DataCache.updateSettings();
        activeEvents = DataCache.getActiveEvents();
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("tinkbuck-1")));
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("vroos-1")));
    }

    /**
     * Make sure that DataCache returns null when given an invalid person to get chronological events for
     */
    @Test
    public void settingsGenderPass(){
        // Verify Male Events
        // Verify Male Events default is on
        List<Event> activeEvents = DataCache.getActiveEvents();
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("wpendl-1")));
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("vroos-1")));

        // Verify Male Events when turned off
        DataCache.setSetting("Male Events", false);
        DataCache.updateSettings();
        activeEvents = DataCache.getActiveEvents();
        assertFalse(activeEvents.contains(DataCache.getEventFromMap("wpendl-1")));
        assertFalse(activeEvents.contains(DataCache.getEventFromMap("vroos-1")));

        // Verify Male Events when turned on again
        DataCache.setSetting("Male Events", true);
        DataCache.updateSettings();
        activeEvents = DataCache.getActiveEvents();
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("wpendl-1")));
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("vroos-1")));


        // Verify Female Events
        // Verify Female Events default is on
        activeEvents = DataCache.getActiveEvents();
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("mpendl-1")));
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("troos-1")));

        // Verify Female Events when turned off
        DataCache.setSetting("Female Events", false);
        DataCache.updateSettings();
        activeEvents = DataCache.getActiveEvents();
        assertFalse(activeEvents.contains(DataCache.getEventFromMap("mpendl-1")));
        assertFalse(activeEvents.contains(DataCache.getEventFromMap("troos-1")));

        // Verify Female Events when turned on again
        DataCache.setSetting("Female Events", true);
        DataCache.updateSettings();
        activeEvents = DataCache.getActiveEvents();
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("mpendl-1")));
        assertTrue(activeEvents.contains(DataCache.getEventFromMap("troos-1")));
    }

    /**
     * Make sure that DataCache Can sort the events in chronological order
     */
    @Test
    public void chronologicalOrderPass(){
        // Verify Order of Person with Many Events
        ArrayList<Event> orderedEvents = DataCache.getOrderedPersonEvents("wkp23");
        assertEquals("wkp23-1", orderedEvents.get(0).getEventID());
        assertEquals("wkp23-2", orderedEvents.get(1).getEventID());
        assertEquals("wkp23-3", orderedEvents.get(2).getEventID());
        assertEquals("wkp23-4", orderedEvents.get(3).getEventID());
        assertEquals("wkp23-5", orderedEvents.get(4).getEventID());

        // Verify Order of Person with a single Event
        orderedEvents = DataCache.getOrderedPersonEvents("wpendl");
        assertEquals("wpendl-1", orderedEvents.get(0).getEventID());
    }

    /**
     * Make sure that DataCache returns null when given an invalid person to get chronological events for
     */
    @Test
    public void chronologicalOrderFail(){
        Person person = new Person("dum", "dummy", "Dum", "Dummerton", "m", "sirdum", "mademdum", null);
        assertNull(DataCache.getPersonFamily(person));
    }

    /**
     * Make sure that DataCache returns a full and accurate search that contains a search stirng
     */
    @Test
    public void searchPass(){
        // Check results after searching for a single letter
        ArrayList<Person> peopleResults = DataCache.getPeopleContaining("i");
        assertEquals(6, peopleResults.size());
        assertTrue(peopleResults.contains(DataCache.getPersonFromMap("wkp23")));
        assertTrue(peopleResults.contains(DataCache.getPersonFromMap("spendl")));
        assertTrue(peopleResults.contains(DataCache.getPersonFromMap("wpendl1")));
        assertTrue(peopleResults.contains(DataCache.getPersonFromMap("tinkbuck")));
        assertTrue(peopleResults.contains(DataCache.getPersonFromMap("wpendl")));
        assertTrue(peopleResults.contains(DataCache.getPersonFromMap("vroos")));

        ArrayList<Event> eventsResults = DataCache.getEventsContaining("j");
        assertEquals(2, eventsResults.size());
        assertTrue(eventsResults.contains(DataCache.getEventFromMap("spendl-1")));
        assertTrue(eventsResults.contains(DataCache.getEventFromMap("troos-1")));

        // Check results after searching for a multiple letters
        peopleResults = DataCache.getPeopleContaining("ill");
        assertEquals(3, peopleResults.size());
        assertTrue(peopleResults.contains(DataCache.getPersonFromMap("wkp23")));
        assertTrue(peopleResults.contains(DataCache.getPersonFromMap("wpendl1")));
        assertTrue(peopleResults.contains(DataCache.getPersonFromMap("wpendl")));

        eventsResults = DataCache.getEventsContaining("birth");
        assertEquals(3, eventsResults.size());
        assertTrue(eventsResults.contains(DataCache.getEventFromMap("wkp23-1")));
        assertTrue(eventsResults.contains(DataCache.getEventFromMap("spendl-1")));
        assertTrue(eventsResults.contains(DataCache.getEventFromMap("wpendl1-1")));

    }

    /**
     * Make sure that DataCache returns an Empty List when given an blank search query
     */
    @Test
    public void searchFail(){
        assertEquals(new ArrayList<>(), DataCache.getPeopleContaining(null));
        assertEquals(new ArrayList<>(), DataCache.getEventsContaining(null));
    }
}
