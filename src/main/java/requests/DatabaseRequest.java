package requests;

import gui.etc.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public static boolean verifyLoginCredentials(String username, String password) throws IOException, InterruptedException, JSONException {

        JSONObject body = new JSONObject();
        body.put("password", password);
        body.put("username", username);

        HttpResponse<String> response = Request.postRequest(body, "users/login");

        if (response.headers().firstValue("token").isPresent()) {

            //Store token and username in static user
            Account.login(username, response.headers().firstValue("token").get(), Boolean.parseBoolean(response.body()));

            System.out.println(Account.getAuth());
            return true;
        }
        return false;

    }

    /**
     * Post bitmap to the server.
     *
     * @return 4 digit test key.
     */
    public static String postQuizRetakeCode(long key) throws JSONException, IOException, InterruptedException {
        JSONObject body = new JSONObject();
        body.put("key", String.valueOf(key));

        HttpResponse<String> response = Request.postRequest(body, "database/code", Account.getAuth());

        return new JSONObject(response.body()).get("key").toString();
    }

    /**
     * Use a retake code to get a selection of quiz ids.
     *
     * @return bitmap
     */
    public static long getQuizRetakeCode(int code) throws InterruptedException, JSONException, IOException {
        JSONObject response = (JSONObject) Request.getJSONFromURL("http://localhost:7080/api/database/code/" + code, Account.getAuth()).get("obj0");
        return Long.parseLong(response.get("bitmap").toString());
    }

    public static boolean setQuizPathFromKey(int key) throws InterruptedException, IOException {
        try {

            JSONObject response = (JSONObject) Request.getJSONFromURL("http://localhost:7080/api/database/key/" + key, Account.getAuth()).get("obj0");

            //If response has the correct pieces, set the Account quiz
            if (response.has("quizowner") && response.has("quizname")) {
                Account.setQuiz(response.get("quizowner").toString(), response.get("quizname").toString(), key);
                return true;
            }

            return false;

        } catch (JSONException e) {
            return false;
        }
    }


    public static int postQuiz(JSONArray jsonObject) throws IOException, InterruptedException, JSONException {

        HttpResponse<String> response = Request.postRequest(jsonObject, "database/questions", Account.getAuth());
        return response.statusCode();

    }

    public static int postQuizKey(Account.User user) throws JSONException, IOException, InterruptedException {
        JSONObject json = new JSONObject();
        json.put("username", user.getUsername());
        json.put("quizKey", Account.getQuiz().getKey());

        HttpResponse<String> response = Request.postRequest(json, "users/key", Account.getAuth());
        return response.statusCode();
    }

    public static ObservableList<Account.Quiz> getSavedQuizzes() throws InterruptedException, JSONException, IOException {

        JSONObject response = Request.getJSONFromURL("http://localhost:7080/api/users/key/" + Account.getUsername(), Account.getAuth());

        if (response.has("obj0")) {

            ObservableList<Account.Quiz> list = FXCollections.observableArrayList();
            for (int i = 0; i < response.length(); i++) {
                JSONObject json = new JSONObject(response.get("obj" + i).toString());
                list.add(new Account.Quiz(json.get("quizowner").toString(),
                        json.get("quizname").toString(),
                        Integer.parseInt(json.get("quizkey").toString())));
            }
            return list;
        }
        System.out.println("fail :(");
        return FXCollections.observableArrayList();


    }


//    private static String verifyAdmin() {}

//
//    public static int getQuizKey() {}


}
