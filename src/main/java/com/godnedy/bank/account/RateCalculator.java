package com.godnedy.bank.account;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.godnedy.boundary.ExchangeRateResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
class RateCalculator {

    public static BigDecimal calculateAmountToAdd(Currency accountCurrency, BigDecimal amount, ExchangeRateResponse exchangeRateResponse) {
        BigDecimal rate;
        if (accountCurrency.isPrimary()) {
            rate = BigDecimal.ONE.setScale(4, RoundingMode.HALF_EVEN).divide(exchangeRateResponse.getAsk(), RoundingMode.HALF_EVEN).setScale(20, RoundingMode.HALF_EVEN);
        } else {
            rate = exchangeRateResponse.getBid();
        }
        return rate.multiply(amount).setScale(2, RoundingMode.HALF_EVEN);
    }

}
