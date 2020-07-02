package com.godnedy.bank

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.godnedy.bank.account.Currency
import com.godnedy.bank.account.InsufficientFundsException
import com.godnedy.bank.api.CreateAccountRequest
import com.godnedy.bank.user.SameUserExistsException
import com.godnedy.boundary.ExchangeRateResponse
import com.godnedy.boundary.ExchangeServiceClient
import com.godnedy.boundary.Rate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static groovy.json.JsonOutput.toJson

@SpringBootTest
class BankAccountServiceIntegrationSpec extends Specification {

    @Autowired
    BankService bankService;

    @Autowired
    ExchangeServiceClient exchangeServiceClient

    private WireMockServer mockServer;

    def setup() {
        mockServer = new WireMockServer(
                WireMockConfiguration.wireMockConfig()
                        .port(8089))
        mockServer.start()
        configureFor("localhost", 8089)

        stubFor(
                get(urlEqualTo("/api/exchangerates/rates/c/USD/last/1/?format=json"))
                        .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(toJson(new ExchangeRateResponse(
                        code: "USD",
                        rates: Collections.singletonList(new Rate(bid: 4d, ask: 5d)))))
                ))
    }

    def cleanup() {
        mockServer.stop()
    }

    def "adds account, exchanges, and returns account information"() {
        given:
            def pesel = "90010103223"
            def fullName = "Name"
            def initial = 100.00d
            def request = createAccountRequest(pesel, fullName, initial)

        when:
            bankService.createAccount(request)
            def accountInfo = bankService.getUserInfo(pesel)
        then: 'account created'
            assert accountInfo.getUser().fullName == fullName
            assert accountInfo.getUser().personalIdNumber == pesel
            assert accountInfo.getAccountInfo().get(0).currency == Currency.PLN
            assert accountInfo.getAccountInfo().get(0).balance == BigDecimal.valueOf(initial)
            assert accountInfo.getAccountInfo().get(1).currency == Currency.USD
            assert accountInfo.getAccountInfo().get(1).balance == BigDecimal.ZERO

        when:
            bankService.exchange(pesel, exchangeData(Currency.PLN, Currency.USD, BigDecimal.valueOf(50d)))
            accountInfo = bankService.getUserInfo(pesel)
        then: "money exchanged from PLN to USD"
            assert accountInfo.getUser().fullName == fullName
            assert accountInfo.getUser().personalIdNumber == pesel
            assert accountInfo.getAccountInfo().get(0).currency == Currency.PLN
            assert accountInfo.getAccountInfo().get(0).balance == BigDecimal.valueOf(50d)
            assert accountInfo.getAccountInfo().get(1).currency == Currency.USD
            assert accountInfo.getAccountInfo().get(1).balance == BigDecimal.valueOf(10d)
        when:
            bankService.exchange(pesel, exchangeData(Currency.USD, Currency.PLN, BigDecimal.valueOf(4.4d)))
            accountInfo = bankService.getUserInfo(pesel)
        then: "money exchanged from USD to PLN"
            assert accountInfo.getUser().fullName == fullName
            assert accountInfo.getUser().personalIdNumber == pesel
            assert accountInfo.getAccountInfo().get(0).currency == Currency.PLN
            assert accountInfo.getAccountInfo().get(0).balance == BigDecimal.valueOf(67.6d)
            assert accountInfo.getAccountInfo().get(1).currency == Currency.USD
            assert accountInfo.getAccountInfo().get(1).balance == BigDecimal.valueOf(5.6d)

    }

    def "cannot add two users with same personal id number"() {
        given:
            def pesel = "90010103223"
            def fullName = "Name"
            def initial = 100.00d
            def request = createAccountRequest(pesel, fullName, initial)

        when:
            bankService.createAccount(request)
            bankService.createAccount(request)
        then:
            thrown(SameUserExistsException)

    }

    CreateAccountRequest createAccountRequest(String pesel, String fullName, Double initialBalance) {
        new CreateAccountRequest(initialBalance: initialBalance, fullName: fullName, personalIdNumber: pesel)
    }

    ExchangeData exchangeData(Currency currencyFrom, Currency currencyTo, BigDecimal amount) {
        new ExchangeData(currencyFrom, currencyTo, amount)
    }

}
