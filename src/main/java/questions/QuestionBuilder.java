package questions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class QuestionBuilder {

    public static Question questionFromJSON(JSONObject json) throws JSONException {
        String type = json.get("type_id").toString();
        String subject = json.get("subjects").toString();
        String question = json.get("question").toString();

        int id = Integer.parseInt(
                json.get("question_num").toString()
        );

        ArrayList<String> options;
        if (json.has("options")) {
                options = new ArrayList<>(Arrays.asList(json.get("options").toString()
                    .split(", ")));
                Collections.shuffle(options); //Make order random
        }
        else options = null;


        return new Question(type, subject, options, question, id);

    }

    public static List<String> answerFromJSON(JSONObject json) throws JSONException {
        List<String> answer = new ArrayList<>(Arrays.asList(json.get("answer").toString()
                .split(", ")));

        return answer;
    }
}
