package quiz.questions;

import java.util.Collections;
import java.util.List;

public class Question {
    private final String type;
    private final String subject;
    private final List<String> options;
    private final String prompt;
    private final int id;

    public Question(String type, String subject, List<String> options, String prompt, int id) {
        this.type = type;
        this.subject = subject;
        this.options = options;
        this.prompt = prompt;
        this.id = id;
    }


    public String getType() {
        return this.type;
    }

    public String getSubject() {
        return this.subject;
    }

    public List<String> getOptions() {
        return this.options;
    }

    public void shuffleOptions() {
        if (!this.type.equals("4") && !this.type.equals("2")) {
            Collections.shuffle(options);
        }
    }

    public String getPrompt() {
        return this.prompt;
    }

    public int getID() {
        return this.id;
    }
}
