package requests;

import etc.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import quiz.question.Question;
import quiz.question.QuestionFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Request methods for getting a JSON question through the Rest Server
 */
public class QuestionRequest extends Request {

    private Question.Subject subject;
    private Question.Type type;

    private int amount;
    private List<Integer> ids;

    private JSONObject json;

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

    public QuestionRequest makeRequest() throws InterruptedException, IOException, JSONException, IllegalArgumentException {

        this.json = getJSONFromSelection();

        if (json.length() == 0) {
            throw new IllegalArgumentException();
        }

        //If the user specified amount is large, or 0 (for pre made quizzes), set it to the maximum amount possible
        if (amount > json.length() || amount == 0) {
            amount = json.length() - 1;
        }

        return this;
    }

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

    /**
     * Create ids of all existing questions, randomize and use sublist of
     * defined amount size, unless ids is specified
     */
    public List<Question> toList() throws JSONException {
        //If ids have been specified use specified list method
        if (this.ids != null) {
            return this.toSpecifiedList();
        }

        //Create a pool of question id's in the specific size of how many questions available
        List<Integer> idPool = new LinkedList<>();
        for (int i = 0; i < json.length() - 1; i++) idPool.add(i);

        //Randomize the pool of ids
        Collections.shuffle(idPool);

        List<Question> questions = new LinkedList<>();
        for (Integer id : idPool.subList(0, this.amount)) {
            questions.add(QuestionFactory.questionFromJSON(
                    (JSONObject) this.json.get("obj" + id))
            );
        }

        return questions;
    }

    //ids are already given
    private List<Question> toSpecifiedList() throws JSONException {

        List<Question> questions = new LinkedList<>();
        int i = 0;
        List<Integer> integers = this.ids;
        for (Integer ignored : integers) {
            questions.add(QuestionFactory.questionFromJSON(
                    (JSONObject) this.json.get("obj" + i++))
            );
        }

        return questions;
    }

}
