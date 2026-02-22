package com.example.aqatests.base;

import com.example.aqatests.config.WireMockConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(WireMockConfig.class)
public abstract class BaseTest {

    @LocalServerPort
    protected int port;

    protected RequestSpecification requestSpec;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        requestSpec = RestAssured.given()
                .contentType(ContentType.URLENC)
                .accept(ContentType.JSON)
                .header("X-Api-Key", "qazWSXedc");
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("mock", () -> "http://localhost:8888");
    }
}