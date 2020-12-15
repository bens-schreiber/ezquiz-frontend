package quiz;

import database.DatabaseRequest;
import etc.Constants;
import gui.popups.ErrorBox;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import quiz.questions.Question;
import quiz.questions.QuestionFactory;
import quiz.questions.nodes.QuizNode;

import java.io.IOException;
import java.util.ArrayList;
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

    //List of QuizNodes that contain the questions, responses, and javafx information
    private static final List<QuizNode> quizNodes = new ArrayList<>();

    //Index of the QuizNodes the quiz is currently on
    private static int currQuestion = 0;

    /**
     * Load questions into QuizManager.questions.
     *
     * @param subject if not null limits questions to that specific subject.
     * @param type    if not null limits questions to that specific type.
     */
    public static void loadQuestions(int amount, @Nullable String subject, @Nullable String type) {

        //Create a pool of question id's in the specific size of how many questions available
        List<Integer> idPool = IntStream
                .range(1, Constants.DATABASE_SIZE)
                .boxed()
                .collect(Collectors.toList());

        //Randomize the pool
        Collections.shuffle(idPool);

        //Initialize the id variable and request variable
        int id;
        JSONObject requestData;
        for (int i = 0; i < amount; i++) {

            //Remove the element and grab its value
            id = idPool.remove(0);

            try {
                if (subject == null && type == null) {//If subject and type are null, request based on all items in db.

                    requestData = DatabaseRequest.getQuestion(id);

                } else if (subject != null && type != null) {//If both subject and type, request for both

                    requestData = DatabaseRequest.getQuestionBySubjectAndType(subject, type, id);

                } else if (subject == null) {//If only type, request for type

                    requestData = DatabaseRequest.getQuestionByType(type, id);

                } else {//If only subject, request for subject

                    requestData = DatabaseRequest.getQuestionBySubject(subject, id);

                }

                Question question = QuestionFactory.questionFromJSON(requestData);
                question.shuffleOptions();
                quizNodes.add(new QuizNode(question));

            } catch (IOException | JSONException | InterruptedException e) {
                ErrorBox.display("A question failed to load. ID: " + id, false);
                quizNodes.clear();
            }

        }
    }

    /**
     * Overloaded loadQuestions method.
     * @param ids id of questions to load.
     */
    public static void loadQuestions(List<Integer> ids) {
        for (int id : ids) {
            try {
                Question question = QuestionFactory.questionFromJSON(DatabaseRequest.getQuestion(id));
                question.shuffleOptions();
                quizNodes.add(new QuizNode(question));

            } catch (IOException | JSONException | InterruptedException e) {
                ErrorBox.display("A question failed to load. ID: " + id, false);
                quizNodes.clear();
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
                if (quizNode.getQuestion().getType().equals("input")) {

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
                //Check if answer is correct.
                quizNode.setCorrect(answer.containsAll(response));

            } catch (IOException | JSONException | InterruptedException e) {
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

    public static List<QuizNode> getQuizNodes() {

        return quizNodes;
    }

    public static QuizNode getCurrNode() {

        return quizNodes.get(currQuestion);

    }

    public static Question getCurrQuestion() {

        return quizNodes.get(currQuestion).getQuestion();

    }

    public static boolean allResponded() {

        return quizNodes.stream().noneMatch(quizNode -> quizNode.getResponse().isEmpty());

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

