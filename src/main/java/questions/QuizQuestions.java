package questions;

import gui.popup.error.ErrorNotifier;
import org.json.JSONException;
import org.json.JSONObject;
import questions.question.Question;
import questions.question.QuestionNode;
import requests.AnswerRequest;
import requests.QuestionRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores a static array of questionNodes to be accessed by QuizController for display
 */
public class QuizQuestions {

    //contains the questions, responses, and javafx information
    private static QuestionNode[] questionNodes;

    private QuizQuestions() {
    }

    /**
     * Initialize questionNodes with specific questions, utilizes QuestionNodeArrayFactory
     *
     * @param amount          amount of questions wanted, corrected if impossible
     * @param questionRequest specific request object that should be utilized
     */
    public static void initializeQuestions(int amount, QuestionRequest questionRequest) throws IllegalArgumentException {
        try {


            //Make the request and get the JSON.
            JSONObject json = questionRequest.makeRequest().getJson();


            //If the amount is greater than possible, or less than 0, make maximum amount possible.
            if (amount > json.length() || amount <= 0)
                amount = json.length();


            //Instantiate nodes with amount.
            questionNodes = new QuestionNode[amount];


            //Create a pool of question id's in the specific size of how many questions available
            List<Integer> idPool = new LinkedList<>();
            for (int i = 0; i < json.length() - 1; i++) idPool.add(i);


            //Randomize the pool of ids
            Collections.shuffle(idPool);


            int i = 0;
            for (Integer id : idPool.subList(0, amount)) {
                questionNodes[i++] = (QuestionNodeFactory.questionFromJSON((JSONObject) json.get("obj" + id)));
            }


        } catch (InterruptedException | IOException | JSONException e) {

            new ErrorNotifier("A question failed to be created.", true).display();
            questionNodes = null;
            e.printStackTrace();

        } catch (IllegalArgumentException e) {

            //Rethrow exception but get rid of any questions that could have been invalidly loaded
            questionNodes = null;
            throw e;

        }
    }

    /**
     * For loading a specific test through the test key.
     *
     * @param ids id of questions to load.
     */
    public static void initializeQuestions(List<Integer> ids) {
        try {

            JSONObject json = new QuestionRequest(ids).makeRequest().getJson();
            questionNodes = new QuestionNode[json.length()];

            int i = 0;
            for (Integer ignored : ids) {
                questionNodes[i] = (QuestionNodeFactory.questionFromJSON((JSONObject) json.get("obj" + i++)));
            }

        } catch (Exception e) {
            new ErrorNotifier("A question failed to be created.", true).display();
            e.printStackTrace();
        }
    }

    /**
     * Grades stored QuestionNodes utilizing static QuestionAnswerHelper class
     */
    public static void gradeAnswers() {
        try {

            //Attempt to set answers for all questions
            QuestionAnswerHelper.setAnswers(questionNodes, new AnswerRequest(questionNodes).makeRequest().getJson());

        } catch (Exception e) {
            new ErrorNotifier("A question failed to be graded.", true).display();
            e.printStackTrace();
        }

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

    public static QuestionNode[] getQuestionNodes() {

        return questionNodes;

    }

}
