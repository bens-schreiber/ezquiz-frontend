package gui.account;

//Records all necessary user side information
public class Account {

    private static Quiz quiz;
    private static User user;

    public static void login(String username, String AUTH_TOKEN, boolean admin) {
        user = new User(username, AUTH_TOKEN, admin);
    }

    //If there is no User then there is not a logged in user
    public static boolean isLoggedIn() {
        return user != null;
    }

    public static void logout() {
        user = null;
        quiz = null;
    }


    public static User getUser() {
        return user;
    }

    public static String getQuizPath() {
        return (quiz.getOwner() + "/" + quiz.getName()).replace(" ", "%20");
    }

    public static Quiz getQuiz() {
        return quiz;
    }


    public static void setQuiz(String owner, String name, int key) {
        Account.quiz = new Quiz(owner, name, key);
    }

    public static void setQuiz(Quiz quiz) {
        Account.quiz = quiz;
    }

}
