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
public class Requests {

    private static String AUTH_TOKEN;

    /**
     * @param id specifies absolute location in server.
     * @throws JSONException on object not found
     */
    public static JSONObject getQuestion(int id) throws IOException, JSONException, InterruptedException {

        return (JSONObject) RequestsHelper.getJSONFromURL(Constants.DEFAULT_PATH + id, AUTH_TOKEN).get("obj0");

    }

    /**
     * Limits pool of questions to those with only the specified Question Type.
     *
     * @param id JSONObject with correlating id. Not absolute id.
     */
    public static JSONObject getQuestionByType(String type, int id) throws IOException, JSONException, InterruptedException {

        return (JSONObject) RequestsHelper
                .getJSONFromURL(Constants.DEFAULT_PATH + "type/" + type, AUTH_TOKEN)
                .get("obj" + id);

    }

    /**
     * Limits pool of questions to those with only the specified Question Subject.
     *
     * @param id JSONObject with correlating id. Not absolute id.
     */
    public static JSONObject getQuestionBySubject(String subject, int id) throws IOException, JSONException, InterruptedException {

        return (JSONObject) RequestsHelper
                .getJSONFromURL(Constants.DEFAULT_PATH + "subject/" + subject, AUTH_TOKEN)
                .get("obj" + id);

    }

    /**
     * Limits pool of questions to those with only the specified question Type and Subject.
     *
     * @param id JSONObject with correlating id. Not absolute id.
     */
    public static JSONObject getQuestionBySubjectAndType(String subject, String type, int id) throws IOException, JSONException, InterruptedException {

        return (JSONObject) RequestsHelper
                .getJSONFromURL(Constants.DEFAULT_PATH + subject + "/" + type, AUTH_TOKEN)
                .get("obj" + id);

    }

    /**
     * @return JSONObject from Question Objects ID.
     */
    public static JSONObject getQuestionAnswer(Question question) throws IOException, JSONException, InterruptedException {

        return (JSONObject) RequestsHelper
                .getJSONFromURL(Constants.DEFAULT_PATH + "answer/" + question.getID(), AUTH_TOKEN)
                .get("obj0");

    }

    public static int registerUser(String username, String password) throws IOException, InterruptedException, JSONException {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);

        HttpResponse<String> response = RequestsHelper.postRequest(body, "register");

        return response.statusCode();
    }

    public static int verifyLoginCredentials(String username, String password) throws IOException, InterruptedException, JSONException {

        JSONObject body = new JSONObject();
        body.put("password", password);
        body.put("username", username);

        HttpResponse<String> response = RequestsHelper.postRequest(body, "login");

        if (response.headers().firstValue("token").isPresent()) {
            AUTH_TOKEN = response.headers().firstValue("token").get();
        }
        return response.statusCode();

    }
}
