package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoadRequest;
import result.Result;
import service.LoadService;

import java.io.*;
import java.net.HttpURLConnection;

public class LoadHandler implements HttpHandler {
    /**
     * 1) Clears all data from the database (just like the /clear API)
     * 2) Loads the user, person, and event data from the request body into the database.
     * @param exchange HttpExchange exchange
     * @throws IOException File Input/Output Error
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {
            // Verify request is POST
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                // Initialize Variables
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                // Convert request from JSON to GSON format
                LoadRequest request = gson.fromJson(reqData, LoadRequest.class);
                // Process Request
                LoadService service = new LoadService();
                Result result = service.load(request);
                // If successful return 200
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(result, resBody);
                resBody.close();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                exchange.getResponseBody().close();
                success = true;
            }
            // If unsuccessful, return 400
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
        }
    }

    /**
     * Reads string from InputStream
     * @param is Input Stream
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

