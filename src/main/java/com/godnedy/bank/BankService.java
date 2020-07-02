package com.godnedy.bank;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.godnedy.bank.account.AccountService;
import com.godnedy.bank.account.Currency;
import com.godnedy.bank.api.CreateAccountRequest;
import com.godnedy.bank.user.AccountInfo;
import com.godnedy.bank.user.UserInfoResponse;
import com.godnedy.bank.user.UserModel;
import com.godnedy.bank.user.UserNotFoundException;
import com.godnedy.bank.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class BankService {

    @Delegate
    UserService userService;

    AccountService accountService;

    @Transactional
    public boolean createAccount(CreateAccountRequest request) {
        try {
            UserModel user = userService.createUser(request.getPeselNumber(), request.getFullName());
            accountService.createAccount(user.getId(), Currency.PLN, request.getInitialBalance());
            accountService.createAccount(user.getId(), Currency.USD, 0.00d);

        } catch (DataAccessException e) {
            log.error(String.format("Failed to create user with pesel number %s", request.getPeselNumber()));
            return false;
        }
        return true;
    }

    public UserInfoResponse getUserInfo(String peselNumber) {
        Optional<UserModel> user = userService.findUser(peselNumber);
        if(!user.isPresent()) {
            throw new UserNotFoundException();
        }
        List<AccountInfo> userAccounts = accountService.getUserAccounts(user.get().getId())
                .stream()
                .map(AccountInfo::from)
                .collect(Collectors.toList());
        return new UserInfoResponse(user.get(), userAccounts);
    }

    @Transactional
    public boolean exchange (String peselNumber, ExchangeData exchangeData) {
       return userService.findUser(peselNumber)
               .map(userModel -> accountService.exchange(userModel.getId(), exchangeData))
               .orElse(false);
    }
}
