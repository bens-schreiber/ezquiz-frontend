package com.benschreiber.requests;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Foundation for request classes
 */
class Request {

    /**
     * Make HTTP GET
     *
     * @param url   where to send request
     * @param token auth token
     */
    static HttpResponse<String> getRequest(String url, String token) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("token", token)
                .uri(URI.create(url))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    /**
     * Make HTTP POST
     *
     * @param body       JSONObject of what to send
     * @param urlSegment where to send it
     */
    static HttpResponse<String> postRequest(JSONObject body, String urlSegment) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(DatabaseRequest.DEFAULT_PATH + urlSegment))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }


    /**
     * @param body       JSONObject of what to send
     * @param urlSegment where to send it
     * @param token      auth token
     */
    static HttpResponse<String> postRequest(JSONObject body, String urlSegment, String token) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("token", token)
                .uri(URI.create(DatabaseRequest.DEFAULT_PATH + urlSegment))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }


    /**
     * HTTP DEL Request
     *
     * @param urlSegment where to send it
     * @param token      auth token
     */
    static HttpResponse<String> deleteRequest(String urlSegment, String token) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("token", token)
                .uri(URI.create(DatabaseRequest.DEFAULT_PATH + urlSegment))
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }
}
