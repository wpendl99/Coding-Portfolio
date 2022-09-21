package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler implements HttpHandler {
    /**
     * Processes main http requests returning corresponding files if they exist
     * @param exchange HttpExchange exchange
     * @throws IOException File Input/Output Error
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            // Make sure the request is GET
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                // Process the URL
                String urlPath = exchange.getRequestURI().toString();

                // If there isn't an extension, default to /index.html
                if (urlPath == null || urlPath.equals("/")){
                    urlPath = "/index.html";
                }

                String filePath = "web" + urlPath;

                File file = new File(filePath);

                // If the file exists, return it
                if(file.exists()){

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);
                    respBody.close();

                    // If the file doesn't exist, return a 404 Error
                } else  {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(Path.of("web/HTML/404.html"), respBody);
                    respBody.close();
                    exchange.getResponseBody().close();

                }
                success = true;

            }
            // If unsuccessful, return 400
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }

        // If there is an error, return 500
        catch (IOException e) {
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
