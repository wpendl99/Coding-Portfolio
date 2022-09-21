package handlers;

import DataAccess.AuthTokenDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.AuthToken;
import request.PersonRequest;
import rescources.Database;
import result.ErrorResult;
import result.Result;
import service.PersonService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PersonHandler implements HttpHandler {
    /**
     * Does one of two things:
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
            // Verify request is GET
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                System.out.println("New Person Request");

                db.openConnection();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Headers reqHeaders = exchange.getRequestHeaders();
                // Verify AuthToken
                if (reqHeaders.containsKey("Authorization")) {

                    String token = reqHeaders.getFirst("Authorization");
                    AuthToken authToken = new AuthTokenDao(db.getConnection()).verifyAuthToken(token);
                    db.closeConnection(true);
                    // If given a valid AuthToken
                    if(authToken != null) {
                        if (Objects.equals(authToken.getAuthtoken(), token)) {

                            String urlPath = exchange.getRequestURI().toString();

                            String[] arr = urlPath.replaceAll("^/", "").split("/");
                            ArrayList<String> list = new ArrayList<>(List.of(arr));

                            System.out.println(list);

                            PersonService service = new PersonService();
                            PersonRequest personRequest = new PersonRequest();
                            personRequest.setAuthToken(authToken);
                            Result result;
                            if (list.size() == 1) {
                                System.out.println("Request to grab all persons");
                                result = service.persons(personRequest);
                            } else {
                                System.out.println("Request to grab one person");
                                personRequest.setPersonID(list.get(1));
                                result = service.person(personRequest);
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
                    // If given invalid AuthToken
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
            // If unsuccessful, return 400
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        // If error, return 500
        } catch (Exception e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    /**
     * Gets the string from the OutputStream
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

