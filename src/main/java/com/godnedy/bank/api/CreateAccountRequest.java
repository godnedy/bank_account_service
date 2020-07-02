package com.godnedy.bank.api;

import com.godnedy.bank.user.PeselValidator;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateAccountRequest {

    @NotEmpty
    String fullName;

    @NotNull
    Double initialBalance;

    @Size(min = 11, max = 11)
    @NotEmpty
    String peselNumber;

    boolean isValid() {
        return PeselValidator.validate(peselNumber);
    }  //TODO ewt pesel jako obiekt
}
