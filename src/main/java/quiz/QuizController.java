package quiz;

import questions.Question;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import questions.*;
import apis.Requests;
import org.json.JSONException;

public class QuizController {
    public static HashMap<Integer, List<String>> responses = new HashMap<>();
    public static List<Question> questions = new ArrayList<>();


    public static List<Question> getQuestions() throws IOException, JSONException {
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(7)));
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(8)));
        return questions;
    }

//    public static List<Question> getQuestions(int amount) throws IOException, JSONException {
//        for (int i = 0; i <= amount; i++) {
//            Question q = QuestionBuilder.questionFromJSON(Requests.getQuestion());
//            if (!questions.contains(q)) questions.add(q);
//        }
//
//        return questions;
//
//    }

    public static List<Question> checkAnswers() throws IOException, JSONException {
        List<Question> missedQuestions = new ArrayList<>();

        for (Question q : questions) { //If question key is not in responses, the question is blank, and incorrect.
            if (!responses.containsKey(q.getID())) missedQuestions.add(q);
        }

        int quizQuestNum = 0; //Iterate through questions along with hashmap, so it doesn't make unneeded requests
        for (Map.Entry<Integer, List<String>> entry : responses.entrySet()) {
            Integer id = entry.getKey();
            List<String> response = entry.getValue();
            List<String> answers = QuestionBuilder.answerFromJSON(Requests.getQuestionAnswer(id));

            //user input type questions might be capitalized or spaced wrong
            if (questions.get(quizQuestNum).getType().equals("4"))
                userInputAnswerHandle(response, answers, missedQuestions, quizQuestNum);


            else if (!answers.containsAll(response)) {//Answer may be larger than one, so .containsAll is used
                missedQuestions.add(questions.get(quizQuestNum));
            }

            quizQuestNum++;
        }

        return missedQuestions;
    }

    private static void userInputAnswerHandle(List<String> response, List<String> answers, List<Question> missedQuestions, int quizQuestNum) {
        response = response.stream()
                .map(String::toLowerCase)
                .map(str -> str.replaceAll("\\s", ""))
                .collect(Collectors.toList());

        answers = answers.stream()
                .map(String::toLowerCase)
                .map(str -> str.replaceAll("\\s", ""))
                .collect(Collectors.toList());

        //Answer may be larger than one, so .containsAll is used
        if (!answers.containsAll(response)) missedQuestions.add(questions.get(quizQuestNum));
    }

    public static void addResponse(int id, List<String> response) {
        responses.put(id, response);
    }


}

