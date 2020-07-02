package com.godnedy.bank.user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class UserInfoResponse {
    UserModel user;
    List<AccountInfo> accountInfo;
}
