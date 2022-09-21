package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.LoginResult;
import request.LoginRequest;
import result.Result;
import service.LoginService;

import java.io.*;
import java.net.HttpURLConnection;

public class LoginHandler implements HttpHandler {
    /**
     * Logs the user in
     * @param exchange HttpExchange exchange
     * @throws IOException File Input/Output Error
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {
            // Verifies request is POST
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                System.out.println("New Login Request");

                // Extract the JSON string from the HTTP request body
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                // Process Request
                LoginRequest request = gson.fromJson(reqData, LoginRequest.class);

                LoginService service = new LoginService();
                Result result = service.login(request);
                // If it that was a valid username and password, return 200
                if(result instanceof LoginResult) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                    gson.toJson(result, resBody);
                    resBody.close();

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    // If invalid username or password, return 400
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                    gson.toJson(result, resBody);
                    resBody.close();

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
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
     * Reads the string from the InputStream
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
