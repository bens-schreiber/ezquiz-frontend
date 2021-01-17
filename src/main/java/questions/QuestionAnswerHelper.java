package questions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import questions.question.QuestionNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class QuestionAnswerHelper {

    static void setAnswers(QuestionNode[] questionNodes, JSONObject json) throws JSONException {

        /*
        Because the answers in the rest server are by default sorted by smallest to greatest ID, a pair class is made
        so that the pair list is sorted and given correct answers, without effecting the positioning of the questions
        array (which would screw up positioning in Results)
        */

        //Fill pair array with quiz node and original index
        Pair[] pairs = new Pair[questionNodes.length];
        for (int i = 0; i < questionNodes.length; i++) {
            pairs[i] = new Pair(questionNodes[i], i);
        }

        //Sort the pair array so that it may be compared to the already sorted json.
        Arrays.sort(pairs);
        int i = 0;
        for (Pair pair : pairs) {
            //Get the correct answer based off the correlating position in the json
            pair.value.setAnswer(answerFromJSON((JSONObject) json.get("obj" + i++)));

            //Assign the value of the pair to its original index, not affecting the positioning of the array.
            questionNodes[pair.index] = pair.value;
        }
    }

    private static List<String> answerFromJSON(JSONObject json) throws JSONException {
        return new ArrayList<>(
                Arrays.asList(json.get("answer").toString()
                        .split(", ")));
    }

    private static class Pair implements Comparable<Pair> {

        public final QuestionNode value;
        public final int index;

        public Pair(QuestionNode questionNode, int index) {
            this.value = questionNode;
            this.index = index;
        }

        @Override
        public int compareTo(@NotNull Pair o) {
            return this.value.getID() - o.value.getID();
        }
    }
}
