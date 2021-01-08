package quiz;

import database.DatabaseRequest;
import etc.Constants;
import gui.popups.ErrorBox;
import quiz.questions.Question;
import quiz.questions.QuestionFactory;
import quiz.questions.nodes.QuizNode;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Stores local quiz information, contains grading methods
 */
public class QuizManager {

    //Hashmap of user preferences
    private static final HashMap<String, String> preferences = new HashMap<>();

    //Array of QuizNodes that contain the questions, responses, and javafx information
    private static QuizNode[] quizNodes;

    //Index of the QuizNodes the quiz is currently on
    private static int currQuestion = 0;

    /**
     * Load questions into QuizManager.questions.
     *
     * @param subject if not null limits questions to that specific subject.
     * @param type    if not null limits questions to that specific type.
     */
    public static void loadQuestions(int amount, @Nullable Question.Type type, @Nullable Question.Subject subject) {

        //Initiate array with set amount
        quizNodes = new QuizNode[amount];

        //Create a pool of question id's in the specific size of how many questions available
        Integer[] array = IntStream.range(1, Constants.DATABASE_SIZE).boxed().toArray(Integer[]::new);
        List<Integer> idPool = Arrays.asList(array);

        //Randomize the pool
        Collections.shuffle(idPool);


        //Initialize the id variable and request
        DatabaseRequest request = new DatabaseRequest(type, subject);
        int i = 0;
        for (Integer id : idPool.subList(0, amount)) {
            try {
                request.setId(id);
                Question question = QuestionFactory.questionFromJSON(request.makeRequest().getJSON());
                question.shuffleOptions();
                quizNodes[i] = new QuizNode(question);
                i++;

            } catch (Exception e) {
                ErrorBox.display("A question failed to load. ID: " + id, false);
                quizNodes = null;
            }

        }
    }

    /**
     * @param ids id of questions to load.
     */
    public static void loadQuestions(List<Integer> ids) {

        DatabaseRequest request = new DatabaseRequest(null, null);
        int i = 0;
        for (Integer id : ids) {
            try {
                request.setId(id);

                Question question = QuestionFactory.questionFromJSON(request.makeRequest().getJSON());

                question.shuffleOptions();
                quizNodes[i] = new QuizNode(question);
                i++;

            } catch (Exception e) {
                ErrorBox.display("A question failed to load. ID: " + id, false);
                quizNodes = null;
                e.printStackTrace();
            }
        }
    }


    /**
     * Grab all answers to questions from responses with Requests.getQuestionAnswer
     */
    public static void checkAnswers() {

        //Iterate through quiz.questions along with hashmap, so it doesn't make unneeded requests
        for (QuizNode quizNode : quizNodes) {
            try {

                List<String> response = quizNode.getResponse();

                List<String> answer = QuestionFactory.answerFromJSON(DatabaseRequest.getQuestionAnswer(quizNode.getQuestion()));

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
                quizNode.setCorrect(answer.containsAll(response) && !response.isEmpty());

            } catch (Exception e) {
                ErrorBox.display("A question failed to be graded. ID: " + quizNode.getQuestion().getID(), true);
                e.printStackTrace();
            }
        }
    }


    /**
     * Getters
     */

    public static int getCurrNum() {

        return currQuestion;

    }

    public static HashMap<String, String> getPreferences() {

        return preferences;
    }

    public static QuizNode[] getQuizNodes() {

        return quizNodes;
    }

    public static QuizNode getCurrNode() {

        return quizNodes[currQuestion];

    }

    public static Question getCurrQuestion() {

        return quizNodes[currQuestion].getQuestion();

    }

    public static boolean allResponded() {

        return Arrays.stream(quizNodes).noneMatch(quizNode -> quizNode.getResponse().isEmpty());

    }


    /**
     * Setters
     */

    public static void nextQuestion() {

        currQuestion++;

    }

    public static void prevQuestion() {

        currQuestion--;

    }

    public static void setCurrNum(int num) {

        currQuestion = num;

    }

}

