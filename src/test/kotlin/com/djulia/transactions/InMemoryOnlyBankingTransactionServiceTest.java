package com.djulia.transactions;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.djulia.transactions.WithdrawalResult.Error.*;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryOnlyBankingTransactionServiceTest {
    private BankingTransactionService bankingTransactionService;
    private String accountNumberWith1500Balance = "123456789ABC";
    private String closedAccountNumber = "999912932";
    private String frozenAccountNumber = "723623921";

    @Before
    public void setup() {
        InMemoryAccountRepo accountRepo = new InMemoryAccountRepo();
        bankingTransactionService = new InMemoryOnlyBankingTransactionService(accountRepo);
        accountRepo.save(new Account(accountNumberWith1500Balance, new BigDecimal(1500), Account.Status.OPEN));
        accountRepo.save(new Account(closedAccountNumber, new BigDecimal(1500), Account.Status.CLOSED));
        accountRepo.save(new Account(frozenAccountNumber, new BigDecimal(1500), Account.Status.FROZEN));
    }

    @Test
    public void withdrawReturnsNewAccountWithLowerBalance_whenWithdrawalUnderCurrentBalance() throws Exception {
        WithdrawalResult result = bankingTransactionService.withdraw(accountNumberWith1500Balance, new BigDecimal(100));

        assertThat(result.getUpdatedAccount().get().getBalance()).isEqualTo(new BigDecimal(1400));
    }

    @Test
    public void withdrawThrowsError_whenAccountDoesNotExist() throws Exception {
        WithdrawalResult result = bankingTransactionService.withdraw("not-a-real-account-number", new BigDecimal(100));
        assertThat(result).isEqualTo(WithdrawalResult.error(new WithdrawalResult.NoSuchAccountError()));
    }

    @Test
    public void withdrawingZeroDollarsOrLess_returnsAnError() throws Exception {
        WithdrawalResult withdrawZeroResult = bankingTransactionService.withdraw(accountNumberWith1500Balance, new BigDecimal(0));
        assertThat(withdrawZeroResult).isEqualTo(WithdrawalResult.error(new WithdrawalResult.InvalidWithdrawalAmountError()));

        WithdrawalResult negativeWithdrawalResult = bankingTransactionService.withdraw(accountNumberWith1500Balance, new BigDecimal(-1));
        assertThat(negativeWithdrawalResult).isEqualTo(WithdrawalResult.error(new WithdrawalResult.InvalidWithdrawalAmountError()));
    }

    @Test
    public void withdrawingMoreThanAccountBalance_returnsAnError() throws Exception {
        WithdrawalResult result = bankingTransactionService.withdraw(accountNumberWith1500Balance, new BigDecimal(1501));
        assertThat(result).isEqualTo(WithdrawalResult.error(new WithdrawalResult.InsufficientFundsError(new BigDecimal(1))));
    }

    @Test
    public void withdrawingFromANonOpenAccount_returnsAnError() {
        WithdrawalResult closedAccountResult = bankingTransactionService.withdraw(closedAccountNumber, new BigDecimal(20));
        assertThat(closedAccountResult).isEqualTo(WithdrawalResult.error(new WithdrawalResult.InactiveAccountError()));

        WithdrawalResult frozenAccountResult = bankingTransactionService.withdraw(frozenAccountNumber, new BigDecimal(20));
        assertThat(frozenAccountResult).isEqualTo(WithdrawalResult.error(new WithdrawalResult.InactiveAccountError()));
    }

}