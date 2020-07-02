package com.godnedy;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class Config {

    private static final int DEFAULT_TIMEOUT = 3000;

    @Bean
    public RestTemplate defaultRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.setReadTimeout(Duration.ofMillis(DEFAULT_TIMEOUT))
                .setConnectTimeout(Duration.ofMillis(DEFAULT_TIMEOUT))
                .build();
    }
}