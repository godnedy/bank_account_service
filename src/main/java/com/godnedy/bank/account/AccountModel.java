package com.godnedy.bank.account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
public class AccountModel {

    private final UUID id;
    private final Currency currency;
    private BigDecimal balance;
    private final UUID userId;

    static AccountModel of(Account account) {
        BigDecimal balance = BigDecimal.valueOf(account.getBalance());
        balance = balance.setScale(2, RoundingMode.HALF_EVEN);
        return new AccountModel(account.getId(), account.getCurrency(), balance, account.getUserId());
    }

    void addToAccount(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    void withdrawFromAccount(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

}
