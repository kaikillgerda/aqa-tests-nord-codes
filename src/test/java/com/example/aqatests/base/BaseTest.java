package com.example.aqatests.base;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    protected RequestSpecification requestSpec;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;  // Порт тестируемого приложения

        requestSpec = RestAssured.given()
                .contentType(ContentType.URLENC)
                .accept(ContentType.JSON)
                .header("X-Api-Key", "qazWSXedc")
                .log().all();  // Добавляем логирование для отладки
    }

    @Step ("Отправка post-запроса в приложение")
    protected Response sendRequest(String token, String action) {
        return requestSpec
                .formParam("token", token)
                .formParam("action", action)
                .when()
                .post("/endpoint")
                .then()
                .log().all()  // Логируем ответ для отладки
                .extract().response();
    }
}