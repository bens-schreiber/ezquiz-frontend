package gui.etc;

//Records all necessary user side information
public class Account {

    static class User {

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

    private static String quizPath;

    public static void login(String username, String AUTH_TOKEN, boolean admin) {
        user = new User(username, AUTH_TOKEN, admin);
    }

    //If there is no User then there is not a logged in user
    public static boolean isLoggedIn() {
        return user != null;
    }

    public static void logout() {
        user = null;
    }

    public static String AUTH_TOKEN() {
        return user.getAUTH_TOKEN();
    }

    public static String getUsername() {
        return user.getUsername();
    }

    public static boolean isAdmin() {
        return user.isAdmin();
    }

    public static void setQuizPath(String path) {
        quizPath = path.replace(" ", "%20");
    }

    public static String getQuizPath() {
        return quizPath;
    }
}
