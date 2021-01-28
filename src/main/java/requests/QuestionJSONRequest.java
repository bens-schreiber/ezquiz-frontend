package requests;

import etc.Constants;
import gui.account.Account;
import gui.account.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Requests all questions in appropriate range and converts from JSON to Question list
 */
public class QuestionJSONRequest {

    private JSONObject json;

    private final String questionPath;

    private final String auth;

    public QuestionJSONRequest(User user) {
        this.auth = user.getAuth();
        this.questionPath = Constants.DEFAULT_PATH + "/questions/" + Account.getQuizPath();
    }

    public QuestionJSONRequest(User user, String questionPath) {
        this.auth = user.getAuth();
        this.questionPath = questionPath;
    }


    public QuestionJSONRequest initializeRequest() throws InterruptedException, IOException, JSONException {

        //Make the request from the path built by the constructor.
        this.json = new JSONObject(
                Request.getFromURL(questionPath, auth).body()
        );

        //If no questions could be found with given parameters
        if (json.length() == 0) {
            throw new IllegalArgumentException();
        }

        return this;
    }

    public JSONObject getJson() {
        return this.json;
    }


}
