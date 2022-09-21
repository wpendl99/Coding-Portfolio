package com.wkp23.familymapclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

public class HttpClient {

    private static final String LOG_TAG = "HttpClient";

    public Result loginUser(URL url, LoginRequest request){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Convert Request JSON String
            String jsonOutString = gson.toJson(request);

            // Set the body of the Request
            try(OutputStream os = connection.getOutputStream()) {
                byte[] output = jsonOutString.getBytes(StandardCharsets.UTF_8);
                os.write(output, 0, output.length);
            }

            // Make the Request
            connection.connect();

            // Get the Response from HTTP
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // return the Result
                    return gson.fromJson(response.toString(), LoginResult.class);
                }

            // Get Error Response
            } else {
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // return the Result
                    return gson.fromJson(response.toString(), ErrorResult.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public Result registerUser(URL url, RegisterRequest request){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Convert Request JSON String
            String jsonOutString = gson.toJson(request);

            // Set the body of the Request
            try(OutputStream os = connection.getOutputStream()) {
                byte[] output = jsonOutString.getBytes(StandardCharsets.UTF_8);
                os.write(output, 0, output.length);
            }

            // Make the Request
            connection.connect();

            // Get the Response from HTTP
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // return the Result
                    return gson.fromJson(response.toString(), RegisterResult.class);
                }

                // Get Error Response
            } else {
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // return the Result
                    return gson.fromJson(response.toString(), ErrorResult.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public Result userPeople(URL url, PersonRequest request) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", request.getAuthToken().getAuthtoken());
            connection.setRequestMethod("GET");

            // Make the Request
            connection.connect();

            // Get the Response from HTTP
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // return the Result
                    return gson.fromJson(response.toString(), PersonResult.class);
                }

                // Get Error Response
            } else {
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // return the Result
                    return gson.fromJson(response.toString(), ErrorResult.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public Result userEvents(URL url, EventRequest request) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", request.getAuthToken().getAuthtoken());
            connection.setRequestMethod("GET");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Make the Request
            connection.connect();

            // Get the Response from HTTP
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // return the Result
                    return gson.fromJson(response.toString(), EventsResult.class);
                }

                // Get Error Response
            } else {
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // return the Result
                    return gson.fromJson(response.toString(), ErrorResult.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}
