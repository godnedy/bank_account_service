package com.godnedy.boundary;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ExchangeRateResponse {

    String code;
    List<Rate> rates;


    public Double getBid() {
        return rates.get(0).bid;
    }

    public Double getAsk() {
        return rates.get(0).ask;
    }
}