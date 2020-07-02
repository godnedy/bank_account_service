package com.godnedy.bank.account;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SUB_ACCOUNT")
public class AccountEntity {

    @Id
    private UUID id;
    private Currency currency;
    private Double balance;
    private UUID userId;

    static AccountEntity from(UUID userId, Currency currency, Double initialBalance) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(UUID.randomUUID());
        accountEntity.setCurrency(currency);
        accountEntity.setBalance(initialBalance);
        accountEntity.setUserId(userId);
        return accountEntity;
    }

    static AccountEntity from(Account model) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(model.getId());
        accountEntity.setCurrency(model.getCurrency());
        accountEntity.setBalance(Double.valueOf(model.getBalance().toString()));
        accountEntity.setUserId(model.getUserId());
        return accountEntity;
    }
}
