package gui.account;

public class Quiz {

    private final String owner;
    private String name;
    private final int key;
    private int score;

    //Default
    public Quiz(String owner, String name, int key) {
        this.owner = owner;
        this.name = name;
        this.key = key;
    }

    //For creating an already answered quiz in QuizzesMenu.
    public Quiz(String owner, String name, int key, int score) {
        this.owner = owner;
        this.name = name;
        this.key = key;
        this.score = score;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    public int getScore() {
        return score;
    }

}
