package com.godnedy.bank.account

import com.godnedy.bank.ExchangeData
import com.godnedy.boundary.ExchangeRateResponse
import com.godnedy.boundary.ExchangeServiceClient
import com.godnedy.boundary.Rate
import org.assertj.core.util.Lists
import spock.lang.Specification
import spock.lang.Subject

class AccountServiceSpec extends Specification {

    def BALANCE = 100d
    def USER_ID = UUID.randomUUID()


    def accountRepository = Mock(AccountRepository)
    def exchangeServiceClient = Mock(ExchangeServiceClient)

    def setup() {
        accountService = new AccountService(accountRepository, exchangeServiceClient)
    }

    @Subject
    AccountService accountService

    def "create account saves a proper object with id"() {
        when:
            accountService.createAccount(USER_ID, Currency.PLN, BALANCE)
        then:
            1 * accountRepository.save(_ as AccountEntity) >> { AccountEntity a ->
                assert a.id != null
                assert a.userId == USER_ID
                assert a.currency == Currency.PLN
                assert a.balance == BALANCE

            }
            accountRepository.findAll() >> Lists.emptyList()
    }

    def "exchange exchanges from PLN using ask rate value"() {
        given:
            def exchangeData = ExchangeData.from("PLN", "USD", 10d)
            def accountPLN = AccountEntity.from(USER_ID, Currency.PLN, 20d)
            def accountUSD = AccountEntity.from(USER_ID, Currency.USD, 0d)

            accountRepository.findAllByUserIdAndCurrencyIn(*_) >> Arrays.asList(accountPLN, accountUSD)
            exchangeServiceClient.getRatesForCurrency("USD") >> createExchangeRateResponse(Currency.USD, 4d, 5d)
        when:
            accountService.exchange(USER_ID, exchangeData)
        then:
            1 * accountRepository.save(_ as AccountEntity) >> { AccountEntity a ->
                assert a.balance == 10d
                assert a.currency == Currency.PLN
            }
            1 * accountRepository.save(_ as AccountEntity) >> { AccountEntity b ->
                assert b.balance == 2.0d
                assert b.currency == Currency.USD
            }
    }

    def "exchange exchanges from USD using bid rate value"() {
        given:
            def exchangeData = ExchangeData.from("USD", "PLN", 20d)
            def accountPLN = AccountEntity.from(USER_ID, Currency.PLN, 0d)
            def accountUSD = AccountEntity.from(USER_ID, Currency.USD, 20d)

            accountRepository.findAllByUserIdAndCurrencyIn(*_) >> Arrays.asList(accountPLN, accountUSD)
            exchangeServiceClient.getRatesForCurrency("USD") >> createExchangeRateResponse(Currency.USD, 4d, 5d)
        when:
            accountService.exchange(USER_ID, exchangeData)
        then:
            1 * accountRepository.save(_ as AccountEntity) >> { AccountEntity a ->
                assert a.balance == 0d
                assert a.currency == Currency.USD
            }
            1 * accountRepository.save(_ as AccountEntity) >> { AccountEntity b ->
                assert b.balance == 80d
                assert b.currency == Currency.PLN
            }
    }

    def "exchange throws exception if exchange not possible" (){
        given:
            def exchangeData = ExchangeData.from("USD", "PLN", 20d)
            def accountPLN = AccountEntity.from(USER_ID, Currency.PLN, 0d)
            def accountUSD = AccountEntity.from(USER_ID, Currency.USD, 19d)

            accountRepository.findAllByUserIdAndCurrencyIn(*_) >> Arrays.asList(accountPLN, accountUSD)
            exchangeServiceClient.getRatesForCurrency("USD") >> createExchangeRateResponse(Currency.USD, 4d, 5d)
        when:
            accountService.exchange(USER_ID, exchangeData)
        then:
            thrown(InsufficientFundsException)
    }

    def "exchange throws exception if external data provider doesn't response" (){
        given:
            def exchangeData = ExchangeData.from("USD", "PLN", 20d)
            def accountPLN = AccountEntity.from(USER_ID, Currency.PLN, 0d)
            def accountUSD = AccountEntity.from(USER_ID, Currency.USD, 19d)

            accountRepository.findAllByUserIdAndCurrencyIn(*_) >> Arrays.asList(accountPLN, accountUSD)
            exchangeServiceClient.getRatesForCurrency("USD") >> createExchangeRateResponse(Currency.USD, 4d, 5d)
        when:
            accountService.exchange(USER_ID, exchangeData)
        then:
            thrown(InsufficientFundsException)
    }

    def createExchangeRateResponse(Currency currency, double bid, double ask) {
       new ExchangeRateResponse(
           code: currency.name(),
           rates: Collections.singletonList(new Rate(bid: bid, ask: ask))
        )
    }

}
