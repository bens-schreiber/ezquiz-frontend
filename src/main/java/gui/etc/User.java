package gui.etc;

//Records all necessary user side information
public class User {

    private static String username = "";

    private static String AUTH_TOKEN = "";

    //Sudo constructor
    public static void login(String username, String AUTH_TOKEN) {

        User.username = username;

        User.AUTH_TOKEN = AUTH_TOKEN;

    }

    //If the username field isn't filled out, the user isn't logged in
    public static boolean isLoggedIn() {

        return !User.username.isEmpty();

    }

    public static void logout() {

        User.username = null;

        User.AUTH_TOKEN = null;

    }

    public static String AUTH_TOKEN() {
        return AUTH_TOKEN;
    }

    public static String getUsername() {
        return username;
    }

}
