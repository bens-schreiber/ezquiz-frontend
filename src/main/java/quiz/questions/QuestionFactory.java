package quiz.questions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionFactory {

    /**
     * Factory method.
     * @return new Question object built from JSONObject.
     * @throws JSONException if any required Question data is missing.
     */
    public static Question questionFromJSON(JSONObject json) throws JSONException {

        String type = json.get("type_name").toString();

        String subject = json.get("subject_name").toString();

        String question = json.get("question").toString();

        int id = Integer.parseInt(json.get("question_num").toString());

        ArrayList<String> options;

        if (json.has("options")) {
            options = new ArrayList<>(Arrays.asList(json.get("options").toString()
                    .split(", ")));

        } else {
            options = null;
        }

        String directions;

        directions = json.has("directions") ? json.get("directions").toString() : switch (type) {

            case "multiple" -> "Select the correct answer.";

            case "t_f" -> "Determine if the problem is true or false.";

            case "checkbox" -> "Check all the boxes that apply.";

            default -> "Correctly type the solution.";

        };

        return new Question(type, subject, options, question, directions, id);


    }

    /**
     * @return answer array from JSON.
     * @throws JSONException if required data is missing.
     */
    public static List<String> answerFromJSON(JSONObject json) throws JSONException {

        return new ArrayList<>(
                Arrays.asList(json.get("answer").toString()
                        .split(", ")));

    }


}
