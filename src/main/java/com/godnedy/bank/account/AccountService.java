package com.godnedy.bank.account;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.godnedy.bank.ExchangeData;
import com.godnedy.boundary.ExchangeRateResponse;
import com.godnedy.boundary.ExchangeServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AccountService {

    AccountRepository accountRepository;
    ExchangeServiceClient exchangeServiceClient;

    @Transactional
    public void createAccount(UUID userId, Currency currency, double initialBalance) {
      AccountEntity accountEntity = AccountEntity.from(userId, currency, initialBalance);
      accountRepository.save(accountEntity);
    }

    public List<Account> getUserAccounts (UUID userId) {
        return accountRepository.findAllByUserId(userId).stream().map(Account::of).collect(Collectors.toList());
    }

    @Transactional
    public boolean exchange(UUID userId, ExchangeData exchangeData) {

        BigDecimal amountToBeExchanged = exchangeData.getAmount();
        List<Currency> currencies = Arrays.asList(exchangeData.getCurrencyFrom(), exchangeData.getCurrencyTo());
        Map<Currency, AccountEntity> userAccounts = accountRepository.findAllByUserIdAndCurrencyIn(userId, currencies)
                .stream().collect(Collectors.toMap(AccountEntity::getCurrency, Function.identity()));
        Account accountFrom = Account.of(userAccounts.get(exchangeData.getCurrencyFrom()));
        Account accountTo = Account.of(userAccounts.get(exchangeData.getCurrencyTo()));

        if (accountFrom.getBalance().compareTo(amountToBeExchanged) < 0) {
            throw new InsufficientFundsException();
        }

        accountFrom.withdrawFromAccount(amountToBeExchanged);
        accountTo.addToAccount(RateCalculator.calculateAmountToAdd(exchangeData.getCurrencyFrom(), amountToBeExchanged, getExchangeRateResponse(exchangeData)));

        accountRepository.save(AccountEntity.from(accountFrom));
        accountRepository.save(AccountEntity.from(accountTo));

        return true;
    }

    private ExchangeRateResponse getExchangeRateResponse(ExchangeData exchangeData) {
        return exchangeData.getCurrencyFrom().isPrimary()
                ? exchangeServiceClient.getRatesForCurrency(exchangeData.getCurrencyTo().name())
                : exchangeServiceClient.getRatesForCurrency(exchangeData.getCurrencyFrom().name());
    }

}
