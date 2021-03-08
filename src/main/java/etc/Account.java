package etc;

/**
 * Stores all user information, and currently selected Quiz.
 */
public class Account {

    private static Quiz quiz;
    private static User user;

    //Null all values
    public static void logout() {
        user = null;
        quiz = null;
    }

    public static User getUser() {
        return user;
    }

    public static Quiz getQuiz() {
        return quiz;
    }

    //Change Account.user to the new user.
    public static void setUser(User user) {
        Account.user = user;
    }

    public static void setQuiz(String owner, String name, int key) {
        Account.quiz = new Quiz(owner, name, key);
    }

    public static void setQuiz(Quiz quiz) {
        Account.quiz = quiz;
    }

}
