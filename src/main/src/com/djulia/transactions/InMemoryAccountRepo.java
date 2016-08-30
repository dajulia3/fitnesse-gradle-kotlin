package com.djulia.transactions;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryAccountRepo implements AccountRepo {
    private final Map<String, Account> accountStore = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findById(String accountId) {
        Account account = accountStore.get(accountId);
        if(account == null){
            return Optional.empty();
        }
        return Optional.of(account);
    }

    @Override
    public void save(Account account) {
        accountStore.put(account.getAccountNumber(), account);
    }

}
