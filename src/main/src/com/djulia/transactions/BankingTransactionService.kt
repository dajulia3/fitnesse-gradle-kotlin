package com.djulia.transactions

import java.math.BigDecimal

interface BankingTransactionService {
    fun withdraw(accountId: String, amountToWithdraw: BigDecimal): WithdrawalResult
}
