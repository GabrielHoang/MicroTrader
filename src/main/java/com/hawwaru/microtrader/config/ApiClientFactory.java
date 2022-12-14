package com.hawwaru.microtrader.config;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.enums.DefaultUrls;
import com.binance.connector.client.impl.SpotClientImpl;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

@Factory
public class ApiClientFactory {

    @Value("${micronaut.binance-api.test-net-api-key}")
    private String testNetApiKey;

    @Value("${micronaut.binance-api.test-net-secret-key}")
    private String testNetSecretKey;


    @Singleton
    public SpotClient getTestSpotClient() {
        return new SpotClientImpl(testNetApiKey, testNetSecretKey, DefaultUrls.TESTNET_URL);
    }
}
