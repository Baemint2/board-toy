package org.example.board.config.auth;

import java.util.UUID;

public class AuthTokenGenerator {
    public static String createAuthToken() {
        return UUID.randomUUID().toString();
    }
}
