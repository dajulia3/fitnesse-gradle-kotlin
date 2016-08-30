package com.djulia.transactions

import java.util.Optional

internal interface AccountRepo {
    fun findById(accountId: String): Account?
    fun save(account: Account)
}
