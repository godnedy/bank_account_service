package com.godnedy.boundary

import com.godnedy.bank.account.Currency
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Subject

class ExchangeServiceClientSpec extends Specification {

    def restTemplate = Mock(RestTemplate)
    @Subject
    ExchangeServiceClient exchangeServiceClient = new ExchangeServiceClient("http://api.nbp.pl", restTemplate)

    def "calls_for_proper_currency"() {
        given:
            def currency = "usd"
            restTemplate.exchange(URI.create("http://api.nbp.pl/api/exchangerates/rates/c/usd/last/1/?format=json"), HttpMethod.GET, _ as HttpEntity, ExchangeRateResponse.class) >> new ResponseEntity<ExchangeRateResponse>(exchangeResponse(), HttpStatus.OK)
        when:
            def response = exchangeServiceClient.getRatesForCurrency(currency)

        then:
            response.get().getCode() == Currency.USD.name()
    }

    def exchangeResponse() {
        new ExchangeRateResponse(
                code: "USD",
                rates: [new Rate(
                        no: "126/C/NBP/2020",
                        effectiveDate: "2020-07-01",
                        bid: new Double(3.9268),
                        ask: new Double(4.0062)
                )]
        )
    }

//    def "throws_specific_exception_in_case_of_problems"() { //nie wiem czy robic Ex
//
//    }
}
