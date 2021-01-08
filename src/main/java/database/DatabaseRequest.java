package database;

import etc.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import quiz.questions.Question;

import java.io.IOException;
import java.net.http.HttpResponse;

/**
 * Request methods for getting data from RestServer
 */
public class DatabaseRequest {

    public static String AUTH_TOKEN = "";

    private int id;
    private final Question.Subject subject;
    private final Question.Type type;
    private JSONObject json;

    /**
     * Constructor
     */

    public DatabaseRequest(Question.Type type, Question.Subject subject) {
        this.subject = subject;
        this.type = type;
    }


    /**
     * Determine what request to make
     */
    public DatabaseRequest makeRequest() throws JSONException, InterruptedException, IOException {

        if ((this.subject != null) && (this.type != null)) this.json = requestQuestion(subject, type, id);

        else if (this.subject != null) this.json = requestQuestion(subject, id);

        else if (this.type != null) this.json = requestQuestion(type, id);

        else this.json = requestQuestion(id);

        return this;
    }

    public JSONObject getJSON() {
        return json;
    }

    public void setId(int id) {
        this.id = id;
    }


    /**
     * @param id specifies absolute location in server.
     * @throws JSONException on object not found
     */
    private JSONObject requestQuestion(int id) throws IOException, JSONException, InterruptedException {

        return (JSONObject) DatabaseRequestHelper.getJSONFromURL(Constants.DEFAULT_PATH + id, AUTH_TOKEN).get("obj0");

    }

    /**
     * Limits pool of questions to those with only the specified Question Type.
     *
     * @param id JSONObject with correlating id. Not absolute id.
     */
    private static JSONObject requestQuestion(Question.Type type, int id) throws IOException, JSONException, InterruptedException {

        return (JSONObject) DatabaseRequestHelper
                .getJSONFromURL(Constants.DEFAULT_PATH + "type/" + type, AUTH_TOKEN)
                .get("obj" + id);

    }

    /**
     * Limits pool of questions to those with only the specified Question Subject.
     *
     * @param id JSONObject with correlating id. Not absolute id.
     */
    private static JSONObject requestQuestion(Question.Subject subject, int id) throws IOException, JSONException, InterruptedException {

        return (JSONObject) DatabaseRequestHelper
                .getJSONFromURL(Constants.DEFAULT_PATH + "subject/" + subject, AUTH_TOKEN)
                .get("obj" + id);

    }

    /**
     * Limits pool of questions to those with only the specified question Type and Subject.
     *
     * @param id JSONObject with correlating id. Not absolute id.
     */
    private static JSONObject requestQuestion(Question.Subject subject, Question.Type type, int id) throws IOException, JSONException, InterruptedException {

        return (JSONObject) DatabaseRequestHelper
                .getJSONFromURL(Constants.DEFAULT_PATH + subject + "/" + type, AUTH_TOKEN)
                .get("obj" + id);

    }



    /*
      static methods
     */

    /**
     * @return JSONObject from Question Objects ID.
     */
    public static JSONObject getQuestionAnswer(Question question) throws IOException, JSONException, InterruptedException {

        return (JSONObject) DatabaseRequestHelper
                .getJSONFromURL(Constants.DEFAULT_PATH + "answer/" + question.getID(), AUTH_TOKEN)
                .get("obj0");

    }

    /**
     * @return status code
     */
    public static int registerUser(String username, String password) throws IOException, InterruptedException, JSONException {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);

        HttpResponse<String> response = DatabaseRequestHelper.postRequest(body, "users/register");

        return response.statusCode();
    }

    /**
     * Verify if login credentials were correct.
     *
     * @return status code.
     */
    public static int verifyLoginCredentials(String username, String password) throws IOException, InterruptedException, JSONException {

        JSONObject body = new JSONObject();
        body.put("password", password);
        body.put("username", username);

        HttpResponse<String> response = DatabaseRequestHelper.postRequest(body, "users/login");

        if (response.headers().firstValue("token").isPresent()) {
            AUTH_TOKEN = response.headers().firstValue("token").get();
            System.out.println(AUTH_TOKEN);
        }
        return response.statusCode();

    }

    /**
     * Post bitmap to the server.
     *
     * @return 4 digit test key.
     */
    public static String uploadTestKey(long key) throws JSONException, IOException, InterruptedException {
        JSONObject body = new JSONObject();
        body.put("key", String.valueOf(key));

        HttpResponse<String> response = DatabaseRequestHelper.postRequest(body, "database/key", AUTH_TOKEN);

        return new JSONObject(response.body()).get("key").toString();
    }

    /**
     * Get test key from server
     *
     * @return bitmap
     */
    public static long getTestKey(int key) throws InterruptedException, JSONException, IOException {
        JSONObject response = (JSONObject) DatabaseRequestHelper.getJSONFromURL("http://localhost:7080/api/database/key/" + key, AUTH_TOKEN).get("obj0");
        return Long.parseLong(response.get("bitmap").toString());
    }
}
