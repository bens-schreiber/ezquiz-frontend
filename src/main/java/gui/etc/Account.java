package gui.etc;

//Records all necessary user side information
public class Account {

    static class User {

        private final String username;

        private final String AUTH_TOKEN;

        public User(String username, String AUTH_TOKEN) {

            this.username = username;

            this.AUTH_TOKEN = AUTH_TOKEN;

        }

        public String getUsername() {
            return username;
        }

        public String getAUTH_TOKEN() {
            return AUTH_TOKEN;
        }
    }

    private static User user;

    public static void login(String username, String AUTH_TOKEN) {
        user = new User(username, AUTH_TOKEN);
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

}
