package com.godnedy.bank.api;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.godnedy.bank.user.PersonalIdNumberValidator;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @NotEmpty
    String fullName;

    @NotNull
    Double initialBalance;

    @Size(min = 11, max = 11)
    @NotEmpty
    String personalIdNumber;

    boolean isValid() {
        return PersonalIdNumberValidator.validate(personalIdNumber);
    }
}
