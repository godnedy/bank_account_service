package com.godnedy.bank.account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Account {

    private final UUID id;
    private final Currency currency;
    private BigDecimal balance;
    private final UUID userId;

    static Account of(AccountEntity accountEntity) {
        BigDecimal balance = BigDecimal.valueOf(accountEntity.getBalance());
        balance = balance.setScale(2, RoundingMode.HALF_EVEN);
        return new Account(accountEntity.getId(), accountEntity.getCurrency(), balance, accountEntity.getUserId());
    }

    void addToAccount(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    void withdrawFromAccount(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

}
