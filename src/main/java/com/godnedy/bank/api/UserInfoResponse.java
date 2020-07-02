package com.godnedy.bank.api;

import java.util.List;

import com.godnedy.bank.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserInfoResponse {
    User user;
    List<AccountInfo> accountInfo;
}
