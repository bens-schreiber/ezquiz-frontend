package quiz.questions;

import java.util.Collections;
import java.util.List;

/**
 * Question object, stores all values for a certain question
 */

public class Question {

    private final String type;

    private final String subject;

    private final List<String> options;

    private final String prompt;

    private final String directions;

    private final int id;

    //Answer is not queried until required
    private List<String> answer;

    public Question(String type, String subject, List<String> options, String prompt, String directions, int id) {

        this.type = type;

        this.subject = subject;

        this.options = options;

        this.prompt = prompt;

        this.id = id;

        this.directions = directions;
    }

    public void shuffleOptions() {

        if (!this.type.equals("4") && !this.type.equals("2")) {

            Collections.shuffle(options);

        }
    }

    /**
     * Getters
     */

    public String getType() {
        return this.type;
    }

    public String getSubject() {
        return this.subject;
    }

    public List<String> getOptions() {
        return this.options;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public String getDirections() {
        return this.directions;
    }

    public int getID() {
        return this.id;
    }

    public List<String> getAnswer() {
        return this.answer;
    }

    /**
     * Setters
     */

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

}
