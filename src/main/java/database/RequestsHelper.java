package database;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Reads json from url and returns JSONObject
 */
//todo: httpclient
class RequestsHelper {
    public static JSONObject getJSONFromURL(String url, String token) throws IOException, InterruptedException, JSONException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .setHeader("token", token)
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new JSONObject(response.body());
    }

    public static HttpResponse<String> postRequest(JSONObject body, String urlSegment) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create("http://localhost:7080/api/users/" + urlSegment))
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }
}