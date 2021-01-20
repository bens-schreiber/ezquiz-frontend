package requests;

import gui.etc.Account;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;

public class DatabaseRequest {

    private DatabaseRequest() {
    }

    /**
     * @return status code
     */
    public static int registerUser(String username, String password) throws IOException, InterruptedException, JSONException {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);

        HttpResponse<String> response = Request.postRequest(body, "users/register");

        return response.statusCode();
    }

    /**
     * Verify if login credentials were correct.
     *
     * @return status code.
     */
    public static int verifyLoginCredentials(String username, String password) throws IOException, InterruptedException, JSONException {

        JSONObject body = new JSONObject();
        body.put("password", password);
        body.put("username", username);

        HttpResponse<String> response = Request.postRequest(body, "users/login");

        if (response.headers().firstValue("token").isPresent()) {

            //Store token and username in static user
            Account.login(username, response.headers().firstValue("token").get());

            System.out.println(Account.AUTH_TOKEN());
        }
        return response.statusCode();

    }

    /**
     * Post bitmap to the server.
     *
     * @return 4 digit test key.
     */
    public static String uploadTestKey(long key) throws JSONException, IOException, InterruptedException {
        JSONObject body = new JSONObject();
        body.put("key", String.valueOf(key));

        HttpResponse<String> response = Request.postRequest(body, "database/key", Account.AUTH_TOKEN());

        return new JSONObject(response.body()).get("key").toString();
    }

    /**
     * Get test key from server
     *
     * @return bitmap
     */
    public static long getTestKey(int key) throws InterruptedException, JSONException, IOException {
        JSONObject response = (JSONObject) Request.getJSONFromURL("http://localhost:7080/api/database/key/" + key, Account.AUTH_TOKEN()).get("obj0");
        return Long.parseLong(response.get("bitmap").toString());
    }
}
