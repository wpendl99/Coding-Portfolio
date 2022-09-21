package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.FillRequest;
import result.Result;
import service.FillService;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class FillHandler implements HttpHandler {
    /**
     * Populates the server's database with generated data for the specified username. The required "username" parameter must be a user already registered with the server. If there is any data in the database already associated with the given username, it is deleted.
     * The optional "generations" parameter lets the caller specify the number of generations of ancestors to be generated, and must be a non-negative integer (the default is 4, which results in 31 new persons each with associated events).
     * More details can be found in the earlier section titled “Family History Information Generation”
     * @param exchange HttpExchange exchange
     * @throws IOException File Input/Output Error
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {
            // Make sure the request is a POST
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                // Initialize Variables
                System.out.println("New Fill Request");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                // Process URL Path
                String urlPath = exchange.getRequestURI().toString();

                String[] arr = urlPath.replaceAll("^/", "").split("/");
                ArrayList<String> list = new ArrayList<>(List.of(arr));

                System.out.println(list);

                // If there wasn't a username, mark unsuccessful
                if(list.size() == 1){
                    System.out.println("Size 1");

                // If given a username
                } else if (list.size() > 1){
                    System.out.println("Size > 1");

                    String username = list.get(1);
                    int generations;

                    // If also given a specification on generation, set that
                    if (list.size() > 2){
                        System.out.println("Size > 2");
                        generations = Integer.parseInt(list.get(2));

                    // If not process 4 generations worth
                    } else {
                        generations = 4;
                    }

                    // Process request
                    FillService service = new FillService();
                    FillRequest request = new FillRequest(username, generations);
                    Result result = service.fill(request);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                    gson.toJson(result, resBody);
                    resBody.close();

                    exchange.getResponseBody().close();
                    success = true;

                } else {
                    System.out.println("Error");

                }
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
     * Reads the string of the InputStearm
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

