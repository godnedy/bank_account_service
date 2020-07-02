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
class UserEntity {

    @Id
    UUID id;
    String personalIdNumber;
    String fullName;

    static UserEntity from(String personalIdNumber, String fullName) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setPersonalIdNumber(personalIdNumber);
        userEntity.setFullName(fullName);
        return userEntity;
    }
}
