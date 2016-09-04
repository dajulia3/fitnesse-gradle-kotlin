package com.djulia.transactions

import org.junit.Before
import org.junit.Test

import java.math.BigDecimal
import org.assertj.core.api.KotlinAssertions.assertThat

class InMemoryOnlyBankingTransactionServiceTest {
    private lateinit var bankingTransactionService: BankingTransactionService
    private val accountNumberWith1500Balance = "123456789ABC"
    private val closedAccountNumber = "999912932"
    private val frozenAccountNumber = "723623921"

    @Before
    fun setup() {
        val accountRepo = InMemoryAccountRepo()
        bankingTransactionService = InMemoryOnlyBankingTransactionService(accountRepo)
        accountRepo.save(Account(accountNumberWith1500Balance, BigDecimal(1500), Account.Status.OPEN))
        accountRepo.save(Account(closedAccountNumber, BigDecimal(1500), Account.Status.CLOSED))
        accountRepo.save(Account(frozenAccountNumber, BigDecimal(1500), Account.Status.FROZEN))
    }


    @Test
    @Throws(Exception::class)
    fun withdrawReturnsNewAccountWithLowerBalance_whenWithdrawalUnderCurrentBalance() {
        val result = bankingTransactionService.withdraw(accountNumberWith1500Balance, BigDecimal(100))

        assertThat(result.updatedAccount?.balance).isEqualTo(BigDecimal(1400))
    }

    @Test
    @Throws(Exception::class)
    fun withdrawThrowsError_whenAccountDoesNotExist() {
        val result = bankingTransactionService.withdraw("not-a-real-account-number", BigDecimal(100))
        assertThat(result).isEqualTo(WithdrawalResult.error(WithdrawalResult.Error.NoSuchAccountError()))
    }

    @Test
    @Throws(Exception::class)
    fun withdrawingZeroDollarsOrLess_returnsAnError() {
        val withdrawZeroResult = bankingTransactionService.withdraw(accountNumberWith1500Balance, BigDecimal(0))
        assertThat(withdrawZeroResult).isEqualTo(WithdrawalResult.error(WithdrawalResult.Error.InvalidWithdrawalAmountError()))

        val negativeWithdrawalResult = bankingTransactionService.withdraw(accountNumberWith1500Balance, BigDecimal(-1))
        assertThat(negativeWithdrawalResult).isEqualTo(WithdrawalResult.error(WithdrawalResult.Error.InvalidWithdrawalAmountError()))
    }

    @Test
    @Throws(Exception::class)
    fun withdrawingMoreThanAccountBalance_returnsAnError() {
        val result = bankingTransactionService.withdraw(accountNumberWith1500Balance, BigDecimal(1501))
        assertThat(result).isEqualTo(WithdrawalResult.error(WithdrawalResult.Error.InsufficientFundsError(BigDecimal(1))))
    }

    @Test
    fun withdrawingFromANonOpenAccount_returnsAnError() {
        val closedAccountResult = bankingTransactionService.withdraw(closedAccountNumber, BigDecimal(20))
        assertThat(closedAccountResult).isEqualTo(WithdrawalResult.error(WithdrawalResult.Error.InactiveAccountError()))

        val frozenAccountResult = bankingTransactionService.withdraw(frozenAccountNumber, BigDecimal(20))
        assertThat(frozenAccountResult).isEqualTo(WithdrawalResult.error(WithdrawalResult.Error.InactiveAccountError()))
    }
}