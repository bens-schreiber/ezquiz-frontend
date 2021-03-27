package com.benschreiber.question;

import com.benschreiber.question.fxnodes.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class QuestionNodeFactory {

    //Verify the given JSON is valid
    private static boolean validate(JSONObject json) {
        return json.has("type") && json.has("question") && json.has("id");
    }

    /**
     * @return new QuestionNode built from JSONObject.
     */
    private static QuestionNode questionFromJSON(JSONObject json) throws JSONException {

        if (validate(json)) {

            Question.Type type = Question.Type.valueOf(json.get("type").toString());

            String question = json.get("question").toString();

            int id = Integer.parseInt(json.get("id").toString());

            //
            List<String> options = json.has("options")
                    ? new LinkedList<>(Arrays.asList(json.get("options").toString()
                    .split("/"))) : null;

            if (options != null) Collections.shuffle(options);


            //apply default directions if no given directions
            String directions = json.get("directions").toString().length() > 1
                    ? json.get("directions").toString() : switch (type) {

                case MULTIPLECHOICE -> "Select the correct answer.";

                case TRUEORFALSE -> "Determine if the problem is true or false.";

                case CHECKBOX -> "Check all the boxes that apply.";

                case WRITTEN -> "Correctly type the solution.";

            };

            TypeNode node = switch (type) {

                case MULTIPLECHOICE -> new MultipleChoice(options, directions, question);

                case TRUEORFALSE -> new TrueOrFalse(directions, question);

                case CHECKBOX -> new CheckBoxNode(options, directions, question);

                case WRITTEN -> new Written(directions, question);
            };

            return new QuestionNode(type, question, directions, id, node);
        } //build the QuestionNode

        throw new JSONException("Required values not found");

    }

    public static QuestionNode[] arrayFromJSON(JSONObject json, int amount, List<Integer> ids) throws JSONException {

        //If the amount is greater than possible, or less than 0, make maximum amount possible.
        if (amount > json.length() || amount <= 0) { amount = json.length(); }

        QuestionNode[] nodes = new QuestionNode[amount];

        int i = 0;
        for (Integer id : ids.subList(0, amount)) {
            nodes[i++] = (QuestionNodeFactory.questionFromJSON((JSONObject) json.get("obj" + id)));
        }

        return nodes;

    }
}
