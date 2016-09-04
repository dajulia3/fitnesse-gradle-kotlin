package com.djulia.banking.transactions

import java.math.BigDecimal

interface BankingTransactionService {
    fun withdraw(accountId: String, amountToWithdraw: BigDecimal): WithdrawalResult
}
