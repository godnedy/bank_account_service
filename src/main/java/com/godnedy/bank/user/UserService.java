package com.godnedy.bank.user;


import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE ,makeFinal = true)
public class UserService {

    UserRepository userRepository;

    public Optional<UserModel> findUser(String pesel) {
        return userRepository.findByPersonalIdentityNumber(pesel).map(UserModel::of);
    }

    public UserModel createUser(String fullName, String pesel) {
        User user = User.from(pesel, fullName);
        return UserModel.of(userRepository.save(user));
    }
}
