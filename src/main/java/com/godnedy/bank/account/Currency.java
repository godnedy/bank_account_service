package com.godnedy.bank.account;

import lombok.Getter;

@Getter
public enum Currency {
    PLN(true),
    USD(false);

    private final boolean primary;

    Currency(boolean primary) {
       this.primary = primary;
    }
}
