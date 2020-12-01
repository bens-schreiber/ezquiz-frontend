package quiz.questions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains method to create a question object from its initial json
 */

public class QuestionFactory {

    //Factory method
    public static Question questionFromJSON(JSONObject json) throws JSONException {

        String type = json.get("type_name").toString();

        String subject = json.get("subject_name").toString();

        String question = json.get("question").toString();

        int id = Integer.parseInt(json.get("question_num").toString());

        String directions;

        ArrayList<String> options;


        if (json.has("options")) {

            options = new ArrayList<>(Arrays.asList(json.get("options").toString()
                    .split(", ")));

        } else {

            options = null;

        }

        if (json.has("directions")) {

            directions = json.get("directions").toString();

        } else {

            directions = switch (type) {

                case "multiple" -> "Select the correct answer.";

                case "t_f" -> "Determine if the problem is true or false.";

                case "checkbox" -> "Check all the boxes that apply.";

                default -> "Correctly type the solution.";

            };
        }

        return new Question(type, subject, options, question, directions, id);

    }

    public static List<String> answerFromJSON(JSONObject json) throws JSONException {

        return new ArrayList<>(
                Arrays.asList(json.get("answer").toString()
                        .split(", ")));

    }

}
