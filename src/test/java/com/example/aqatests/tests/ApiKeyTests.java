package com.example.aqatests.tests;

import com.example.aqatests.config.AppConfig;
import com.example.aqatests.utils.TokenGenerator;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Безопасность")
@Feature("API Key")
public class ApiKeyTests {

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = AppConfig.getAppBaseUrl();
        RestAssured.port = AppConfig.getAppPort();
    }

    @Test
    @DisplayName("Запрос без заголовка X-Api-Key")
    @Description("Сервер должен вернуть 403")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldReturnForbiddenWhenNoApiKey() {
        String token = TokenGenerator.generateValidToken();

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .accept(ContentType.JSON)
                .formParam("token", token)
                .formParam("action", "LOGIN")
                .when()
                .post("/endpoint")
                .then()
                .log().all()
                .extract().response();

        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    @DisplayName("Запрос с неверным API-ключом")
    @Description("Сервер должен вернуть 403")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldReturnForbiddenWhenWrongApiKey() {
        String token = TokenGenerator.generateValidToken();
        String wrongKey = "wrong_key";

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .accept(ContentType.JSON)
                .header("X-Api-Key", wrongKey)
                .formParam("token", token)
                .formParam("action", "LOGIN")
                .when()
                .post("/endpoint")
                .then()
                .log().all()
                .extract().response();

        assertThat(response.statusCode()).isEqualTo(401);
    }
}