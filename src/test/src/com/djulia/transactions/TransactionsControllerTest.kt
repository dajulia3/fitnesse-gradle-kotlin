package com.djulia.transactions

import com.djulia.transactions.TransactionsController.WithdrawalRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.Test
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import src.com.djulia.transactions.whenever
import src.com.djulia.transactions.any
import src.com.djulia.transactions.anyObject

import java.math.BigDecimal

import org.assertj.core.api.KotlinAssertions.assertThat
import com.djulia.transactions.TransactionsController.ErrorResponse
import com.djulia.transactions.TransactionsController.WithdrawalResponse
import org.mockito.Mockito.*

class TransactionsControllerTest {

    private val transactionService = mock(BankingTransactionService::class.java)
    private val resultCapturingMessageConverter = ResultCapturingMessageConverter()
    private val mockMvc = MockMvcBuilders.standaloneSetup(TransactionsController(transactionService)).setMessageConverters(MappingJackson2HttpMessageConverter(ObjectMapper().registerModule(KotlinModule()))).setMessageConverters(resultCapturingMessageConverter).build()

    @Test
    @Throws(Exception::class)
    fun makeWithdrawal() {
        whenever(transactionService.withdraw(anyObject(), anyObject()))
                .thenReturn(WithdrawalResult.success(Account("12345ABC", BigDecimal(200), Account.Status.OPEN)))
        whenever(transactionService.withdraw(src.com.djulia.transactions.anyObject(), src.com.djulia.transactions.anyObject())).thenReturn(
                WithdrawalResult.success(Account("12345ABC", BigDecimal(200), Account.Status.OPEN)))
        val content = JsonHelpers.serializeContentForMvcTest(
                WithdrawalRequest("12345ABC", BigDecimal(500)))
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/withdrawals").content(
                content)).andExpect(MockMvcResultMatchers.status().isCreated)

        assertThat(resultCapturingMessageConverter.getResult()).isEqualTo(WithdrawalResponse(BigDecimal(200)))
    }

    @Test
    @Throws(Exception::class)
    fun makeWithdrawal_errors_whenAccountIsInactive() {
        whenever(transactionService.withdraw(any(), any())).thenReturn(
                WithdrawalResult.error(WithdrawalResult.Error.InactiveAccountError()))

        val content = JsonHelpers.serializeContentForMvcTest(
                WithdrawalRequest("12345ABC", BigDecimal(500)))
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/withdrawals").content(content)).andExpect(MockMvcResultMatchers.status().isBadRequest)

        assertThat(resultCapturingMessageConverter.getResult()).isEqualTo(ErrorResponse("Inactive Account. Can't withdraw"))
    }

    @Test
    @Throws(Exception::class)
    fun makeWithdrawal_errors_whenAccountHasInsufficientFunds() {
        whenever(transactionService.withdraw(any(), any())).thenReturn(
                WithdrawalResult.error(WithdrawalResult.Error.InsufficientFundsError(BigDecimal(55))))

        val content = JsonHelpers.serializeContentForMvcTest(
                WithdrawalRequest("12345ABC", BigDecimal(500)))
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/withdrawals").content(content)).andExpect(MockMvcResultMatchers.status().isBadRequest)

        assertThat(resultCapturingMessageConverter.getResult()).isEqualTo(ErrorResponse("Insufficient funds. Need $55 to complete transaction."))
    }

    @Test
    @Throws(Exception::class)
    fun makeWithdrawal_errors_whenAccountHasInvalideWithdrawalAmountException() {
        whenever(transactionService.withdraw(any(), any())).thenReturn(
                WithdrawalResult.error(WithdrawalResult.Error.InvalidWithdrawalAmountError()))

        val content = JsonHelpers.serializeContentForMvcTest(
                WithdrawalRequest("12345ABC", BigDecimal(500)))
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/withdrawals").content(content)).andExpect(MockMvcResultMatchers.status().isBadRequest)

        assertThat(resultCapturingMessageConverter.getResult()).isEqualTo(ErrorResponse("Invalid withdrawal"))
    }

    @Test
    @Throws(Exception::class)
    fun makeWithdrawal_errors_whenNoSuchAccountFound() {
        whenever(transactionService.withdraw(any(), any())).thenReturn(
                WithdrawalResult.error(WithdrawalResult.Error.NoSuchAccountError()))

        val content = JsonHelpers.serializeContentForMvcTest(
                WithdrawalRequest("12345ABC", BigDecimal(500)))
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/withdrawals").content(content)).andExpect(MockMvcResultMatchers.status().isBadRequest)

        assertThat(resultCapturingMessageConverter.getResult()).isEqualTo(ErrorResponse("No account found with the id 12345ABC"))
    }

}