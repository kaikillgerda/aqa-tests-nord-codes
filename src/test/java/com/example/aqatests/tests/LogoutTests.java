package com.example.aqatests.tests;

import com.example.aqatests.base.BaseApiTest;
import com.example.aqatests.config.MockStubs;
import com.example.aqatests.models.ResponseDto;
import com.example.aqatests.utils.TokenGenerator;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Сессии")
@Feature("LOGOUT")
public class LogoutTests extends BaseApiTest {

    @Test
    @DisplayName("Успешный LOGOUT после LOGIN")
    @Description("После LOGIN можно выйти, и токен удаляется")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldSuccessfullyLogout() {
        String token = TokenGenerator.generateValidToken();
        MockStubs.stubAuthSuccess();

        appClient.login(token);
        MockStubs.verifyAuthCalled(token);

        ResponseDto logoutResponse = appClient.logout(token);
        assertThat(logoutResponse.getResult()).isEqualTo("OK");
    }

    @Test
    @DisplayName("LOGOUT без предварительного LOGIN")
    @Description("Если токен не был залогинен, LOGOUT возвращает ERROR (по условию)")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorWhenLogoutWithoutLogin() {
        String token = TokenGenerator.generateValidToken();

        ResponseDto response = appClient.logout(token);

        assertThat(response.getResult()).isEqualTo("ERROR");
        assertThat(response.getMessage()).isNotEmpty();
    }

    @Test
    @DisplayName("Повторный LOGOUT после успешного LOGOUT")
    @Description("После выхода повторный LOGOUT с тем же токеном возвращает ERROR")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorWhenLogoutTwice() {
        String token = TokenGenerator.generateValidToken();
        MockStubs.stubAuthSuccess();

        appClient.login(token);
        MockStubs.verifyAuthCalled(token);

        appClient.logout(token);
        ResponseDto secondLogout = appClient.logout(token);

        assertThat(secondLogout.getResult()).isEqualTo("ERROR");
        assertThat(secondLogout.getMessage()).isNotEmpty();
    }

    @Test
    @DisplayName("ACTION после LOGOUT недоступен")
    @Description("После выхода ACTION возвращает ERROR")
    @Severity(SeverityLevel.NORMAL)
    public void shouldNotAllowActionAfterLogout() {
        String token = TokenGenerator.generateValidToken();
        MockStubs.stubAuthSuccess();
        MockStubs.stubDoActionSuccess();

        appClient.login(token);
        appClient.logout(token);

        ResponseDto actionResponse = appClient.action(token);
        assertThat(actionResponse.getResult()).isEqualTo("ERROR");
        assertThat(actionResponse.getMessage()).isNotEmpty();
        MockStubs.verifyDoActionNotCalled();
    }
}