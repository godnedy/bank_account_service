package com.godnedy.bank.account;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AccountRepository extends CrudRepository<AccountEntity, UUID> {

    List<AccountEntity> findAllByUserIdAndCurrencyIn(UUID userId, List<Currency> currency);
    List<AccountEntity> findAllByUserId(UUID userId);
}
