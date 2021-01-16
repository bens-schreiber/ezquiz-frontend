package requests;

import etc.Constants;
import gui.etc.Account;
import org.json.JSONException;
import org.json.JSONObject;
import questions.Question;

import java.io.IOException;
import java.util.List;

/**
 * Requests all questions in appropriate range and converts from JSON to Question list
 */
public class QuestionRequest extends Request {

    private Question.Subject subject;
    private Question.Type type;
    private List<Integer> ids;
    private JSONObject json;
    private int amount;

    /**
     * Constructor
     */
    public QuestionRequest(Question.Type type, Question.Subject subject, int amount) {
        this.subject = subject;
        this.type = type;
        this.amount = amount;
    }

    public QuestionRequest(List<Integer> ids) {
        this.ids = ids;
    }

    /**
     * Determine the proper gateway from object variables
     */
    private JSONObject getJSONFromSelection() throws InterruptedException, JSONException, IOException {

        //If this instance has ids then make the request using them
        if (this.ids != null) {
            StringBuilder stringBuilder = new StringBuilder().append(Constants.DEFAULT_PATH);

            for (Integer id : this.ids) {
                stringBuilder.append(id).append(",");
            }

            //get rid of the last "," because it isnt needed
            return getJSONFromURL(stringBuilder.substring(0, stringBuilder.toString().length() - 1), Account.AUTH_TOKEN());
        }

        if (this.subject != null && this.type != null) {
            return getJSONFromURL(Constants.DEFAULT_PATH + this.subject + "/" + this.type, Account.AUTH_TOKEN());

        } else if (this.subject != null) {
            return getJSONFromURL(Constants.DEFAULT_PATH + "subject/" + this.subject, Account.AUTH_TOKEN());

        } else if (this.type != null) {
            return getJSONFromURL(Constants.DEFAULT_PATH + "type/" + this.type, Account.AUTH_TOKEN());

        } else {
            return getJSONFromURL(Constants.DEFAULT_PATH, Account.AUTH_TOKEN());
        }

    }

    public QuestionRequest makeRequest() throws InterruptedException, IOException, JSONException, IllegalArgumentException {

        //Make the actual request
        this.json = getJSONFromSelection();

        //If no questions could be found with given parameters
        if (json.length() == 0) {
            throw new IllegalArgumentException();
        }

        return this;
    }

    public JSONObject getJson() {
        return json;
    }


}
