package com.godnedy.bank.account;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum Currency {
    PLN(true),
    USD(false);

    private final boolean primary;

    Currency(boolean primary) {
       this.primary = primary;
    }
}
