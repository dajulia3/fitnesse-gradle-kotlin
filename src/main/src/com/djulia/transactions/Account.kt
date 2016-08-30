package com.djulia.transactions

import java.math.BigDecimal

data class Account(val accountNumber: String, val balance: BigDecimal, val status: Account.Status) {
    fun accountDebitedBy(amountToDebit: BigDecimal): Account {
        return Account(accountNumber, balance.subtract(amountToDebit), status)
    }

    enum class Status {
        OPEN,
        CLOSED,
        FROZEN
    }

    fun balanceSufficientToCoverDebit(amountToWithdraw: BigDecimal): Boolean {
        return accountDebitedBy(amountToWithdraw).balance.compareTo(BigDecimal(0)) < 0
    }

    fun accountIsActive(): Boolean {
        return status == Status.OPEN
    }
}
