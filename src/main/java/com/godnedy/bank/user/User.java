package com.godnedy.bank.user;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class User {

    UUID id;

    String personalIdNumber;

    String fullName;

    static User of(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getPersonalIdNumber(), userEntity.getFullName());
    }
}
