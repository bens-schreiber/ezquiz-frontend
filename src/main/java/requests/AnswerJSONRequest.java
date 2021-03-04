package requests;

import etc.Constants;
import gui.account.Account;
import org.json.JSONException;
import org.json.JSONObject;
import question.QuestionNode;

import java.io.IOException;

/**
 * Makes a request to the database that assigns the correct answers to each question of the quiz node array.
 */
public class AnswerJSONRequest {

    private JSONObject json;
    private final QuestionNode[] questionNodes;

    public AnswerJSONRequest(QuestionNode[] questionNode) {
        this.questionNodes = questionNode;
    }

    public AnswerJSONRequest initializeRequest() throws InterruptedException, JSONException, IOException {

        //Create path for getting answers
        StringBuilder stringBuilder = new StringBuilder()
                .append(Constants.DEFAULT_PATH)
                .append("questions/answer/");

        for (QuestionNode questionNode : this.questionNodes) {
            stringBuilder.append(questionNode.getID()).append(",");
        }

        //get rid of the last "," and convert string to JSON
        this.json = new JSONObject(
                Request.getFromURL(stringBuilder.substring(0, stringBuilder.length() - 1), Account.getUser().getAuth())
                        .body());


        return this;
    }

    public JSONObject getJson() {
        return json;
    }
}
