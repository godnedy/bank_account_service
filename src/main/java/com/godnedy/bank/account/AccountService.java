package com.godnedy.bank.account;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.springframework.transaction.annotation.Transactional;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AccountService {

    AccountRepository accountRepository;
    ExchangeServiceClient exchangeServiceClient;

    @Transactional
    public void createAccount(UUID userId, Currency currency, double initialBalance) {
      Account account = Account.from(userId, currency, initialBalance);
      accountRepository.save(account);
    }

    public List<AccountModel> getUserAccounts (UUID userId) {
       return accountRepository.findAllByUserId(userId).stream().map(AccountModel::of).collect(Collectors.toList());
    }

    @Transactional
        public boolean exchange(UUID userId, ExchangeData exchangeData) {
        ExchangeRateResponse exchangeRateResponse = exchangeData.getCurrencyFrom().isPrimary()
                ? exchangeServiceClient.getRatesForCurrency(exchangeData.getCurrencyTo().name())
                : exchangeServiceClient.getRatesForCurrency(exchangeData.getCurrencyFrom().name());

        BigDecimal amountToBeExchanged = exchangeData.getAmount();
        List<Currency> currencies = Arrays.asList(exchangeData.getCurrencyFrom(), exchangeData.getCurrencyTo());
        Map<Currency, Account> userAccounts = accountRepository.findAllByUserIdAndCurrencyIn(userId, currencies)
                .stream().collect(Collectors.toMap(Account::getCurrency, Function.identity()));
        AccountModel accountFrom = AccountModel.of(userAccounts.get(exchangeData.getCurrencyFrom()));
        AccountModel accountTo = AccountModel.of(userAccounts.get(exchangeData.getCurrencyTo()));


        if (accountFrom.getBalance().compareTo(amountToBeExchanged) < 0) {
            throw new InsufficientFundsException();
        }
        accountFrom.withdrawFromAccount(amountToBeExchanged);
        //TODO kiedy ask a kiedy bid
        accountTo.addToAccount(calculateAmountToAdd(exchangeData.getCurrencyFrom(), amountToBeExchanged, exchangeRateResponse));
        accountRepository.save(Account.from(accountFrom));
        accountRepository.save(Account.from(accountTo));

        return true;
    }

    private BigDecimal calculateAmountToAdd(Currency accountCurrency, BigDecimal amount, ExchangeRateResponse exchangeRateResponse) {
        BigDecimal rate;
        if (accountCurrency.isPrimary()) {
            rate = BigDecimal.ONE.setScale(2, RoundingMode.HALF_EVEN).divide(BigDecimal.valueOf(exchangeRateResponse.getAsk()), RoundingMode.HALF_EVEN).setScale(20, RoundingMode.HALF_EVEN);
        } else {
            rate = BigDecimal.valueOf(exchangeRateResponse.getBid()).setScale(2, RoundingMode.HALF_EVEN);
        }
        return rate.multiply(amount);
    }


}
