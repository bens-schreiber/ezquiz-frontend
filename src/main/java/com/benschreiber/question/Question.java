package com.benschreiber.question;

import java.util.Collections;
import java.util.List;

/**
 * Stores necessary information to display a questions text elements
 */
public class Question {

    private final Type type;
    public enum Type {MULTIPLECHOICE, TRUEORFALSE, CHECKBOX, WRITTEN}

    private final int id;
    private final String prompt, directions;
    private List<String> answer;

    /**
     * Class constructor.
     * Answer is null until specifically set.
     */
    public Question(Type type, String prompt, String directions, int id) {
        this.type = type;
        this.prompt = prompt;
        this.id = id;
        this.directions = directions;
    }

    /**
     * Getters
     */
    public Type getType() {
        return this.type;
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
