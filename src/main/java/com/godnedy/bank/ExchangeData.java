package com.godnedy.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.godnedy.bank.account.Currency;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeData {
    private Currency currencyFrom;
    private Currency currencyTo;
    private BigDecimal amount;

    public static ExchangeData from(String currencyFrom, String currencyTo, Double amount) {
        Currency from = Currency.valueOf(currencyFrom);
        Currency to = Currency.valueOf(currencyTo);
        if(from.equals(to)) {
            throw new IllegalArgumentException();
        }
        BigDecimal a = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_EVEN);
        return new ExchangeData(from, to, a);
    }
}
