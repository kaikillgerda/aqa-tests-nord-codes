package com.example.aqatests.tests;

import com.example.aqatests.base.BaseApiTest;
import com.example.aqatests.config.MockStubs;
import com.example.aqatests.models.ResponseDto;
import com.example.aqatests.utils.TokenGenerator;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Авторизация")
@Feature("LOGIN")
public class LoginTests extends BaseApiTest {

    @Test
    @DisplayName("Успешный LOGIN с валидным токеном")
    @Description("При валидном токене и ответе 200 от /auth возвращается OK")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldSuccessfullyLogin() {
        String token = TokenGenerator.generateValidToken();
        MockStubs.stubAuthSuccess();

        ResponseDto response = appClient.login(token);

        assertThat(response.getResult()).isEqualTo("OK");
        MockStubs.verifyAuthCalled(token);
    }

    @Test
    @DisplayName("LOGIN с невалидным токеном (неверный формат)")
    @Description("Если токен не соответствует маске A-Z0-9 32 символа, приложение возвращает ERROR и не вызывает /auth")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorForInvalidToken() {
        String token = TokenGenerator.generateInvalidToken();
        MockStubs.stubAuthSuccess();

        ResponseDto response = appClient.login(token);

        assertThat(response.getResult()).isEqualTo("ERROR");
        assertThat(response.getMessage()).isNotEmpty();
        MockStubs.verifyAuthNotCalled();
    }

    @Test
    @DisplayName("LOGIN с токеном неверной длины")
    @Description("Токен содержит только A-Z0-9, но длина не 32")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorForWrongLengthToken() {
        String token = TokenGenerator.generateInvalidTokenWrongLength();
        MockStubs.stubAuthSuccess();

        ResponseDto response = appClient.login(token);

        assertThat(response.getResult()).isEqualTo("ERROR");
        assertThat(response.getMessage()).isNotEmpty();
        MockStubs.verifyAuthNotCalled();
    }

    @Test
    @DisplayName("LOGIN с токеном, содержащим недопустимые символы")
    @Description("Токен содержит символы, отличные от A-Z0-9")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorForSpecialCharsToken() {
        String token = TokenGenerator.generateInvalidTokenSpecialChars();
        MockStubs.stubAuthSuccess();

        ResponseDto response = appClient.login(token);

        assertThat(response.getResult()).isEqualTo("ERROR");
        assertThat(response.getMessage()).isNotEmpty();
        MockStubs.verifyAuthNotCalled();
    }

    @Test
    @DisplayName("LOGIN при ошибке внешнего сервиса /auth (500)")
    @Description("Если /auth возвращает 500, приложение возвращает ERROR")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorWhenAuthFails() {
        String token = TokenGenerator.generateValidToken();
        MockStubs.stubAuthError(500);

        ResponseDto response = appClient.login(token);

        assertThat(response.getResult()).isEqualTo("ERROR");
        assertThat(response.getMessage()).isNotEmpty();
        MockStubs.verifyAuthCalled(token);
    }

    @Test
    @DisplayName("LOGIN при таймауте внешнего сервиса /auth")
    @Description("Проверяем обработку долгого ответа")
    @Severity(SeverityLevel.MINOR)
    public void shouldReturnErrorWhenAuthTimeout() {
        String token = TokenGenerator.generateValidToken();
        MockStubs.stubAuthTimeout();

        ResponseDto response = appClient.login(token);

        assertThat(response.getResult()).isEqualTo("ERROR");
        assertThat(response.getMessage()).isNotEmpty();
        MockStubs.verifyAuthCalled(token);
    }
}