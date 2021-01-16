package requests;

import etc.Constants;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import quiz.question.QuestionFactory;
import quiz.question.nodes.QuestionNode;

import java.io.IOException;
import java.util.Arrays;

/**
 * Makes a request to the database that assigns the correct answers to each question of the quiz node array.
 */
public class AnswerRequest extends Request {

    private final QuestionNode[] questionNodes;
    private JSONObject json;

    /**
     * @param questionNodes array of the questions that need to be given answers
     */
    public AnswerRequest(QuestionNode[] questionNodes) {
        this.questionNodes = questionNodes;
    }

    public AnswerRequest makeRequest() throws InterruptedException, JSONException, IOException {

        StringBuilder stringBuilder = new StringBuilder().append(Constants.DEFAULT_PATH).append("answer/");

        for (QuestionNode questionNode : this.questionNodes) {
            stringBuilder.append(questionNode.getQuestion().getID()).append(",");
        }

        //get rid of the last "," because it isn't needed
        this.json = getJSONFromURL(stringBuilder.substring(0, stringBuilder.toString().length() - 1), Account.AUTH_TOKEN());

        return this;
    }

    /**
     * @return QuizNodes with questions that have answers set.
     */
    public QuestionNode[] setAnswers() throws JSONException {

        /*
        Because the answers in the rest server are by default sorted by smallest to greatest ID, a pair class is made
        so that the pair list is sorted and given correct answers, without effecting the positioning of the questions
        array
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
            pair.value.getQuestion()
                    .setAnswer(QuestionFactory.answerFromJSON((JSONObject) this.json.get("obj" + i++)));

            //Assign the value of the pair to its original index, not affecting the positioning of the array.
            questionNodes[pair.index] = pair.value;

        }

        return this.questionNodes;
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
            return this.value.getQuestion().getID() - o.value.getQuestion().getID();
        }
    }

}
