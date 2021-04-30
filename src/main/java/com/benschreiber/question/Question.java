package com.benschreiber.question;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Stores necessary information to display a questions text elements
 */
public class Question {

    public enum Type {MULTIPLECHOICE, TRUEORFALSE, CHECKBOX, WRITTEN}

    private final Type type;
    private final int id;
    private final String prompt, directions;
    private List<String> answer;
    private final List<String> options;

    /**
     * Class constructor.
     * Answer is null until specifically set.
     */
    public Question(Type type, List<String> options, String prompt, String directions, int id) {
        this.type = type;
        this.options = options;
        this.prompt = prompt;
        this.directions = directions;
        this.id = id;
    }

    public Question(Question question) {
        this.type = question.type;
        this.options = question.options;
        this.prompt = question.prompt;
        this.directions = question.directions;
        this.id = question.id;
        this.answer = question.answer;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getPrompt() {
        return prompt;
    }

    public Type getType() {
        return type;
    }

    public String getDirections() {
        return directions;
    }

    public int getID() {
        return id;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    /**
     * Factory
     */
    public static class Factory {

        //Factory Method
        public static Question[] arrayFromJSON(JSONObject json, int amount, List<Integer> ids) throws JSONException {

            //If the amount is greater than possible, or less than 0, make maximum amount possible.
            if (amount > json.length() || amount <= 0) { amount = json.length(); }

            Question[] nodes = new Question[amount];

            int i = 0;
            for (Integer id : ids.subList(0, amount)) {
                nodes[i++] = (questionFromJSON((JSONObject) json.get("obj" + id)));
            }

            return nodes;

        }

        //Verify the given JSON is valid
        private static boolean validate(JSONObject json) {
            return json.has("type") && json.has("question") && json.has("id");
        }

        //Create a single question from JSON
        private static Question questionFromJSON(JSONObject json) throws JSONException {

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

                return new Question(type, options, question, directions, id);
            }

            throw new JSONException("Required values not found");

        }

    }

}
