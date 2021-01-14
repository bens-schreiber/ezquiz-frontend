package quiz;

import etc.Preference;
import gui.popup.error.ErrorNotifier;
import org.json.JSONException;
import quiz.nodes.QuizNode;
import quiz.question.Question;
import requests.DatabaseRequest;
import requests.QuestionRequest;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores local quiz information, contains grading methods
 */


public class QuizManager {

    //Hashmap of user preferences, initialize default preferences
    private static final HashMap<Preference, String> preferences = new HashMap<>();
    static {
        preferences.put(Preference.NOTEPAD, "true");
        preferences.put(Preference.CALCULATOR, "true");
        preferences.put(Preference.DRAWINGPAD, "true");
        preferences.put(Preference.QUIZNAME, "FBLA QUIZ - 5 Questions, Random");
        preferences.put(Preference.SHOWANSWERS, "true");
        preferences.put(Preference.TIME, "1800");
    }

    //LinkedList of QuizNodes that contain the questions, responses, and javafx information
    private static QuizNode[] quizNodes;

    //Index of the QuizNodes the quiz is currently showing
    private static int currentQuestionIndex = 0;


    /**
     * Load questions into QuizManager.questions.
     *
     * @param subject if not null limits questions to that specific subject.
     * @param type    if not null limits questions to that specific type.
     */
    public static void loadQuestions(int amount, @Nullable Question.Type type, @Nullable Question.Subject subject) {

        //Initiate the quizNodes as a static array with the amount specified
        quizNodes = new QuizNode[amount];

        //Count loop iterations for putting into QuizNodes
        QuestionRequest request = new QuestionRequest(type, subject, amount).makeRequest();
        int i = 0;
        try {
            for (Question question : request.toList()) {
                quizNodes[i++] = new QuizNode(question);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * For loading a specific test through the test key.
     *
     * @param ids id of questions to load.
     */
    public static void loadQuestions(List<Integer> ids) {

        //Initiate the quizNodes as a static array with the amount specified
        quizNodes = new QuizNode[ids.size()];

        //Count loop iterations for putting into QuizNodes
        QuestionRequest request = new QuestionRequest(ids).makeRequest();
        int i = 0;
        try {
            for (Question question : request.toList()) {
                quizNodes[i++] = new QuizNode(question);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Grab all answers to questions from responses with Requests.getQuestionAnswer
     */
    public static void checkAnswers() {

        for (QuizNode quizNode : quizNodes) {
            try {

                if (quizNode.isAnswered()) {

                    List<String> response = quizNode.getResponse();

                    List<String> answer = DatabaseRequest.getQuestionAnswer(quizNode.getQuestion());

                    //set question answer for use in Results
                    quizNode.getQuestion().setAnswer(answer);

                    //user input type might be capitalized or spaced wrong. handle differently
                    if (quizNode.getQuestion().getType() == Question.Type.WRITTEN) {

                        //Set to all lowercase and no spaces for minimal input based error
                        response = response.stream()
                                .map(String::toLowerCase)
                                .map(str -> str.replaceAll("\\s", ""))
                                .collect(Collectors.toList());

                        answer = answer.stream()
                                .map(String::toLowerCase)
                                .map(str -> str.replaceAll("\\s", ""))
                                .collect(Collectors.toList());
                    }

                    //Answer may be larger than one, so .containsAll is used
                    //Check if answer is correct, if no response then mark wrong.
                    quizNode.setCorrect(answer.containsAll(response));

                } else {
                    quizNode.setCorrect(false);
                    quizNode.getQuestion().setAnswer(DatabaseRequest.getQuestionAnswer(quizNode.getQuestion()));
                }

            } catch (Exception e) {

                new ErrorNotifier("A question failed to be graded. ID: " + quizNode.getQuestion().getID(), true)
                        .display();

                e.printStackTrace();
            }
        }
    }


    /**
     * Getters
     */

    public static int getCurrNum() {

        return currentQuestionIndex;

    }

    public static HashMap<Preference, String> getPreferences() {

        return preferences;
    }

    public static QuizNode[] getQuizNodes() {

        return quizNodes;

    }

    public static QuizNode getCurrentNode() {

        return quizNodes[currentQuestionIndex];

    }

    public static Question getCurrentQuestion() {

        return quizNodes[currentQuestionIndex].getQuestion();

    }

    public static boolean allResponded() {

        return List.of(quizNodes).stream().allMatch(QuizNode::isAnswered);

    }


    /**
     * Setters
     */

    public static void nextQuestion() {

        currentQuestionIndex++;

    }

    public static void prevQuestion() {

        currentQuestionIndex--;

    }

    public static void setCurrentNum(int num) {

        currentQuestionIndex = num;

    }

}

