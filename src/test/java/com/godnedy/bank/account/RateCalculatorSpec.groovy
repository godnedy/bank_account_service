package com.godnedy.bank.account


import com.godnedy.boundary.ExchangeRateResponse
import com.godnedy.boundary.Rate
import spock.lang.Specification
import spock.lang.Unroll

class RateCalculatorSpec extends Specification {

    @Unroll
    def "should calculate correct rate for #testCase"() {
        given:
            def amount = BigDecimal.valueOf(20d)
            def exchangeServiceResponse = new ExchangeRateResponse(
                    code: "USD",
                    rates: Collections.singletonList(new Rate(bid: 4d, ask: 5d)))
        when:
            def response = RateCalculator.calculateAmountToAdd(currency, amount, exchangeServiceResponse)
        then:
            response.equals(expectedRate)
        where:
            testCase | currency     | expectedRate
            "PLN"    | Currency.PLN | BigDecimal.valueOf(4.00d).setScale(2)
            "USD"    | Currency.USD | BigDecimal.valueOf(80.00d).setScale(2)
    }
}
