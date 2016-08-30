package com.djulia.transactions;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static com.djulia.transactions.TransactionsController.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionsControllerTest {

    private BankingTransactionService transactionService = mock(BankingTransactionService.class);
    private final ResultCapturingMessageConverter resultCapturingMessageConverter = new ResultCapturingMessageConverter();
    private MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TransactionsController(transactionService))
            .setMessageConverters(resultCapturingMessageConverter)
            .build();

    @Test
    public void makeWithdrawal() throws Exception {
        when(transactionService.withdraw(any(), any())).thenReturn(
                new Account("12345ABC", new BigDecimal(200), Account.Status.OPEN)
        );

        ;
        String content = JsonHelpers.serializeContentForMvcTest(
                new WithdrawalRequest("12345ABC", new BigDecimal(500))
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/withdrawals").content(
                content))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        assertThat(resultCapturingMessageConverter.getResult()).isEqualTo(new WithdrawalResponse(new BigDecimal(200)));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////// I had to KNOW to write these tests based on internals of the ////////////////
    ///////////// InMemoryOnlyBankingTransactionService class!!                ////////////////
    ///////////// YUCK!!!!!                                                    ////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void makeWithdrawal_errors_whenAccountIsInactive() throws Exception {
        when(transactionService.withdraw(any(), any())).thenThrow(
                InMemoryOnlyBankingTransactionService.InactiveAccountException.class
        );

        String content = JsonHelpers.serializeContentForMvcTest(
                new WithdrawalRequest("12345ABC", new BigDecimal(500))
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/withdrawals").content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThat(resultCapturingMessageConverter.getResult()).isEqualTo(new ErrorResponse("Inactive Account. Can't withdraw"));
    }

    @Test
    public void makeWithdrawal_errors_whenAccountHasInsufficientFunds() throws Exception {
        when(transactionService.withdraw(any(), any())).thenThrow(
                InMemoryOnlyBankingTransactionService.InsufficientFundsException.class
        );

        String content = JsonHelpers.serializeContentForMvcTest(
                new WithdrawalRequest("12345ABC", new BigDecimal(500))
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/withdrawals").content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThat(resultCapturingMessageConverter.getResult()).isEqualTo(new ErrorResponse("Insufficient funds"));
    }

    @Test
    public void makeWithdrawal_errors_whenAccountHasInvalideWithdrawalAmountException() throws Exception {
        when(transactionService.withdraw(any(), any())).thenThrow(
                InMemoryOnlyBankingTransactionService.InvalidWithdrawalAmountException.class
        );

        String content = JsonHelpers.serializeContentForMvcTest(
                new WithdrawalRequest("12345ABC", new BigDecimal(500))
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/withdrawals").content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        assertThat(resultCapturingMessageConverter.getResult()).isEqualTo(new ErrorResponse("Invalid withdrawal"));
    }
}