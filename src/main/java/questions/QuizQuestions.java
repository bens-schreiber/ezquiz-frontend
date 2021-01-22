package questions;

import gui.popup.error.ErrorNotifier;
import org.json.JSONException;
import questions.question.Question;
import questions.question.QuestionNode;
import requests.AnswerJSONRequest;
import requests.QuestionJSONRequest;

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
     * @param amount              amount of questions wanted, corrected if impossible
     * @param questionJSONRequest specific request object that should be utilized
     */
    public static void initializeQuestions(int amount, QuestionJSONRequest questionJSONRequest) throws IllegalArgumentException {
        try {

            //Make the request on the given QuestionJSONRequest, storing the json in the object.
            questionJSONRequest.makeRequest();

            //Create a pool of question id's in the size of how many questions available, randomize order
            List<Integer> idPool = new LinkedList<>();

            for (int i = 0; i < questionJSONRequest.getJson().length() - 1; i++) {
                idPool.add(i);
            }

            Collections.shuffle(idPool);

            questionNodes = QuestionNodeFactory.nodeArrayFromJSON(questionJSONRequest.getJson(), amount, idPool);

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

            QuestionJSONRequest questionJSONRequest = new QuestionJSONRequest(ids).makeRequest();
            questionNodes = QuestionNodeFactory.nodeArrayFromJSON(questionJSONRequest.getJson(), ids.size(), ids);

        } catch (Exception e) {
            new ErrorNotifier("A question failed to be created.", true).display();
            e.printStackTrace();
        }
    }

    /**
     * Grades stored QuestionNodes utilizing QuestionAnswerHelper class
     */
    public static void gradeAnswers() {
        try {

            //Attempt to set answers for all questions
            QuestionAnswerHelper.setAnswers(questionNodes, new AnswerJSONRequest(questionNodes).makeRequest().getJson());

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

