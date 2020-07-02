package com.godnedy.bank.api;

import com.godnedy.bank.BankService;
import com.godnedy.bank.ExchangeData;
import com.godnedy.bank.UserNotFoundException;
import com.godnedy.bank.account.InsufficientFundsException;
import com.godnedy.bank.user.SameUserExistsException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping("/api/user")
public class BankController {

    BankService bankService;

    @PostMapping
    public ResponseEntity createAccount(@Validated @RequestBody CreateAccountRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return bankService.createAccount(request) ? new ResponseEntity<>(CREATED) : new ResponseEntity<>(CONFLICT);
    }

    @GetMapping("/{personalIdNumber}")
    public ResponseEntity getUserInfo(@PathVariable String personalIdNumber) {
        return new ResponseEntity<>(bankService.getUserInfo(personalIdNumber), OK);
    }

    @PostMapping("/{personalIdNumber}/exchange")
    public ResponseEntity exchange(@PathVariable String personalIdNumber, @Validated @RequestBody ExchangeRequest request) {
        ExchangeData exchangeData = ExchangeData.from(request.currencyFrom, request.currencyTo, request.amount);
        return bankService.exchange(personalIdNumber, exchangeData)
                ? new ResponseEntity<>(OK)
                : new ResponseEntity<>(BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public void handleException(IllegalArgumentException e) {
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public void handleUserNotFoundException(UserNotFoundException e) {
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(BAD_REQUEST)
    public void handleInsufficientFundsException() {}

    @ExceptionHandler(SameUserExistsException.class)
    @ResponseStatus(CONFLICT)
    public void handleSameUserExistsException() {}
}
