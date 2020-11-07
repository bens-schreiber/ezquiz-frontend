package questions;

import java.util.List;

public class Question {
    private final String type;
    private final String subject;
    private final List<String> options;
    private final String question;
    private final int id;

    public Question(String type, String subject, List<String> options, String question, int id) {
        this.type = type;
        this.subject = subject;
        this.options = options;
        this.question = question;
        this.id = id;
    }


    public String getType() {return this.type;}
    public String getSubject() {return this.subject;}
    public List<String> getOptions() {return this.options;}
    public String getQuestion() {return this.question;}
    public int getID() {return this.id;}
}
