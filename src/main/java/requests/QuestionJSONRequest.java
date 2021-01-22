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
public class QuestionJSONRequest {

    private JSONObject json;

    //Initialize path as string builder. Add onto default path to create path needed for the request.
    private StringBuilder path = new StringBuilder(Constants.DEFAULT_PATH);

    /**
     * Constructor
     */
    public QuestionJSONRequest(Question.Type type, Question.Subject subject) {
        path.append(subject).append("/").append(type);
    }

    public QuestionJSONRequest(Question.Type type) {
        path.append("type/").append(type);
    }

    public QuestionJSONRequest(Question.Subject subject) {
        path.append("subject/").append(subject);
    }

    public QuestionJSONRequest(List<Integer> ids) {
        for (Integer id : ids) {
            path.append(id).append(",");
        }
        path = new StringBuilder(path.substring(0, path.toString().length() - 1));
    }

    public QuestionJSONRequest() {
    }


    public QuestionJSONRequest initializeRequest() throws InterruptedException, IOException, JSONException {

        //Make the request from the path built by the constructor.
        this.json = Request.getJSONFromURL(path.toString(), Account.AUTH_TOKEN());

        //If no questions could be found with given parameters
        if (json.length() == 0) {
            throw new IllegalArgumentException();
        }

        return this;
    }

    public JSONObject getJson() {
        System.out.println(json);
        return this.json;
    }


}
