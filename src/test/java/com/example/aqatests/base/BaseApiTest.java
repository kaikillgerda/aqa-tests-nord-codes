package com.example.aqatests.base;

import com.example.aqatests.client.AppClient;
import com.example.aqatests.config.WireMockExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(WireMockExtension.class)
public abstract class BaseApiTest extends BaseTest {
    protected AppClient appClient = new AppClient();
}