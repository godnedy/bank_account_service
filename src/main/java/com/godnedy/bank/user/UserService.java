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

    public Optional<User> findUser(String personalIdNumber) {
        return userRepository.findByPersonalIdNumber(personalIdNumber).map(User::of);
    }

    public User createUser(String fullName, String personalIdNumber) {
        Optional<UserEntity> byPersonalIdNumber = userRepository.findByPersonalIdNumber(personalIdNumber);
        if(byPersonalIdNumber.isPresent()) {
            throw new SameUserExistsException();
        }
        UserEntity userEntity = UserEntity.from(personalIdNumber, fullName);
        return User.of(userRepository.save(userEntity));
    }
}
