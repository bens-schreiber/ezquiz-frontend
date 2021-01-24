package requests;

import gui.etc.Account;
import org.json.JSONArray;
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
            Account.login(username, response.headers().firstValue("token").get(), Boolean.parseBoolean(response.body()));

            System.out.println(Account.AUTH_TOKEN());
        }
        return response.statusCode();

    }

    /**
     * Post bitmap to the server.
     *
     * @return 4 digit test key.
     */
    public static String uploadQuizRetakeCode(long key) throws JSONException, IOException, InterruptedException {
        JSONObject body = new JSONObject();
        body.put("key", String.valueOf(key));

        HttpResponse<String> response = Request.postRequest(body, "database/code", Account.AUTH_TOKEN());

        return new JSONObject(response.body()).get("key").toString();
    }

    /**
     * Use a retake code to get a selection of quiz ids.
     *
     * @return bitmap
     */
    public static long getQuizRetakeCode(int code) throws InterruptedException, JSONException, IOException {
        JSONObject response = (JSONObject) Request.getJSONFromURL("http://localhost:7080/api/database/code/" + code, Account.AUTH_TOKEN()).get("obj0");
        return Long.parseLong(response.get("bitmap").toString());
    }

    public static boolean setQuizPathFromKey(int key) throws InterruptedException, IOException {
        try {

            JSONObject response = (JSONObject) Request.getJSONFromURL("http://localhost:7080/api/database/key/" + key, Account.AUTH_TOKEN()).get("obj0");

            if (response.has("quizowner") && response.has("quizname")) {
                Account.setQuizPath(response.get("quizowner") + "/" + response.get("quizname"));
                return true;
            }

            return false;

        } catch (JSONException e) {
            return false;
        }
    }


    public static int uploadQuiz(JSONArray jsonObject, String url) throws IOException, InterruptedException, JSONException {

        HttpResponse<String> response = Request.postRequest(jsonObject, url, Account.AUTH_TOKEN());
        return response.statusCode();

    }


//    private static String verifyAdmin() {}

//
//    public static int getQuizKey() {}


}
