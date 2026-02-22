package com.example.aqatests.tests;

import com.example.aqatests.base.BaseApiTest;
import com.example.aqatests.config.MockStubs;
import com.example.aqatests.models.ResponseDto;
import com.example.aqatests.utils.TokenGenerator;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Сценарии")
@Feature("Полная последовательность")
public class SequenceTests extends BaseApiTest {

    @Test
    @DisplayName("Успешная последовательность LOGIN -> ACTION -> LOGOUT")
    @Description("Проверяет полный жизненный цикл токена")
    @Severity(SeverityLevel.BLOCKER)
    public void shouldSuccessfullyLoginActionLogout() {
        String token = TokenGenerator.generateValidToken();
        MockStubs.stubAuthSuccess();
        MockStubs.stubDoActionSuccess();

        ResponseDto loginResponse = appClient.login(token);
        assertThat(loginResponse.getResult()).isEqualTo("OK");
        MockStubs.verifyAuthCalled(token);

        ResponseDto actionResponse = appClient.action(token);
        assertThat(actionResponse.getResult()).isEqualTo("OK");
        MockStubs.verifyDoActionCalled(token);

        // Запоминаем количество вызовов /doAction для этого токена
        int callsBeforeLogout = MockStubs.countDoActionCallsForToken(token);

        ResponseDto logoutResponse = appClient.logout(token);
        assertThat(logoutResponse.getResult()).isEqualTo("OK");

        // После выхода ACTION недоступен
        ResponseDto actionAfterLogout = appClient.action(token);
        assertThat(actionAfterLogout.getResult()).isEqualTo("ERROR");
        assertThat(actionAfterLogout.getMessage()).isNotEmpty();

        // Проверяем, что новых вызовов /doAction не было
        int callsAfterLogout = MockStubs.countDoActionCallsForToken(token);
        assertThat(callsAfterLogout).isEqualTo(callsBeforeLogout);
    }
}