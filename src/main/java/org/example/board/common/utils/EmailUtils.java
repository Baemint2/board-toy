package org.example.board.common.utils;

public class EmailUtils {
    public static String maskEmail(String email) {
        int index = email.indexOf("@");
        if(index > 1) {
            StringBuilder maskedEmail = new StringBuilder(email);
            for (int i = 0; i < index - 1; i++) {
                maskedEmail.setCharAt(i, '*');
            }
            return maskedEmail.toString();
        }
        return email;
    }
}
