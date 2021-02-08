package requests;

import etc.Constants;
import gui.account.Account;
import gui.account.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;

/**
 * Requests all questions from a quiz key, along with the stored quiz preferences.
 */
public class QuizJSONRequest {

    private JSONObject questions;
    private JSONObject preferences;

    private final String questionPath = Constants.DEFAULT_PATH + "/quiz/" + Account.getQuiz().getKey();
    private final String auth;

    public QuizJSONRequest(User user) {
        this.auth = user.getAuth();
    }

    public QuizJSONRequest initializeRequest() throws InterruptedException, IOException, JSONException {

        //Make the request from the path built by the constructor.
        JSONObject json = new JSONObject(
                Request.getFromURL(questionPath, auth).body()
        );

        //If no questions could be found
        if (json.length() == 0 || !json.has("questions") && !json.has("preferences")) {
            throw new ConnectException();
        }

        this.questions = (JSONObject) json.get("questions");

        this.preferences = (JSONObject) ((JSONObject) json.get("preferences")).get("obj0");

        return this;
    }

    public JSONObject getQuestions() {
        return this.questions;
    }

    public JSONObject getPreferences() {
        return preferences;
    }
}
