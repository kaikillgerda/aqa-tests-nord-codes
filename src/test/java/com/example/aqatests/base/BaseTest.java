package com.example.aqatests.base;

import com.example.aqatests.config.AppConfig;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseTest {

    @BeforeAll
    public static void setUpBase() {
        RestAssured.baseURI = AppConfig.getAppBaseUrl();
        RestAssured.port = AppConfig.getAppPort();
    }

    @Step("Отправка POST-запроса с токеном {token}, действие {action}")
    protected Response sendRequest(String token, String action) {
        RequestSpecification spec = RestAssured.given()
                .contentType(ContentType.URLENC)
                .accept(ContentType.JSON)
                .header("X-Api-Key", AppConfig.getAppApiKey())
                .log().all();

        Response response = spec
                .formParam("token", token)
                .formParam("action", action)
                .when()
                .post("/endpoint")
                .then()
                .log().all()
                .extract().response();

        Allure.addAttachment("Запрос", "text/plain",
                new ByteArrayInputStream(("token=" + token + "&action=" + action).getBytes(StandardCharsets.UTF_8)),
                "txt");
        Allure.addAttachment("Ответ", "application/json",
                new ByteArrayInputStream(response.asPrettyString().getBytes(StandardCharsets.UTF_8)),
                "json");

        return response;
    }
}