package userTestCases;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

import com.wkp23.familymapclient.DataCache;
import com.wkp23.familymapclient.HttpClient;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import static org.hamcrest.CoreMatchers.instanceOf;

import model.AuthToken;
import request.EventRequest;
import request.LoginRequest;
import request.PersonRequest;
import request.RegisterRequest;
import result.ErrorResult;
import result.EventsResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;
import result.Result;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class HttpClientTest {

    // Server Data
    private final HttpClient client = new HttpClient();
    private final String serverHost = "localhost";
    private final String serverPort = "8080";

    //User Data
    private final String user1username = "test1";
    private final String user1password = "pa22w0rd";
    private final String user1email = "email@email.com";
    private final String user1firstName = "dummy";
    private final String user1lastName = "dummerton";
    private final String user1gender = "m";
    private final String user2username = "test2";
    private final String user2password = "drowssap";
    private final String user2fakeAuth = "7E";
    private final String user2firstName = "dumette";
    private final String user2lastName = "dummerton";
    private final String user2gender = "f";

    // URL goodURL = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
    // URL badURL = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

    @Before
    public void clearServer() {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/clear");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            connection.connect();

            assertEquals(connection.getResponseCode(), HttpURLConnection.HTTP_OK);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make sure you can successfully register a user
     */
    @Test
    public void registerUserPass() {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            RegisterRequest request = new RegisterRequest(user1username, user1password, user1email, user1firstName, user1lastName, user1gender);

            assertThat(client.registerUser(url, request), instanceOf(RegisterResult.class));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make sure you can't register twice
     */
    @Test
    public void registerUserFail() {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            RegisterRequest request = new RegisterRequest(user1username, user1password, user1email, user1firstName, user1lastName, user1gender);

            // register user1 the first time
            assertThat(client.registerUser(url, request), instanceOf(RegisterResult.class));

            // Try registering user1 again
            assertNull(client.registerUser(url, request));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make sure you can successfully login a user after registering them
     */
    @Test
    public void loginUserPass() {
        try {
            URL registerURL = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            URL loginURL = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            // Register user1
            RegisterRequest registerRequest = new RegisterRequest(user1username, user1password, user1email, user1firstName, user1lastName, user1gender);

            assertThat(client.registerUser(registerURL, registerRequest), instanceOf(RegisterResult.class));

            // Login user1
            LoginRequest loginRequest = new LoginRequest(user1username, user1password);

            assertThat(client.loginUser(loginURL, loginRequest), instanceOf(LoginResult.class));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make sure you can't login without registering
     */
    @Test
    public void loginUserFail() {
        try {
            URL registerURL = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            URL loginURL = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            // Register user1
            RegisterRequest registerRequest = new RegisterRequest(user1username, user1password, user1email, user1firstName, user1lastName, user1gender);

            assertThat(client.registerUser(registerURL, registerRequest), instanceOf(RegisterResult.class));

            // Login user2
            LoginRequest loginRequest = new LoginRequest(user2username, user2password);

            assertNull(client.loginUser(loginURL, loginRequest));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make sure you can successfully get a user's people
     */
    @Test
    public void userPeoplePass() {
        try {
            URL registerURL = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            URL peopleURL = new URL("http://" + serverHost + ":" + serverPort + "/person");
            // Register user1
            RegisterRequest registerRequest = new RegisterRequest(user1username, user1password, user1email, user1firstName, user1lastName, user1gender);

            Result registerResult = client.registerUser(registerURL, registerRequest);
            assertThat(registerResult, instanceOf(RegisterResult.class));

            // Get user1's people
            AuthToken authToken = new AuthToken(((RegisterResult) registerResult).getAuthToken(), ((RegisterResult) registerResult).getUsername());
            PersonRequest personRequest = new PersonRequest(authToken);

            assertThat(client.userPeople(peopleURL, personRequest), instanceOf(PersonResult.class));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make sure you can't get other peoples people
     */
    @Test
    public void userPeopleFail() {
        try {
            URL registerURL = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            URL peopleURL = new URL("http://" + serverHost + ":" + serverPort + "/person");
            // Register user1
            RegisterRequest registerRequest = new RegisterRequest(user1username, user1password, user1email, user1firstName, user1lastName, user1gender);

            Result registerResult = client.registerUser(registerURL, registerRequest);
            assertThat(registerResult, instanceOf(RegisterResult.class));

            // Get user2's people
            AuthToken authToken = new AuthToken(user2fakeAuth, user2username);
            PersonRequest personRequest = new PersonRequest(authToken);

            assertNull(client.userPeople(peopleURL, personRequest));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make sure you can successfully get a user's people
     */
    @Test
    public void userEventsPass() {
        try {
            URL registerURL = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            URL eventsURL = new URL("http://" + serverHost + ":" + serverPort + "/event");
            // Register user1
            RegisterRequest registerRequest = new RegisterRequest(user1username, user1password, user1email, user1firstName, user1lastName, user1gender);

            Result registerResult = client.registerUser(registerURL, registerRequest);
            assertThat(registerResult, instanceOf(RegisterResult.class));

            // Get user1's people
            AuthToken authToken = new AuthToken(((RegisterResult) registerResult).getAuthToken(), ((RegisterResult) registerResult).getUsername());
            EventRequest eventsRequest = new EventRequest(authToken);

            assertThat(client.userEvents(eventsURL, eventsRequest), instanceOf(EventsResult.class));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make sure you can't get other peoples people
     */
    @Test
    public void userEventsFail() {
        try {
            URL registerURL = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            URL eventsURL = new URL("http://" + serverHost + ":" + serverPort + "/event");
            // Register user1
            RegisterRequest registerRequest = new RegisterRequest(user1username, user1password, user1email, user1firstName, user1lastName, user1gender);

            Result registerResult = client.registerUser(registerURL, registerRequest);
            assertThat(registerResult, instanceOf(RegisterResult.class));

            // Get user2's people
            AuthToken authToken = new AuthToken(user2fakeAuth, user2username);
            EventRequest eventsRequest = new EventRequest(authToken);

            assertNull(client.userEvents(eventsURL, eventsRequest));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
