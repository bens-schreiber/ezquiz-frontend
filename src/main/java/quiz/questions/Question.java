package quiz.questions;

import java.util.Collections;
import java.util.List;

/**
 * Stores necessary information to display a question
 */

public class Question {

    private final int id;
    private final String type;
    private final String subject;
    private final String prompt;
    private final String directions;
    private final List<String> options;
    private List<String> answer;

    /**
     * Class constructor.
     * Answer is null until specifically set.
     */

    public Question(String type, String subject, List<String> options, String prompt, String directions, int id) {
        this.type = type;
        this.subject = subject;
        this.options = options;
        this.prompt = prompt;
        this.id = id;
        this.directions = directions;
    }

    /**
     * Shuffles question options, if question has options.
     */
    public void shuffleOptions() {
        if (options != null) {
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
