package requests;

import gui.account.Account;
import gui.account.Quiz;
import gui.account.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpResponse;

public class DatabaseRequest {

    private DatabaseRequest() {
    }


    /**
     * POST
     *
     * @return status code
     */
    public static Status postNewUser(String username, String password) throws IOException, InterruptedException, JSONException {
        try {

            JSONObject body = new JSONObject()
                    .put("username", username)
                    .put("password", password);

            HttpResponse<String> response = Request.postRequest(body, "users/register");

            return Status.getStatusFromInt(response.statusCode());

        } catch (ConnectException ignore) {
            return Status.NO_CONNECTION;
        }
    }


    /**
     * POST
     * Verify if login credentials were correct, Account.login if possible.
     *
     * @return status code
     */
    public static Status verifyLoginCredentials(String username, String password) throws IOException, InterruptedException, JSONException {
        try {

            JSONObject body = new JSONObject()
                    .put("password", password)
                    .put("username", username);

            HttpResponse<String> response = Request.postRequest(body, "users/login");

            if (response.headers().firstValue("token").isPresent()) {
                //Store token and username in static user
                System.out.println(response.headers().firstValue("token"));
                Account.login(username, response.headers().firstValue("token").get(), Boolean.parseBoolean(response.body()));
            }

            return Status.getStatusFromInt(response.statusCode());

        } catch (ConnectException ignore) {
            return Status.NO_CONNECTION;
        }

    }

    /**
     * POST
     * Post a Quiz Key to a User on the database.
     *
     * @return status code
     */
    public static Status postQuizKey(User user, Quiz quiz) throws JSONException, IOException, InterruptedException {

        try {

            //Create a JSON object with the fields username and quizKey and the current Accounts information attatched.
            JSONObject json = new JSONObject()
                    .put("username", user.getUsername())
                    .put("quizKey", quiz.getKey());

            HttpResponse<String> response = Request.postRequest(json, "users/key", Account.getUser().getAuth());

            return Status.getStatusFromInt(response.statusCode());

        } catch (ConnectException ignore) {
            return Status.NO_CONNECTION;
        }
    }

    /**
     * POST
     * Post a Quiz to the database.
     *
     * @return status code
     */
    public static Status postQuiz(JSONArray jsonObject, User user) throws IOException, InterruptedException {
        try {

            HttpResponse<String> response = Request.postRequest(jsonObject, user.getAuth());

            return Status.getStatusFromInt(response.statusCode());

        } catch (ConnectException ignore) {
            return Status.NO_CONNECTION;
        }

    }

    /**
     * POST
     * Post score to quiz to database.
     *
     * @return status code
     */
    public static Status postQuizScore(int score, User user, Quiz quiz) throws JSONException, IOException, InterruptedException {
        try {

            JSONObject json = new JSONObject().put("score", score)
                    .put("username", user.getUsername())
                    .put("quizKey", quiz.getKey());

            HttpResponse<String> response = Request.postRequest(json, "users/score", Account.getUser().getAuth());

            return Status.getStatusFromInt(response.statusCode());

        } catch (ConnectException ignore) {
            return Status.NO_CONNECTION;
        }

    }


    /**
     * GET
     * From a Quiz Key, set Account.Quiz path
     *
     * @return status code
     */
    public static Status getQuizPathFromKey(int key, User user) throws InterruptedException, IOException, JSONException {

        try {

            HttpResponse<String> response = Request.getFromURL("http://localhost:7080/api/quiz/key/" + key, user.getAuth());

            JSONObject body = new JSONObject(response.body());


            if (body.has("obj0")) {

                JSONObject json = new JSONObject(body.get("obj0").toString());

                //If response has the correct pieces, set the Account quiz
                if (json.has("quizowner") && json.has("quizname")) {
                    Account.setQuiz(json.get("quizowner").toString(), json.get("quizname").toString(), key);
                    return Status.ACCEPTED;
                }

            }

            return Status.NO_CONTENT;

        } catch (ConnectException ignore) {
            return Status.NO_CONNECTION;
        }
    }


    /**
     * GET
     * Get all saved quizzes to the current Account from the database.
     *
     * @return ObservableList that FX uses for TableView.
     */
    public static ObservableList<Quiz> getSavedQuizKeys(User user) throws InterruptedException, JSONException, IOException {

        try {

            HttpResponse<String> response = Request.getFromURL("http://localhost:7080/api/users/key/" + user.getUsername(), user.getAuth());

            JSONObject body = new JSONObject(response.body());
            ObservableList<Quiz> list = FXCollections.observableArrayList();
            //If response contains at least a single object
            if (body.has("obj0")) {

                for (int i = 0; i < body.length(); i++) {

                    JSONObject json = new JSONObject(body.get("obj" + i).toString());

                    //Server will always respond with these variables, no validation needed
                    list.add(new Quiz(json.get("quizowner").toString(),
                            json.get("quizname").toString(),
                            Integer.parseInt(json.get("quizkey").toString())));
                }
            }

            return list;

        } catch (ConnectException ignore) {
            return FXCollections.observableArrayList();
        }

    }

    /**
     * GET
     * Get all of users previous quizzes.
     *
     * @return list for tableview
     */
    public static ObservableList<Quiz> getPreviousQuizzes() throws InterruptedException, JSONException, IOException {

        try {

            HttpResponse<String> response = Request.getFromURL("http://localhost:7080/api/users/score/" + Account.getUser().getUsername(), Account.getUser().getAuth());

            JSONObject body = new JSONObject(response.body());
            //If response contains at least a single object
            ObservableList<Quiz> list = FXCollections.observableArrayList();
            if (body.has("obj0")) {

                for (int i = 0; i < body.length(); i++) {

                    JSONObject json = new JSONObject(body.get("obj" + i).toString());

                    //Server will always respond with these variables, no validation needed
                    list.add(new Quiz(json.get("quizowner").toString(),
                            json.get("quizname").toString(),
                            Integer.parseInt(json.get("quizkey").toString()),
                            Integer.parseInt(json.get("score").toString())
                    ));
                }
            }

            return list;

        } catch (ConnectException ignore) {
            return FXCollections.observableArrayList();
        }
    }


    /**
     * GET
     * Get all of users previous quizzes.
     *
     * @return status code
     */
    public static ObservableList<Quiz> getCreatedQuizzes(User user) throws InterruptedException, JSONException, IOException {

        try {

            HttpResponse<String> response = Request.getFromURL("http://localhost:7080/api/users/quizzes/" + user.getUsername(), user.getAuth());
            JSONObject body = new JSONObject(response.body());

            ObservableList<Quiz> list = FXCollections.observableArrayList();
            //If response contains at least a single object
            if (body.has("obj0")) {

                for (int i = 0; i < body.length(); i++) {

                    JSONObject json = new JSONObject(body.get("obj" + i).toString());

                    //Null owner because it isn't needed for table view
                    list.add(new Quiz(null,
                            json.get("quizname").toString(),
                            Integer.parseInt(json.get("quizkey").toString())));
                }
            }

            return list;

        } catch (ConnectException ignore) {
            return FXCollections.observableArrayList();
        }
    }


    /**
     * DELETE
     * Delete all questions, quizkeys and user saved quizkeys from database, remove quiz
     *
     * @return status code
     */
    public static Status deleteQuiz(Quiz quiz, User user) throws IOException, InterruptedException {

        try {

            HttpResponse<String> response = Request.deleteRequest("quiz/quizzes/" + quiz.getKey(), user.getAuth());
            return Status.getStatusFromInt(response.statusCode());
        } catch (ConnectException ignore) {
            return Status.NO_CONNECTION;
        }

    }


    /**
     * DELETE
     * Delete the user saved quizkey
     *
     * @return status code
     */
    public static Status deleteQuizKey(Quiz quiz, User user) throws IOException, InterruptedException {

        try {

            HttpResponse<String> response = Request.deleteRequest("users/key/" + user.getUsername()
                    + "/" + quiz.getKey(), user.getAuth());

            return Status.getStatusFromInt(response.statusCode());

        } catch (ConnectException ignore) {
            return Status.NO_CONNECTION;
        }
    }
}
