package com.example.aqatests.tests;

import com.example.aqatests.base.BaseTest;
import com.example.aqatests.config.WireMockExtension;
import com.example.aqatests.utils.TokenGenerator;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Тестирование эндпоинта /endpoint")
@Feature("Основные сценарии")
public class EndpointTests extends BaseTest {

    @RegisterExtension
    static WireMockExtension wmExtension = new WireMockExtension();

    @BeforeEach
    public void setupWireMock() {
        // Настраиваем клиент WireMock для работы с нашим сервером
        WireMock.configureFor("localhost", 8888);
    }

    private void stubAuthSuccess() {
        stubFor(post(urlEqualTo("/auth"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"OK\"}")));
    }

    private void stubAuthFailure(int statusCode) {
        stubFor(post(urlEqualTo("/auth"))
                .willReturn(aResponse().withStatus(statusCode)));
    }

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

    @Test
    @DisplayName("Проверка доступности приложения")
    @Description("Убеждаемся, что тестируемое приложение запущено и доступно")
    public void healthCheck() {
        Response response = sendRequest("TESTTOKEN123456789012345678901234", "TEST");
        // Любой ответ (даже ошибка) означает, что приложение отвечает
        assertTrue(response.statusCode() == 200 ||
                response.statusCode() == 400 ||
                response.statusCode() == 404);
    }
}