package com.godnedy.bank.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SUB_ACCOUNT")
public class Account {

    @Id
    private UUID id;
    private Currency currency;
    private Double balance;
    private UUID userId;

    static Account from(UUID userId, Currency currency, Double initialBalance) {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setCurrency(currency);
        account.setBalance(initialBalance);
        account.setUserId(userId);
        return account;
    }

    static Account from(AccountModel model) {
        Account account = new Account();
        account.setId(model.getId());
        account.setCurrency(model.getCurrency());
        account.setBalance(Double.valueOf(model.getBalance().toString()));
        account.setUserId(model.getUserId());
        return account;
    }
}
