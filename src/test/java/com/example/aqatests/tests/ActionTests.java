package com.example.aqatests.tests;

import com.example.aqatests.base.BaseApiTest;
import com.example.aqatests.config.MockStubs;
import com.example.aqatests.models.ResponseDto;
import com.example.aqatests.utils.TokenGenerator;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Действия")
@Feature("ACTION")
public class ActionTests extends BaseApiTest {

    @Test
    @DisplayName("Успешный ACTION после LOGIN")
    @Description("После успешного LOGIN можно выполнить ACTION")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldSuccessfullyPerformActionAfterLogin() {
        String token = TokenGenerator.generateValidToken();
        MockStubs.stubAuthSuccess();
        MockStubs.stubDoActionSuccess();

        ResponseDto loginResponse = appClient.login(token);
        assertThat(loginResponse.getResult()).isEqualTo("OK");
        MockStubs.verifyAuthCalled(token);

        ResponseDto actionResponse = appClient.action(token);
        assertThat(actionResponse.getResult()).isEqualTo("OK");
        MockStubs.verifyDoActionCalled(token);
    }

    @Test
    @DisplayName("ACTION без предварительного LOGIN")
    @Description("Если токен не был залогинен, ACTION возвращает ERROR и не вызывает /doAction")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorWhenActionWithoutLogin() {
        String token = TokenGenerator.generateValidToken();
        MockStubs.stubDoActionSuccess();

        ResponseDto response = appClient.action(token);

        assertThat(response.getResult()).isEqualTo("ERROR");
        assertThat(response.getMessage()).isNotEmpty();
        MockStubs.verifyDoActionNotCalled();
    }

    @Test
    @DisplayName("ACTION с другим токеном после LOGIN")
    @Description("Если залогинен один токен, а ACTION отправлен с другим, возвращается ERROR")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorWhenActionWithDifferentToken() {
        String validToken = TokenGenerator.generateValidToken();
        String anotherToken = TokenGenerator.generateValidToken();
        MockStubs.stubAuthSuccess();
        MockStubs.stubDoActionSuccess();

        appClient.login(validToken);
        ResponseDto actionResponse = appClient.action(anotherToken);

        assertThat(actionResponse.getResult()).isEqualTo("ERROR");
        assertThat(actionResponse.getMessage()).isNotEmpty();
        MockStubs.verifyDoActionNotCalled();
    }

    @Test
    @DisplayName("ACTION при ошибке внешнего сервиса /doAction (500)")
    @Description("Если /doAction возвращает 500, приложение возвращает ERROR")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorWhenDoActionFails() {
        String token = TokenGenerator.generateValidToken();
        MockStubs.stubAuthSuccess();
        MockStubs.stubDoActionError(500);

        appClient.login(token);
        ResponseDto response = appClient.action(token);

        assertThat(response.getResult()).isEqualTo("ERROR");
        assertThat(response.getMessage()).isNotEmpty();
        MockStubs.verifyDoActionCalled(token);
    }
}