package com.example.aqatests.tests;

import com.example.aqatests.utils.TokenGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenTests extends EndpointTests {

    @Test
    @DisplayName("Корректность token по маске A-Z0-9 32")
    @Description("Проверка валидного токена 32 символа A-Z0-9")
    @Severity(SeverityLevel.CRITICAL)
    public void tokenMaskValidation() {
        String token = TokenGenerator.generateValidToken();
        stubAuthSuccess();

        Response response = sendRequest(token, "LOGIN");

        assertEquals(200, response.statusCode());
        assertEquals("OK", response.jsonPath().getString("result"));
    }

    @Test
    @DisplayName("Корректный token")
    @Description("При корректном token и ответе 200 от /auth, получаем OK")
    @Severity(SeverityLevel.CRITICAL)
    public void loginSuccess() {
        String token = TokenGenerator.generateInvalidToken();
        stubAuthSuccess();

        Response response = sendRequest(token, "LOGIN");

        assertEquals(200, response.statusCode());
        assertEquals("OK", response.jsonPath().getString("result"));

        // Проверяем, что запрос к /auth действительно был сделан
        verify(postRequestedFor(urlEqualTo("/auth"))
                .withRequestBody(containing("token=" + token)));
    }
}
