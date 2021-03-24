package com.benschreiber.requests;

/**
 * HTTP Response codes
 */
public enum Status {
    NO_CONTENT, ACCEPTED, NO_CONNECTION, UNAUTHORIZED;

    static Status getStatusFromInt(int response) {
        return switch (response) {
            case 202 -> ACCEPTED;
            case 204 -> NO_CONTENT;
            case 401 -> UNAUTHORIZED;
            default -> NO_CONNECTION;
        };
    }
}
