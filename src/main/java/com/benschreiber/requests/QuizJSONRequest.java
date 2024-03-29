package com.benschreiber.requests;

import com.benschreiber.etc.Account;
import com.benschreiber.etc.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Requests all questions from a quiz key, along with the stored quiz preferences.
 */
public class QuizJSONRequest {

    private JSONObject questions;
    private JSONObject preferences;

    private String questionPath = DatabaseRequest.DEFAULT_PATH + "/quiz/" + Account.getQuiz().getKey();
    private final String auth;

    public QuizJSONRequest(User user) {
        this.auth = user.getAuth();
    }

    public QuizJSONRequest(User user, String questionPath) {
        this.auth = user.getAuth();
        this.questionPath = questionPath;
    }

    public QuizJSONRequest initializeRequest() throws InterruptedException, IOException, JSONException {

        //Make the request from the path built by the constructor.
        JSONObject json = new JSONObject(Request.getRequest(questionPath, auth).body());

        if (json.has("questions") && json.has("preferences")) {
            this.questions = (JSONObject) json.get("questions");
            this.preferences = (JSONObject) ((JSONObject) json.get("preferences")).get("obj0");
        } else {
            this.questions = json;
        }


        return this;

    }

    public JSONObject getQuestions() {
        return this.questions;
    }

    public JSONObject getPreferences() {
        return preferences;
    }
}
