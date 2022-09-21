package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.Result;
import service.ClearService;

import java.io.*;
import java.net.HttpURLConnection;


public class ClearHandler implements HttpHandler {

    /**
     * Creates a new user account (row in database)
     * Generates 4 generations of ancestor data for the new user (just like the /fill endpoint if called with a generations value of 4 and this new user’s username as parameters)
     * @param exchange HttpExchange exchange
     * @throws IOException File input/output Error
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Set success to false at start
        boolean success = false;

        try {
            // Check to see if request is POST
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                // Set Variables
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                ClearService service = new ClearService();

                // Run Clear Request
                Result result = service.clear();

                // If Successful, return 200
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(result, resBody);
                resBody.close();

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                exchange.getResponseBody().close();
                success = true;
            }

            // If Unsuccessful, return 400
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }

        // If any errors, return 500
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    /**
     * Read String from InputStream
     * @param is InputStream stream
     * @return String string
     * @throws IOException File Input/Output Error
     */
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}

