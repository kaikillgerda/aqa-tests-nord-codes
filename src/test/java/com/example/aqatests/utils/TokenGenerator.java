package com.example.aqatests.utils;

import java.security.SecureRandom;
import java.util.UUID;

public class TokenGenerator {
    //    private static final String HEX = "0123456789ABCDEF"; //для прогона и проверки логики -
//    раскомментировать и закомментировать 8 строку
    private static final String HEX = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom random = new SecureRandom();

    public static String generateValidToken() {
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            sb.append(HEX.charAt(random.nextInt(HEX.length())));
        }
        return sb.toString();
    }

    public static String generateInvalidToken() {
        return UUID.randomUUID().toString(); // 36 символов, содержит дефисы
    }

    public static String generateInvalidTokenWrongLength() {
        int length = random.nextBoolean() ? 31 : 33;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(HEX.charAt(random.nextInt(HEX.length())));
        }
        return sb.toString();
    }

    public static String generateInvalidTokenSpecialChars() {
        return "INVALID_TOKEN_" + random.nextInt(1000);
    }
}