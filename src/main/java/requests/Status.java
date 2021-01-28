package requests;

public enum Status {
    NO_CONTENT, ACCEPTED, NO_CONNECTION, UNAUTHORIZED;

    Status() {
    }

    static Status getStatusFromInt(int response) {

        return switch (response) {
            case 202 -> ACCEPTED;
            case 204 -> NO_CONTENT;
            case 401 -> UNAUTHORIZED;
            default -> NO_CONNECTION;
        };

    }
}
