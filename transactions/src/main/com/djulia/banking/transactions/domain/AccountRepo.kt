package com.djulia.banking.transactions.domain

import java.util.Optional

internal interface AccountRepo {
    fun findById(accountId: String): Account?
    fun save(account: Account)
}
