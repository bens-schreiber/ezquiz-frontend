package quiz.question;

import java.util.Collections;
import java.util.List;

/**
 * Stores necessary information to display a question
 */
public class Question {

    private final Type type;

    public enum Type {
        MULTIPLECHOICE, TRUEORFALSE, CHECKBOX, WRITTEN
    }

    private final Subject subject;
    public enum Subject {
        NETWORKDESIGN, INTBUS, MARKETING, BUSMATH
    }

    private final int id;
    private final String prompt, directions;
    private final List<String> options;
    private List<String> answer;

    /**
     * Class constructor.
     * Answer is null until specifically set.
     */
    public Question(Type type, Subject subject, List<String> options, String prompt, String directions, int id) {
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

    public Type getType() {
        return this.type;
    }

    public Subject getSubject() {
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
