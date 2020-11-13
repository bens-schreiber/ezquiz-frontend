package database;

import quiz.questions.Question;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Requests {

    public static JSONObject getQuestion() throws IOException, JSONException {
        return getQuestion(getRandomID(0, 40));
    }


    public static JSONObject getQuestion(int id) throws IOException, JSONException {
        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/"
                + id).get("obj0");
    }


    public static JSONObject getQuestionAnswer(Question question) throws IOException, JSONException {
        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/answer/"
                + question.getID()).get("obj0");
    }

    public static JSONObject getQuestionAnswer(int id) throws IOException, JSONException {
        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/answer/"
                + id).get("obj0");
    }


    public static JSONObject getQuestionByType(String type) throws IOException, JSONException {
        return getQuestionByType(type, getRandomID(0, 9));
    }


    public static JSONObject getQuestionByType(String type, int id) throws IOException, JSONException {
        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/type/" + type)
                .get("obj" + id);
    }


    public static JSONObject getQuestionBySubject(String subject) throws IOException, JSONException {
        return getQuestionBySubject(subject, getRandomID(0, 7));
    }


    public static JSONObject getQuestionBySubject(String subject, int id) throws IOException, JSONException {
        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/subject/" + subject)
                .get("obj" + id);
    }


    private static int getRandomID(int lower, int upper) {
        return ThreadLocalRandom.current().nextInt(lower, upper + 1);
    }
}
