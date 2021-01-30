package requests;

import etc.Constants;
import gui.account.Account;
import gui.account.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;

/**
 * Requests all questions in appropriate range and converts from JSON to Question list
 */
public class QuizJSONRequest {

    private JSONObject questions;
    private JSONObject preferences;

    private final String questionPath;

    private final String auth;

    public QuizJSONRequest(User user) {
        this.auth = user.getAuth();
        this.questionPath = Constants.DEFAULT_PATH + "/questions/" + Account.getQuiz().getKey();
    }

    public QuizJSONRequest(User user, String questionPath) {
        this.auth = user.getAuth();
        this.questionPath = questionPath;
    }


    public QuizJSONRequest initializeRequest() throws InterruptedException, IOException, JSONException {

        //Make the request from the path built by the constructor.
        JSONObject json = new JSONObject(
                Request.getFromURL(questionPath, auth).body()
        );

        //If no questions could be found
        if (json.length() == 0 || !json.has("preferences") || !json.has("questions")) {
            throw new ConnectException("Could not connect to rest server.");
        }

        this.preferences = (JSONObject) ((JSONObject) json.get("preferences")).get("obj0");
        this.questions = (JSONObject) json.get("questions");

        return this;
    }

    public JSONObject getPreferenceJSON() {
        return this.preferences;
    }

    public JSONObject getQuestionJSON() {
        return this.questions;
    }


}
