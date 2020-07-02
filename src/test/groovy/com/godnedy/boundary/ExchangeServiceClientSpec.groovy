package com.godnedy.boundary

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Subject

class ExchangeServiceClientSpec extends Specification {

    def BID = 1.3333d
    def ASK = 1.4435d
    def CURRENCY = "GBP"

    def restTemplate = Mock(RestTemplate)

    @Subject
    ExchangeServiceClient exchangeServiceClient = new ExchangeServiceClient("http://api.nbp.pl", restTemplate)

    def "calls_for_proper_currency"() {
        given:
            restTemplate.exchange(URI.create("http://api.nbp.pl/api/exchangerates/rates/c/" + CURRENCY +"/last/1/?format=json"),
                    HttpMethod.GET, _ as HttpEntity, ExchangeRateResponse.class) >> new ResponseEntity<ExchangeRateResponse>(exchangeResponse(), HttpStatus.OK)
        when:
            def response = exchangeServiceClient.getRatesForCurrency(CURRENCY)

        then:
            response.getCode() == CURRENCY
            response.getBid() == BID
            response.getAsk() == ASK
    }

    def "throws exception if problems with external rate provider"() {
        given:
            def currency = "usd"
            restTemplate.exchange(*_) >> { throw new RestClientException("message")}
        when:
            exchangeServiceClient.getRatesForCurrency(currency)

        then:
            thrown(ImpossibleToProvideRateByExternalServiceException)
    }

    def exchangeResponse() {
        new ExchangeRateResponse(
                code: "GBP",
                rates: [new Rate(
                        bid: new Double(BID),
                        ask: new Double(ASK)
                )]
        )
    }
}
