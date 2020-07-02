package com.godnedy.bank.account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
interface AccountRepository extends CrudRepository<Account, UUID> {

    List<Account> findAllByUserIdAndCurrencyIn(UUID userId, List<Currency> currency);
    List<Account> findAllByUserId(UUID userId);
}
