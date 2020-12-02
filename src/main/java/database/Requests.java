package database;

import etc.Constants;
import quiz.questions.Question;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Request methods for getting data from RestServer
 */
public class Requests {

    /**
     * @param id specifies absolute location in server.
     * @throws JSONException on object not found
     */
    public static JSONObject getQuestion(int id) throws IOException, JSONException {

        return (JSONObject) JsonReader.readJsonFromUrl(Constants.DEFAULT_PATH + id).get("obj0");

    }

    /**
     * Limits pool of questions to those with only the specified Question Type.
     *
     * @param id JSONObject with correlating id. Not absolute id.
     */
    public static JSONObject getQuestionByType(String type, int id) throws IOException, JSONException {

        return (JSONObject) JsonReader.readJsonFromUrl(Constants.DEFAULT_PATH + "type/" + type).get("obj" + id);

    }

    /**
     * Limits pool of questions to those with only the specified Question Subject.
     *
     * @param id JSONObject with correlating id. Not absolute id.
     */
    public static JSONObject getQuestionBySubject(String subject, int id) throws IOException, JSONException {

        return (JSONObject) JsonReader.readJsonFromUrl(Constants.DEFAULT_PATH + "subject/" + subject).get("obj" + id);

    }

    /**
     * Limits pool of questions to those with only the specified question Type and Subject.
     *
     * @param id JSONObject with correlating id. Not absolute id.
     */
    public static JSONObject getQuestionBySubjectAndType(String subject, String type, int id) throws IOException, JSONException {

        return (JSONObject) JsonReader.readJsonFromUrl(Constants.DEFAULT_PATH + subject + "/" + type).get("obj" + id);

    }

    /**
     * @return JSONObject from Question Objects ID.
     */
    public static JSONObject getQuestionAnswer(Question question) throws IOException, JSONException {

        return (JSONObject) JsonReader.readJsonFromUrl(Constants.DEFAULT_PATH + "answer/" + question.getID()).get("obj0");

    }

    /**
     * @return JSONObject from id.
     */
    public static JSONObject getQuestionAnswer(int id) throws IOException, JSONException {

        return (JSONObject) JsonReader.readJsonFromUrl(Constants.DEFAULT_PATH + "answer/" + id).get("obj0");

    }
}
