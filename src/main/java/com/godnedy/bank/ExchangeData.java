package com.godnedy.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.godnedy.bank.account.Currency;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ExchangeData {
    Currency currencyFrom;
    Currency currencyTo;
    BigDecimal amount;

    public static ExchangeData from(String currencyFrom, String currencyTo, Double amount) {
        Currency from = Currency.valueOf(currencyFrom.toUpperCase());
        Currency to = Currency.valueOf(currencyTo.toUpperCase());
        if(from.equals(to)) {
            throw new IllegalArgumentException();
        }
        BigDecimal a = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_EVEN);
        return new ExchangeData(from, to, a);
    }
}
