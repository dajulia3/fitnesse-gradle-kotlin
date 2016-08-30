package com.djulia.transactions

import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

internal class InMemoryAccountRepo : AccountRepo {
    private val accountStore = ConcurrentHashMap<String, Account>()

    override fun findById(accountId: String): Account? {
        val account = accountStore[accountId] ?: return null
        return account
    }

    override fun save(account: Account) {
        accountStore.put(account.accountNumber, account)
    }

}
