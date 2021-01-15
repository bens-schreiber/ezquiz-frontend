package quiz;

import etc.Preference;
import gui.popup.error.ErrorNotifier;
import quiz.nodes.QuestionNode;
import quiz.question.Question;
import requests.AnswerRequest;
import requests.QuestionRequest;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Initializes and stores questionNodes, contains grading methods
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

    //contains the questions, responses, and javafx information
    private static QuestionNode[] questionNodes;


    /**
     * Load questions into QuizManager.questions.
     *
     * @param subject if not null limits questions to that specific subject.
     * @param type    if not null limits questions to that specific type.
     */
    public static void loadQuestions(int amount, @Nullable Question.Type type, @Nullable Question.Subject subject) {

        //Initiate the questionNodes as a static array with the amount specified
        questionNodes = new QuestionNode[amount];

        try {
            QuestionRequest request = new QuestionRequest(type, subject, amount).makeRequest();
            int i = 0;
            for (Question question : request.toList()) {
                questionNodes[i++] = new QuestionNode(question);
            }

        } catch (Exception e) {
            new ErrorNotifier("A question failed to be created.", true).display();
            e.printStackTrace();
        }

    }

    /**
     * For loading a specific test through the test key.
     *
     * @param ids id of questions to load.
     */
    public static void loadQuestions(List<Integer> ids) {

        //Initiate the questionNodes  as a static array with the amount specified
        questionNodes = new QuestionNode[ids.size()];

        try {

            //Count loop iterations for putting into questionNodes
            QuestionRequest request = new QuestionRequest(ids).makeRequest();

            int i = 0;
            for (Question question : request.toList()) {
                questionNodes[i++] = new QuestionNode(question);
            }

        } catch (Exception e) {
            new ErrorNotifier("A question failed to be created.", true).display();
            e.printStackTrace();
        }
    }


    /**
     * Grab all answers to questions from responses with Requests.getQuestionAnswer
     */
    public static void gradeAnswers() {

        try {

            //Attempt to set answers for all questions
            questionNodes = new AnswerRequest(questionNodes).makeRequest().setAnswers();

        } catch (Exception e) {
            new ErrorNotifier("A question failed to be graded.", true).display();
            e.printStackTrace();
        }

        for (QuestionNode questionNode : questionNodes) {

            if (questionNode.isAnswered()) {

                List<String> response = questionNode.getResponse();
                List<String> answer = questionNode.getQuestion().getAnswer();

                //user input type might be capitalized or spaced wrong. handle differently
                if (questionNode.getQuestion().getType() == Question.Type.WRITTEN) {

                    //Set to all lowercase and no spaces for minimal input based error
                    response = response.stream()
                            .map(String::toLowerCase)
                            .map(str -> str.replaceAll("\\s", ""))
                            .collect(Collectors.toList());

                    answer = questionNode.getQuestion().getAnswer().stream()
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


    /**
     * Getters
     */

    public static HashMap<Preference, String> getPreferences() {
        return preferences;
    }

    public static QuestionNode[] getStages() {

        return questionNodes;

    }


}

