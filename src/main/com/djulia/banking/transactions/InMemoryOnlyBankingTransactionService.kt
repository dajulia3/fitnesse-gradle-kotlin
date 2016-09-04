package com.djulia.banking.transactions

import java.math.BigDecimal

internal class InMemoryOnlyBankingTransactionService(private val accountRepo: AccountRepo) : BankingTransactionService {

    override fun withdraw(accountId: String, amountToWithdraw: BigDecimal): WithdrawalResult {
        val maybeAccount = accountRepo.findById(accountId) ?: return WithdrawalResult.error(WithdrawalResult.Error.NoSuchAccountError())

        if (!maybeAccount.accountIsActive()) {
            return WithdrawalResult.error(WithdrawalResult.Error.InactiveAccountError())
        }

        if (withdrawalAmountAboveZero(amountToWithdraw)) {
            return WithdrawalResult.error(WithdrawalResult.Error.InvalidWithdrawalAmountError())
        }

        if (maybeAccount.balanceSufficientToCoverDebit(amountToWithdraw)) {
            return WithdrawalResult.error(WithdrawalResult.Error.InsufficientFundsError(maybeAccount.balance.subtract(amountToWithdraw).abs()))
        }

        return WithdrawalResult.success(maybeAccount.accountDebitedBy(amountToWithdraw))
    }

    private fun withdrawalAmountAboveZero(amountToWithdraw: BigDecimal): Boolean {
        return amountToWithdraw.compareTo(BigDecimal(0)) <= 0
    }

}
