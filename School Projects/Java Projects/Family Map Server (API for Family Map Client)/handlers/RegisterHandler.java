package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.*;
import request.RegisterRequest;
import result.ErrorResult;
import result.Result;
import service.RegisterService;

import java.io.*;
import java.net.HttpURLConnection;

public class RegisterHandler implements HttpHandler {
    /**
     * Creates a new user account (user row in the database)
     * Generates 4 generations of ancestor data for the new user (just like the /fill endpoint if called with a generations value of 4 and this new user’s username as parameters)
     * Logs the user in
     * @param exchange HttpExchange exchange
     * @throws IOException File Input/Output Error
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {
            // Verify that request if a POST
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                System.out.println("New Register Request");
                // Extract the JSON string from the HTTP request body
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                // Get the request body input stream
                InputStream reqBody = exchange.getRequestBody();

                // Read JSON string from the input stream
                String reqData = readString(reqBody);

                RegisterRequest request = gson.fromJson(reqData, RegisterRequest.class);

                RegisterService service = new RegisterService();
                Result result = service.register(request);

                // If the Registration was successful return 200
                if(result.getClass() != ErrorResult.class) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                // If the request was unsuccessful, return 400
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
				gson.toJson(result, resBody);
                resBody.close();

                exchange.getResponseBody().close();
                success = true;
            }
            // If unsuccessful, return 400
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }
        // If error, return 500
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    /**
     * Reads the String from the Input Stream
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
