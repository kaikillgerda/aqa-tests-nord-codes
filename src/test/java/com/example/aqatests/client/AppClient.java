package com.example.aqatests.client;

import com.example.aqatests.base.BaseTest;
import com.example.aqatests.models.ResponseDto;
import io.qameta.allure.Step;

public class AppClient extends BaseTest {

    @Step("LOGIN с токеном {token}")
    public ResponseDto login(String token) {
        return sendRequest(token, "LOGIN").as(ResponseDto.class);
    }

    @Step("ACTION с токеном {token}")
    public ResponseDto action(String token) {
        return sendRequest(token, "ACTION").as(ResponseDto.class);
    }

    @Step("LOGOUT с токеном {token}")
    public ResponseDto logout(String token) {
        return sendRequest(token, "LOGOUT").as(ResponseDto.class);
    }
}