package quiz;

import questions.Question;

import java.io.IOException;
import java.util.*;
import questions.*;
import apis.Requests;
import org.json.JSONException;

public class QuizController {
    public static HashMap<Integer, List<String>> responses = new HashMap<>();
    public static List<Question> questions = new ArrayList<>();

    //TODO: Make this into a method to grab x amount of questions. Figure out drag and drop or replace db with supported Matching node questions
    public static List<Question> getQuestions() throws IOException, JSONException {
//        Question test = new Question("5", "math", Arrays.asList("1,2,3,4".split(",")), "Get number 4", 100);
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(1)));
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(2)));
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(3)));
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(4)));
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(5)));
        return questions;

    }

    public static List<Question> checkAnswers() throws IOException, JSONException {
        List<Question> missedQuestions = new ArrayList<>();

        for (Question q : questions) { //If question key is not in responses, the question is blank, and incorrect.
            if (!responses.containsKey(q.getID())) missedQuestions.add(q);
        }

        int quizQuestNum = 0; //Iterate through questions along with hashmap, so it doesn't make unneeded requests
        for (Map.Entry<Integer, List<String>> entry : responses.entrySet()) {
            Integer id = entry.getKey();
            List<String> response = entry.getValue();

            if (!QuestionBuilder.answerFromJSON(Requests.getQuestionAnswer(id))
                    .containsAll(response)) {//Answer may be larger than one, so .containsAll is used
                missedQuestions.add(questions.get(quizQuestNum));
            }

            quizQuestNum++;
        }

        return missedQuestions;
    }

    public static void addResponse(int id, List<String> response) {
        responses.put(id, response);
    }


}

