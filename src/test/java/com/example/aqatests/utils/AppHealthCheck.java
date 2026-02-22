package com.example.aqatests.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppHealthCheck {

    public static boolean isAppRunning() {
        try {
            URL url = new URL("http://localhost:8080/endpoint");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("X-Api-Key", "qazWSXedc");
            connection.setDoOutput(true);
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);

            int responseCode = connection.getResponseCode();
            // 404 тоже ок, значит приложение отвечает
            return responseCode == 404 || responseCode == 200 || responseCode == 400;
        } catch (IOException e) {
            return false;
        }
    }

    public static void waitForApp(long timeoutSeconds) throws InterruptedException {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutSeconds * 1000) {
            if (isAppRunning()) {
                System.out.println("✓ Приложение доступно на порту 8080");
                return;
            }
            Thread.sleep(1000);
        }
        throw new RuntimeException("Приложение не запустилось за " + timeoutSeconds + " секунд");
    }
}