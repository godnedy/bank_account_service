package com.godnedy.bank.api;

import com.godnedy.bank.account.Account;
import com.godnedy.bank.account.Currency;
import lombok.Data;

@Data
public class AccountInfo {
    Currency currency;
    Double balance;

    public static AccountInfo from(Account account) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setCurrency(account.getCurrency());
        accountInfo.setBalance(account.getBalance().doubleValue());
        return accountInfo;
    }

}
