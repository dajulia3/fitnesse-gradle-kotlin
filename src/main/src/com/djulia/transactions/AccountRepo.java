package com.djulia.transactions;

import java.util.Optional;

interface AccountRepo {
    Optional<Account> findById(String accountId);
    void save(Account account);
}
