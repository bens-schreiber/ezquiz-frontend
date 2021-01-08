package database;

import etc.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import quiz.questions.Question;

import java.io.IOException;

/**
 * Request methods for getting a JSON question through the Rest Server
 */
public class QuestionRequest extends Request {

    private int id;
    private Question.Subject subject;
    private Question.Type type;
    private JSONObject json;

    /**
     * Constructor
     */

    public QuestionRequest(Question.Type type, Question.Subject subject) {
        this.subject = subject;
        this.type = type;
    }

    public QuestionRequest() {
    }


    /**
     * Determine what request to make
     */
    public QuestionRequest makeRequest() throws JSONException, InterruptedException, IOException {

        if ((this.subject != null) && (this.type != null)) this.json = requestQuestion(subject, type, id);

        else if (this.subject != null) this.json = requestQuestion(subject, id);

        else if (this.type != null) this.json = requestQuestion(type, id);

        else this.json = requestQuestion(id);

        return this;
    }


    /**
     * @param id specifies absolute location in server.
     * @throws JSONException on object not found
     */
    private static JSONObject requestQuestion(int id) throws IOException, JSONException, InterruptedException {

        return (JSONObject) Request.getJSONFromURL(Constants.DEFAULT_PATH + id, AUTH_TOKEN).get("obj0");

    }

    /**
     * Limits pool of questions to those with only the specified Question Type.
     *
     * @param id JSONObject with correlating id. Not absolute id.
     */
    private static JSONObject requestQuestion(Question.Type type, int id) throws IOException, JSONException, InterruptedException {

        return (JSONObject) Request
                .getJSONFromURL(Constants.DEFAULT_PATH + "type/" + type, AUTH_TOKEN)
                .get("obj" + id);

    }

    /**
     * Limits pool of questions to those with only the specified Question Subject.
     *
     * @param id JSONObject with correlating id. Not absolute id.
     */
    private static JSONObject requestQuestion(Question.Subject subject, int id) throws IOException, JSONException, InterruptedException {

        return (JSONObject) Request
                .getJSONFromURL(Constants.DEFAULT_PATH + "subject/" + subject, AUTH_TOKEN)
                .get("obj" + id);

    }

    /**
     * Limits pool of questions to those with only the specified question Type and Subject.
     *
     * @param id JSONObject with correlating id. Not absolute id.
     */
    private static JSONObject requestQuestion(Question.Subject subject, Question.Type type, int id) throws IOException, JSONException, InterruptedException {

        return (JSONObject) Request
                .getJSONFromURL(Constants.DEFAULT_PATH + subject + "/" + type, AUTH_TOKEN)
                .get("obj" + id);

    }

    public JSONObject getJSON() {
        return json;
    }

    public void setId(int id) {
        this.id = id;
    }

}
