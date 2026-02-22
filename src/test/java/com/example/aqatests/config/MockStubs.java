package com.example.aqatests.config;

import com.github.tomakehurst.wiremock.client.WireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockStubs {

    public static void stubAuthSuccess() {
        stubFor(post(urlEqualTo("/auth")).willReturn(aResponse().withStatus(200)));
    }

    public static void stubAuthError(int statusCode) {
        stubFor(post(urlEqualTo("/auth")).willReturn(aResponse().withStatus(statusCode)));
    }

    public static void stubAuthTimeout() {
        stubFor(post(urlEqualTo("/auth")).willReturn(aResponse().withFixedDelay(10000).withStatus(200)));
    }

    public static void stubDoActionSuccess() {
        stubFor(post(urlEqualTo("/doAction")).willReturn(aResponse().withStatus(200)));
    }

    public static void stubDoActionError(int statusCode) {
        stubFor(post(urlEqualTo("/doAction")).willReturn(aResponse().withStatus(statusCode)));
    }

    public static void verifyAuthCalled(String token) {
        verify(postRequestedFor(urlEqualTo("/auth")).withRequestBody(containing("token=" + token)));
    }

    public static void verifyDoActionCalled(String token) {
        verify(postRequestedFor(urlEqualTo("/doAction")).withRequestBody(containing("token=" + token)));
    }

    public static void verifyAuthNotCalled() {
        verify(0, postRequestedFor(urlEqualTo("/auth")));
    }

    public static void verifyDoActionNotCalled() {
        verify(0, postRequestedFor(urlEqualTo("/doAction")));
    }

    public static int countDoActionCallsForToken(String token) {
        return WireMock.findAll(postRequestedFor(urlEqualTo("/doAction"))
                .withRequestBody(containing("token=" + token))).size();
    }
}