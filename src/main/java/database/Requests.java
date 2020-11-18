package database;

import quiz.questions.Question;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Requests {

    public static JSONObject getQuestion(int id) throws IOException, JSONException {

        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/"
                + id).get("obj0");

    }

    public static JSONObject getQuestionByType(String type, int id) throws IOException, JSONException {

        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/type/" + type)
                .get("obj" + id);

    }

    public static JSONObject getQuestionBySubject(String subject, int id) throws IOException, JSONException {

        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/subject/" + subject)
                .get("obj" + id);

    }

    public static JSONObject getQuestionBySubjectAndType(String subject, String type, int id) throws IOException, JSONException {

        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/" + subject + "/" + type)
                .get("obj" + id);

    }

    public static JSONObject getQuestionAnswer(Question question) throws IOException, JSONException {

        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/answer/"
                + question.getID()).get("obj0");

    }

    public static JSONObject getQuestionAnswer(int id) throws IOException, JSONException {

        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/answer/"
                + id).get("obj0");

    }
}
