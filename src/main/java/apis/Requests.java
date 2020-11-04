package apis;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Requests {

    public static JSONObject getRandQuestion() throws IOException, JSONException {
        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/"
                + getRandomID(0, 50)).get("obj0");
    }

    public static JSONObject getQuestionAnswerByID(int ID) throws IOException, JSONException {
        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/answer"
                + ID).get("obj0");
    }


    public static JSONObject getQuestionByType(String type) throws IOException, JSONException {
        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/type/" + type)
                .get("obj" + getRandomID(0, 9));
    }

    public static JSONObject getQuestionBySubject(String subject) throws IOException, JSONException {
        return (JSONObject) JsonReader.readJsonFromUrl("http://localhost:7080/api/database/questions/subject/" + subject)
                .get("obj" + getRandomID(0, 4));
    }

    private static int getRandomID(int lower, int upper) {
        return lower + (int)(Math.random() * (upper - lower) + 1);
    }
}
