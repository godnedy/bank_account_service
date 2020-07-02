package com.godnedy.boundary;

import java.net.URI;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

@Slf4j
@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ExchangeServiceClient {

    private static final String CURRENCY_RATE_PATH = "/api/exchangerates/rates/c/{currency}/last/1/";

    String baseUrl;
    RestTemplate restTemplate;

    @Autowired
    public ExchangeServiceClient(@Value("${exchange.url}") String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public ExchangeRateResponse getRatesForCurrency(String currency) {
        try {
            ResponseEntity<ExchangeRateResponse> response = restTemplate.exchange(
                    uriFor(CURRENCY_RATE_PATH, currency), GET, httpEntity(), ExchangeRateResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Rate for currency {} not found", currency);
            throw new RuntimeException("clcl");
        }
    }

    private URI uriFor(String path, Object... variables) {
        return fromUri(URI.create(baseUrl))
                .path(path)
                .queryParam("format", "json")
                .build(variables);
    }

    private HttpEntity httpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }
}