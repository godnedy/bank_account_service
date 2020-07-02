package com.godnedy.bank.user;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class User {

    @Id
    UUID id;
    String personalIdentityNumber;
    String fullName;

    static User from(String personalIdentityNumber, String fullName) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setPersonalIdentityNumber(personalIdentityNumber);
        user.setFullName(fullName);
        return user;
    }
}
