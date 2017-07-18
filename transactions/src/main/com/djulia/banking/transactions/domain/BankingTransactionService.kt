package com.djulia.banking.transactions.domain

import java.math.BigDecimal

interface BankingTransactionService {
    fun withdraw(accountId: String, amountToWithdraw: BigDecimal): WithdrawalResult
}
