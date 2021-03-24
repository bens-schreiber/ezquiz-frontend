package com.benschreiber.gui.quiz;

import com.benschreiber.etc.Account;
import com.benschreiber.question.QuestionNodeFactory;
import com.benschreiber.requests.QuizJSONRequest;
import com.benschreiber.question.Question;
import com.benschreiber.question.QuestionNode;
import com.benschreiber.requests.AnswerJSONRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Static methods to initialize and grade classes.question nodes
 */
public class QuizQuestions {

    //contains the questions, responses, and javafx information
    private static QuestionNode[] questionNodes;

    private QuizQuestions() {
    }

    /**
     * Initialize questionNodes with specific questions, utilizes QuestionNodeArrayFactory
     *
     * @param amount amount of questions wanted. 0 if maximum possible.
     */
    public static void initializeQuiz(int amount) throws IllegalArgumentException, JSONException, IOException, InterruptedException {
        try {

            //Initialize the request, storing json in the object.
            QuizJSONRequest request = new QuizJSONRequest(Account.getUser())
                    .initializeRequest();

            //Create a pool of classes.question id's in the size of how many questions available, randomize order
            List<Integer> idPool = new LinkedList<>();
            for (int i = 0; i < request.getQuestions().length(); i++) {
                idPool.add(i);
            }

            Collections.shuffle(idPool);

            //Initialize preferences
            QuizHelper.initializePreferences(request.getPreferences());

            //Initialize questions
            questionNodes = QuestionNodeFactory.arrayFromJSON(request.getQuestions(), amount, idPool);

        } catch (Exception e) {

            //Rethrow exception but get rid of any questions that are invalidly loaded.
            questionNodes = null;
            throw e;
        }
    }

    public static QuestionNode[] getQuestionNodes() {

        return questionNodes;

    }

    /**
     * Grades stored QuestionNodes utilizing QuestionAnswerHelper class
     */
    public static void gradeAnswers() throws InterruptedException, IOException, JSONException {

        //Attempt to set answers for all questions
        QuestionAnswerHelper.setAnswers(questionNodes, new AnswerJSONRequest(questionNodes).initializeRequest().getJson());

        for (QuestionNode questionNode : questionNodes) {

            if (questionNode.isAnswered()) {

                List<String> response = questionNode.getResponse();
                List<String> answer = questionNode.getAnswer();

                //user input type might be capitalized or spaced wrong. handle differently
                if (questionNode.getType() == Question.Type.WRITTEN) {
                    //Set to all lowercase and no spaces for minimal input based error
                    response = response.stream()
                            .map(String::toLowerCase)
                            .map(str -> str.replaceAll("\\s", ""))
                            .collect(Collectors.toList());

                    answer = questionNode.getAnswer().stream()
                            .map(String::toLowerCase)
                            .map(str -> str.replaceAll("\\s", ""))
                            .collect(Collectors.toList());
                }

                //Answer may be larger than one, so .containsAll is used
                //Check if answer is correct, if no response then mark wrong.
                questionNode.setCorrect(answer.containsAll(response));

            } else {
                questionNode.setCorrect(false);
            }
        }
    }

    private static class QuestionAnswerHelper {

        /**
         * Set all quizNodes answers
         */
        static void setAnswers(QuestionNode[] questionNodes, JSONObject json) throws JSONException {

            /*
            Because the answers in the rest server are by default sorted by smallest to greatest ID, a pair class is made
            so that the pair list is sorted and given correct answers, without effecting the positioning of the questions
            array (which would screw up positioning in Results)
            */

            //Fill pair array with quiz node and original index
            Pair[] pairs = IntStream.range(0, questionNodes.length).mapToObj(i -> new Pair(questionNodes[i], i)).toArray(Pair[]::new);

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
            public int compareTo(@NotNull QuizQuestions.QuestionAnswerHelper.Pair o) {
                return this.value.getID() - o.value.getID();
            }
        }
    }
}

