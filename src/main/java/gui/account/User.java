package gui.account;

/**
 * Pojo for storing logged in user values
 */
public class User {

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

    public String getAuth() {
        return AUTH_TOKEN;
    }
}
