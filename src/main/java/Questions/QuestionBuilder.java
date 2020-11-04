package Questions;

import org.json.JSONException;
import org.json.JSONObject;

public class QuestionBuilder {

    public static Question questionFromJSON(JSONObject json) throws JSONException {
        String type = json.get("type_id").toString();
        String subject = json.get("subjects").toString();
        String question = json.get("question").toString();

        int id = Integer.parseInt(
                json.get("question_num").toString()
        );

        String options;
        if (json.has("options")) {
            options = json.get("options").toString();
        } else options = null;

        return new Question(type, subject, options, question, id);

    }
}
