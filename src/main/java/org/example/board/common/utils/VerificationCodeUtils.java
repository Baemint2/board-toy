package org.example.board.common.utils;

import java.security.SecureRandom;

public class VerificationCodeUtils {
    public static final String CODE_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int CODE_LENGTH = 6 ;

    public static String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CODE_CHARACTERS.charAt(random.nextInt(CODE_CHARACTERS.length())));
        }
        return sb.toString();
    }
}
