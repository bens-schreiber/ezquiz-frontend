package classes.question;

import classes.question.fxnodes.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QuestionNodeFactory {

    /**
     * @return new Question object built from JSONObject.
     */
    private static QuestionNode questionFromJSON(JSONObject json) throws JSONException {

        if (verify(json)) {

            Question.Type type = Question.Type.valueOf(json.get("type").toString());

            String question = json.get("classes/question").toString();

            int id = Integer.parseInt(json.get("id").toString());

            //Only need to iterate through options, use linked list ds
            LinkedList<String> options = null;
            if (json.has("options")) {
                options = new LinkedList<>(Arrays.asList(json.get("options").toString()
                        .split("/")));
            }

            String directions = json.get("directions").toString().length() > 1 ? json.get("directions").toString() : switch (type) {

                case MULTIPLECHOICE -> "Select the correct answer.";

                case TRUEORFALSE -> "Determine if the problem is true or false.";

                case CHECKBOX -> "Check all the boxes that apply.";

                case WRITTEN -> "Correctly type the solution.";

            };

            //shut up
            TypeNode node = switch (type) {

                case MULTIPLECHOICE -> new MultipleChoice(options, directions, question);

                case TRUEORFALSE -> new TrueOrFalse(directions, question);

                case CHECKBOX -> new CheckBoxNode(options, directions, question);

                case WRITTEN -> new Written(directions, question);
            };


            return new QuestionNode(type, options, question, directions, id, node);
        }

        throw new JSONException("Required values not found");

    }

    //Make sure all required json pieces are here
    private static boolean verify(JSONObject json) {
        return json.has("type") && json.has("classes/question") && json.has("id");
    }

    public static QuestionNode[] nodeArrayFromJSON(JSONObject json, int amount, List<Integer> ids) throws JSONException {

        //If the amount is greater than possible, or less than 0, make maximum amount possible.
        if (amount > json.length() || amount <= 0) amount = json.length();

        QuestionNode[] nodes = new QuestionNode[amount];

        int i = 0;
        for (Integer id : ids.subList(0, amount)) {
            nodes[i++] = (QuestionNodeFactory.questionFromJSON((JSONObject) json.get("obj" + id)));
        }

        return nodes;

    }
}
