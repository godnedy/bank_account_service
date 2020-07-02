package com.godnedy.bank.api;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
class ExchangeRequest {

    @NotEmpty
    String currencyFrom;

    @NotEmpty
    String currencyTo;

    @Positive
    Double amount;

}
