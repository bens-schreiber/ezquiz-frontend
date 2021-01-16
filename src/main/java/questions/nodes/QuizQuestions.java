package questions.nodes;

import gui.popup.error.ErrorNotifier;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import questions.Question;
import requests.AnswerRequest;
import requests.QuestionRequest;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utilizes requests package to load and create a set of questions for a quiz
 */
public class QuizQuestions {

    //contains the questions, responses, and javafx information
    private static QuestionNode[] questionNodes;

    private QuizQuestions() {
    }


    /**
     * Load question nodes
     *
     * @param subject if not null limits questions to that specific subject.
     * @param type    if not null limits questions to that specific type.
     */
    public static void loadQuestions(int amount, @Nullable Question.Type type, @Nullable Question.Subject subject) throws IllegalArgumentException {
        try {

            //Put JSON through QuestionListBuilder
            List<Question> questions = new QuestionListBuilder(new QuestionRequest(type, subject, amount).makeRequest().getJson(), amount)
                    .toList().verifyAmount()
                    .build();

            //Initiate the questionNodes as a static array with the actual amount possible
            questionNodes = new QuestionNode[questions.size()];

            //Turn question list into list of QuestionNodes
            int i = 0;
            for (Question question : questions) questionNodes[i++] = new QuestionNode(question);

        } catch (InterruptedException | IOException | JSONException e) {

            new ErrorNotifier("A question failed to be created.", true).display();
            questionNodes = null;
            e.printStackTrace();

        } catch (IllegalArgumentException e) {

            questionNodes = null;
            throw e;

        }
    }


    /**
     * For loading a specific test through the test key.
     * @param ids id of questions to load.
     */
    public static void loadQuestions(List<Integer> ids) {
        try {

            List<Question> questions = new QuestionListBuilder(new QuestionRequest(ids).makeRequest().getJson(), ids)
                    .toList().build();

            //Initiate the questionNodes  as a static array with the amount specified
            questionNodes = new QuestionNode[questions.size()];

            int i = 0;
            for (Question question : questions) questionNodes[i++] = new QuestionNode(question);

        } catch (Exception e) {
            new ErrorNotifier("A question failed to be created.", true).display();
            e.printStackTrace();
        }
    }

    public static void gradeAnswers() {
        try {

            //Attempt to set answers for all questions
            questionNodes = new QuestionAnswer(questionNodes).request().setAnswers().getQuestionNodes();

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

    public static QuestionNode[] getQuestionNodes() {

        return questionNodes;

    }

    private static class QuestionListBuilder {
        /**
         * Generate question list from JSONObject.
         */

        private int amount;
        private final JSONObject json;
        private List<Integer> ids;

        private final List<Question> questions = new LinkedList<>();

        public QuestionListBuilder(JSONObject json, int amount) {
            this.json = json;
            this.amount = amount;
        }

        public QuestionListBuilder(JSONObject json, List<Integer> ids) {
            this.json = json;
            this.ids = ids;
        }

        //If the amount of questions is larger than the amount of questions found in the json or 0,
        //correct it to the maximum possible
        public QuestionListBuilder verifyAmount() {
            if (amount > json.length() || amount == 0) {
                amount = json.length() - 1;
            }
            return this;
        }

        public QuestionListBuilder toList() throws JSONException {

            //If ids have been specified use specified list method
            if (ids != null) {
                return this.toSpecifiedList();
            }

            //Create a pool of question id's in the specific size of how many questions available
            List<Integer> idPool = new LinkedList<>();
            for (int i = 0; i < json.length() - 1; i++) idPool.add(i);

            //Randomize the pool of ids
            Collections.shuffle(idPool);

            for (Integer id : idPool.subList(0, amount)) {
                questions.add(questionFromJSON((JSONObject) json.get("obj" + id)));
            }

            return this;

        }

        //if ids are already given
        private QuestionListBuilder toSpecifiedList() throws JSONException {

            int i = 0;
            for (Integer ignored : this.ids) {
                questions.add(QuestionListBuilder.questionFromJSON((JSONObject) json.get("obj" + i++)));
            }
            return this;

        }

        public List<Question> build() {
            return this.questions;
        }

        /**
         * @return new Question object built from JSONObject.
         */
        private static Question questionFromJSON(JSONObject json) throws JSONException {

            Question.Type type = Question.Type.valueOf(json.get("type_name").toString());

            Question.Subject subject = Question.Subject.valueOf(json.get("subject_name").toString());

            String question = json.get("question").toString();

            int id = Integer.parseInt(json.get("question_num").toString());

            //Only need to iterate through options, use linked list ds
            LinkedList<String> options;

            if (json.has("options")) {
                options = new LinkedList<>(Arrays.asList(json.get("options").toString()
                        .split(", ")));

            } else {
                options = null;
            }

            String directions;

            directions = json.has("directions") ? json.get("directions").toString() : switch (type) {

                case MULTIPLECHOICE -> "Select the correct answer.";

                case TRUEORFALSE -> "Determine if the problem is true or false.";

                case CHECKBOX -> "Check all the boxes that apply.";

                case WRITTEN -> "Correctly type the solution.";

            };

            return new Question(type, subject, options, question, directions, id);
        }
    }

    private static class QuestionAnswer {

        private final QuestionNode[] questionNodes;
        private JSONObject json;

        private QuestionAnswer(QuestionNode[] questionNodes) {
            this.questionNodes = questionNodes;
        }

        public QuestionAnswer request() throws InterruptedException, IOException, JSONException {

            this.json = new AnswerRequest(questionNodes).makeRequest().getJson();

            return this;
        }

        public QuestionAnswer setAnswers() throws JSONException {

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
                pair.value.getQuestion()
                        .setAnswer(answerFromJSON((JSONObject) this.json.get("obj" + i++)));

                //Assign the value of the pair to its original index, not affecting the positioning of the array.
                questionNodes[pair.index] = pair.value;
            }

            return this;
        }

        public QuestionNode[] getQuestionNodes() {
            return questionNodes;
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
                return this.value.getQuestion().getID() - o.value.getQuestion().getID();
            }
        }
    }

}

