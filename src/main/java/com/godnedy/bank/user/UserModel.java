package com.godnedy.bank.user;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserModel {

    UUID id;

    String personalIdentityNumber;

    String fullName;

    static UserModel of(User user) {
        return new UserModel(user.getId(), user.getPersonalIdentityNumber(), user.getFullName());
    }
}
