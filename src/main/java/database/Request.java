package database;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class Request {

    //AUTH UUID for http requests
    public static String AUTH_TOKEN = "";

    /**
     * Make HTTP get request to server.
     *
     * @param url   where to send request
     * @param token auth token
     */
    static JSONObject getJSONFromURL(String url, String token) throws IOException, InterruptedException, JSONException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("token", token)
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new JSONObject(response.body());
    }

    /**
     * Make HTTP post to server.
     *
     * @param body       JSONObject of what to send
     * @param urlSegment where to send it
     */
    static HttpResponse<String> postRequest(JSONObject body, String urlSegment) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create("http://localhost:7080/api/" + urlSegment))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    /**
     * Overloaded postRequest
     * Make HTTP post to server.
     *
     * @param body       JSONObject of what to send
     * @param urlSegment where to send it
     * @param token      auth token
     */
    static HttpResponse<String> postRequest(JSONObject body, String urlSegment, String token) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("token", token)
                .uri(URI.create("http://localhost:7080/api/" + urlSegment))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }
}
