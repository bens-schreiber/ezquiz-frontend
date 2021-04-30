package com.benschreiber.requests;

import com.benschreiber.etc.Account;
import com.benschreiber.gui.fxobjs.QuestionNode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Makes a request to the database that sets the correct answers to each classes.question of the quiz node array.
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
                .append(DatabaseRequest.DEFAULT_PATH)
                .append("questions/answer/");

        for (QuestionNode questionNode : this.questionNodes) {
            stringBuilder.append(questionNode.getID()).append(",");
        }

        //get rid of the last "," and convert string to JSON
        this.json = new JSONObject(
                Request.getRequest(stringBuilder.substring(0, stringBuilder.length() - 1), Account.getUser().getAuth())
                        .body());


        return this;
    }

    public JSONObject getJson() {
        return json;
    }
}
