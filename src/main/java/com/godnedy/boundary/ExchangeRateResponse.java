package com.godnedy.boundary;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Data
@NoArgsConstructor
public class ExchangeRateResponse {

    String code;
    List<Rate> rates;


    public BigDecimal getBid() {
        return BigDecimal.valueOf(rates.get(0).bid).setScale(4, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getAsk() {
        return BigDecimal.valueOf(rates.get(0).ask).setScale(4, RoundingMode.HALF_EVEN);
    }
}