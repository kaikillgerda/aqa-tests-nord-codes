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
        // Создаём сервер с портом 8888 и включаем подробное логирование
        wireMockServer = new WireMockServer(WireMockConfiguration.options()
                .port(8888)
                .notifier(new ConsoleNotifier(true))); // true — подробный вывод
        wireMockServer.start();

        // Настраиваем клиент на правильный порт
        WireMock.configureFor("localhost", 8888);

        // Небольшая задержка для гарантии запуска (можно убрать, если не требуется)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        if (wireMockServer != null) {
            wireMockServer.resetAll();
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}