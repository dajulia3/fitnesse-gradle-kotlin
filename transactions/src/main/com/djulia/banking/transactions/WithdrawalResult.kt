package com.djulia.banking.transactions

import java.math.BigDecimal

class WithdrawalResult {
    val updatedAccount: Account?
    val maybeError: Error?

    sealed class Error {
        override fun equals(other: Any?): Boolean {
            return this.javaClass.equals(other?.javaClass)
        }

        override fun hashCode(): Int {
            return this.javaClass.hashCode()
        }

        class InvalidWithdrawalAmountError : Error()
        class InactiveAccountError : Error()
        class NoSuchAccountError : Error()
        data class InsufficientFundsError(val differenceInFunds: BigDecimal?) : Error()
    }


    private constructor(error: Error) {
        this.maybeError = error
        this.updatedAccount = null;
    }

    private constructor(updatedAccount: Account) {
        this.updatedAccount = updatedAccount
        this.maybeError = null
    }

    companion object {

        fun error(error: Error): WithdrawalResult {
            return WithdrawalResult(error)
        }

        fun success(updatedAccount: Account): WithdrawalResult {
            return WithdrawalResult(updatedAccount)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as WithdrawalResult

        if (updatedAccount != other.updatedAccount) return false
        if (maybeError != other.maybeError) return false

        return true
    }

    override fun hashCode(): Int {
        var result = updatedAccount?.hashCode() ?: 0
        result = 31 * result + (maybeError?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "WithdrawalResult(updatedAccount=$updatedAccount, maybeError=$maybeError)"
    }


}
