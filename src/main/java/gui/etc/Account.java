package gui.etc;

//Records all necessary user side information
public class Account {

    public static class Quiz {

        private final String owner, name;
        private final int key;

        public Quiz(String owner, String name, int key) {
            this.owner = owner;
            this.name = name;
            this.key = key;
        }

        public String getOwner() {
            return owner;
        }

        public String getName() {
            return name;
        }

        public int getKey() {
            return key;
        }
    }

    private static Quiz quiz;

    private static class User {

        private final String username;

        private final String AUTH_TOKEN;

        private final boolean admin;

        public User(String username, String AUTH_TOKEN, boolean admin) {

            this.username = username;

            this.AUTH_TOKEN = AUTH_TOKEN;

            this.admin = admin;

        }

        public String getUsername() {
            return username;
        }

        public boolean isAdmin() {
            return admin;
        }

        public String getAUTH_TOKEN() {
            return AUTH_TOKEN;
        }
    }

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

    public static String getAuth() {
        return user.getAUTH_TOKEN();
    }

    public static String getUsername() {
        return user.getUsername();
    }

    public static boolean isAdmin() {
        return user.isAdmin();
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
