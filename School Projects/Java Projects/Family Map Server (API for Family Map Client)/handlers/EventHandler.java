package handlers;

import DataAccess.AuthTokenDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.AuthToken;
import request.EventRequest;
import rescources.Database;
import result.ErrorResult;
import result.Result;
import service.EventService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventHandler implements HttpHandler {

    /**
     * Depending on URL Path of request either:
     * - Returns ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
     * - Returns the single Event object with the specified ID (if the event is associated with the current user). The current user is determined by the provided authtoken.
     * @param exchange HttpExchange exchange
     * @throws IOException File Input/Output Error
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Database db = new Database();
        boolean success = false;

        try {
            // Make sure request is a GET
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                // Initialize variables
                System.out.println("New Event Request");
                db.openConnection();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Headers reqHeaders = exchange.getRequestHeaders();

                // Verify AuthToken
                if (reqHeaders.containsKey("Authorization")) {
                    String token = reqHeaders.getFirst("Authorization");
                    AuthToken authToken = new AuthTokenDao(db.getConnection()).verifyAuthToken(token);
                    db.closeConnection(true);

                    // If the AuthToken is Valid, process request
                    if(authToken != null) {
                        if (Objects.equals(authToken.getAuthtoken(), token)) {
                            // Process URL Path
                            String urlPath = exchange.getRequestURI().toString();
                            String[] arr = urlPath.replaceAll("^/", "").split("/");
                            ArrayList<String> list = new ArrayList<>(List.of(arr));

                            System.out.println(list);

                            EventService service = new EventService();
                            EventRequest request = new EventRequest();
                            request.setAuthToken(authToken);
                            Result result;
                            // If request is for all Persons
                            if (list.size() == 1) {
                                System.out.println("Request to grab all persons");

                                result = service.events(request);

                            // If request is for a specific person
                            } else {
                                System.out.println("Request to grab one person");
                                request.setEventID(list.get(1));
                                result = service.event(request);
                            }

                            // Start sending the HTTP response to the client, starting with
                            // the status code and any defined headers.
                            if(result.getClass() != ErrorResult.class) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            } else {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            }
                            Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                            gson.toJson(result, resBody);
                            resBody.close();

                            success = true;
                        }

                    // If the AuthToken isn't valid, deny request and return 400
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                        gson.toJson(new ErrorResult("Error - Invalid AuthKey", false), resBody);
                        resBody.close();
                        exchange.getResponseBody().close();
                        success = true;
                    }
                }
            }

            // If Unsuccessful, return 400
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }

        // If there was an error, return 500
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        } catch (Exception e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    /**
     * Reads string from OutputStream
     * @param str String string
     * @param os OutputStream stream
     * @throws IOException File Input/Output Error
     */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}


