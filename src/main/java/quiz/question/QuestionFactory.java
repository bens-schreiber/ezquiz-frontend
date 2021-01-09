package quiz.question;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QuestionFactory {

    /**
     * Factory method.
     * @return new Question object built from JSONObject.
     * @throws JSONException if any required Question data is missing.
     */
    public static Question questionFromJSON(JSONObject json) throws JSONException {

        Question.Type type = Question.Type.valueOf(json.get("type_name").toString());

        Question.Subject subject = Question.Subject.valueOf(json.get("subject_name").toString());

        String question = json.get("question").toString();

        int id = Integer.parseInt(json.get("question_num").toString());

        //Only need to iterate through options, use linked list ds
        LinkedList<String> options;

        if (json.has("options")) {
            options = new LinkedList<>(Arrays.asList(json.get("options").toString()
                    .split(", ")));

        } else {
            options = null;
        }

        String directions;

        directions = json.has("directions") ? json.get("directions").toString() : switch (type) {

            case MULTIPLECHOICE -> "Select the correct answer.";

            case TRUEORFALSE -> "Determine if the problem is true or false.";

            case CHECKBOX -> "Check all the boxes that apply.";

            case WRITTEN -> "Correctly type the solution.";

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
