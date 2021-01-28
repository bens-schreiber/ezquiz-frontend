package requests;

import etc.Constants;
import org.json.JSONArray;
import org.json.JSONException;
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
     * Make HTTP get request to server.
     *
     * @param url   where to send request
     * @param token auth token
     */
    static HttpResponse<String> getFromURL(String url, String token) throws IOException, InterruptedException, JSONException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("token", token)
                .uri(URI.create(url))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
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
                .uri(URI.create(Constants.DEFAULT_PATH + urlSegment))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }


    /**
     * Make HTTP post to server.
     *
     * @param body  JSONArray to send
     * @param token auth token
     */
    static HttpResponse<String> postRequest(JSONArray body, String token) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("token", token)
                .uri(URI.create("http://localhost:7080/api/" + "quiz/new"))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }


    /**
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
                .uri(URI.create(Constants.DEFAULT_PATH + urlSegment))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }


    /**
     * Delete Request to the Server.
     *
     * @param urlSegment where to send it
     * @param token      auth token
     */
    static HttpResponse<String> deleteRequest(String urlSegment, String token) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("token", token)
                .uri(URI.create(Constants.DEFAULT_PATH + urlSegment))
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }
}
