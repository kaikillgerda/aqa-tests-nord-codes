package com.example.aqatests.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) throw new RuntimeException("config.properties not found");
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static String getAppBaseUrl() {
        return props.getProperty("app.baseUrl");
    }

    public static int getAppPort() {
        return Integer.parseInt(props.getProperty("app.port"));
    }

    public static String getAppApiKey() {
        return props.getProperty("app.apiKey");
    }

    public static String getMockBaseUrl() {
        return props.getProperty("mock.baseUrl");
    }

    public static int getMockPort() {
        return Integer.parseInt(props.getProperty("mock.port"));
    }
}