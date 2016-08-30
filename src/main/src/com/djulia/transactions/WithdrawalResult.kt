package com.djulia.transactions

import java.math.BigDecimal
import java.util.Objects
import java.util.Optional
import java.util.function.Function

class WithdrawalResult {

    val updatedAccount: Account?
    val maybeError: Error?

    //Now we want to associate information with our errors!
    sealed class Error {
        class InvalidWithdrawalAmountError : Error()
        class InactiveAccountError : Error()
        class NoSuchAccountError : Error()
        class InsufficientFundsError(val differenceInFunds: BigDecimal?) : Error() {
            override fun equals(o: Any?): Boolean {
                if (this === o) return true
                if (o == null || javaClass != o.javaClass) return false
                if (!super.equals(o)) return false

                val that = o as InsufficientFundsError?

                return if (differenceInFunds != null) differenceInFunds == that?.differenceInFunds else that?.differenceInFunds == null

            }

            override fun hashCode(): Int {
                var result = super.hashCode()
                result = 31 * result + if (differenceInFunds != null) differenceInFunds.hashCode() else 0
                return result
            }
        }
    }



    private constructor(error: Error) {
        this.maybeError = error
        this.updatedAccount = null;
    }

    private constructor(updatedAccount: Account) {
        this.updatedAccount = updatedAccount
        this.maybeError = null

    }

    val isSuccessful: Boolean
        get() = if(maybeError == null){true } else false

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val that = o as WithdrawalResult?

        if (if (updatedAccount != null) updatedAccount != that?.updatedAccount else that?.updatedAccount != null)
            return false
        return if (maybeError != null) maybeError == that?.maybeError else that?.maybeError == null

    }

    override fun hashCode(): Int {
        var result = if (updatedAccount != null) updatedAccount.hashCode() else 0
        result = 31 * result + if (maybeError != null) maybeError.hashCode() else 0
        return result
    }

    override fun toString(): String {
        return "WithdrawalResult{updatedAccount=$updatedAccount, maybeError=$maybeError}"
    }

    companion object {

        fun error(error: Error): WithdrawalResult {
            return WithdrawalResult(error)
        }

        fun success(updatedAccount: Account): WithdrawalResult {
            return WithdrawalResult(updatedAccount)
        }
    }

}
