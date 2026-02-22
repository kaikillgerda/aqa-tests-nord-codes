package com.example.aqatests.utils;

import java.security.SecureRandom;

public class TokenGenerator {
    private static final String HEX = "0123456789ABCDEF";
    private static final SecureRandom random = new SecureRandom();

    public static String generateValidToken() {
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            sb.append(HEX.charAt(random.nextInt(HEX.length())));
        }
        return sb.toString();
    }
}