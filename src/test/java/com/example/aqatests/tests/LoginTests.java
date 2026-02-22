package com.example.aqatests.tests;

import com.example.aqatests.utils.TokenGenerator;
import com.example.aqatests.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTests extends EndpointTests {

    @Test
    @DisplayName("Успешный LOGIN")
    @Description("При корректном токене и ответе 200 от /auth, получаем OK")
    @Severity(SeverityLevel.CRITICAL)
    public void loginSuccess() {
        String token = TokenGenerator.generateValidToken();

        stubAuthSuccess();

        Response response = sendRequest(token, "LOGIN");

        assertEquals(200, response.statusCode());
        assertEquals("OK", response.jsonPath().getString("result"));

        // Проверяем, что запрос к /auth действительно был сделан
        verify(postRequestedFor(urlEqualTo("/auth"))
                .withRequestBody(containing("token=" + token)));
    }
}
