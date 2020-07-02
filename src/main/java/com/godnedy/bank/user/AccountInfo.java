package com.godnedy.bank.user;

import java.math.BigDecimal;

import com.godnedy.bank.account.AccountModel;
import com.godnedy.bank.account.Currency;
import lombok.Data;

@Data
public class AccountInfo {
    Currency currency;
    BigDecimal balance;

    public static AccountInfo from(AccountModel accountModel) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setCurrency(accountModel.getCurrency());
        accountInfo.setBalance(accountModel.getBalance());
        return accountInfo;
    }

}
