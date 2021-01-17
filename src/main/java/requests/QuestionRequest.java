package requests;

import etc.Constants;
import gui.etc.Account;
import org.json.JSONException;
import org.json.JSONObject;
import questions.question.Question;

import java.io.IOException;
import java.util.List;

/**
 * Requests all questions in appropriate range and converts from JSON to Question list
 */
public class QuestionRequest {

    private JSONObject json;
    private StringBuilder path = new StringBuilder(Constants.DEFAULT_PATH);

    /**
     * Constructor
     */
    public QuestionRequest(Question.Type type, Question.Subject subject) {
        path.append(subject).append("/").append(type);
    }

    public QuestionRequest(Question.Type type) {
        path.append("type/").append(type);
    }

    public QuestionRequest(Question.Subject subject) {
        path.append("subject/").append(subject);
    }

    public QuestionRequest(List<Integer> ids) {
        for (Integer id : ids) path.append(id).append(",");
        path = new StringBuilder(path.substring(0, path.toString().length() - 1));
    }

    public QuestionRequest() {
    }


    public QuestionRequest makeRequest() throws InterruptedException, IOException, JSONException, IllegalArgumentException {

        //Make the request
        this.json = Request.getJSONFromURL(path.toString(), Account.AUTH_TOKEN());

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
