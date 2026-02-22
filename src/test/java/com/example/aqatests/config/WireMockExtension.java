package com.example.aqatests.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class WireMockExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback {

    private WireMockServer wireMockServer;

    @Override
    public void beforeAll(ExtensionContext context) {
        wireMockServer = new WireMockServer(WireMockConfiguration.options()
                .port(AppConfig.getMockPort())
                .notifier(new ConsoleNotifier(true)));
        wireMockServer.start();
        WireMock.configureFor(AppConfig.getMockBaseUrl(), AppConfig.getMockPort());
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        if (wireMockServer != null) wireMockServer.resetAll();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (wireMockServer != null) wireMockServer.stop();
    }
}