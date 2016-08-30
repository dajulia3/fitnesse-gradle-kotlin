package com.djulia.transactions;

import com.djulia.transactions.InMemoryOnlyBankingTransactionService.InactiveAccountException;
import com.djulia.transactions.InMemoryOnlyBankingTransactionService.InvalidWithdrawalAmountException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static com.djulia.transactions.InMemoryOnlyBankingTransactionService.*;
import static com.djulia.transactions.InMemoryOnlyBankingTransactionService.InsufficientFundsException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        Account result = bankingTransactionService.withdraw(accountNumberWith1500Balance, new BigDecimal(100));

        assertThat(result.getBalance()).isEqualTo(new BigDecimal(1400));
    }

    @Test
    public void withdrawThrowsError_whenAccountDoesNotExist() throws Exception {
        assertThatThrownBy(
                () -> bankingTransactionService.withdraw("not-a-real-account-number", new BigDecimal(100))
        ).isInstanceOf(NoSuchAccountException.class);

    }

    @Test
    public void withdrawingZeroDollarsOrLess_raisesAnError() throws Exception {
        assertThatThrownBy(
                () -> bankingTransactionService.withdraw(accountNumberWith1500Balance, new BigDecimal(0))
        ).isInstanceOf(InvalidWithdrawalAmountException.class);

        assertThatThrownBy(
                () -> bankingTransactionService.withdraw(accountNumberWith1500Balance, new BigDecimal(-1))
        ).isInstanceOf(InvalidWithdrawalAmountException.class);
    }

    @Test
    public void withdrawingMoreThanAccountBalance_raisesAnError() throws Exception {
        assertThatThrownBy(
                () -> bankingTransactionService.withdraw(accountNumberWith1500Balance, new BigDecimal(1501))
        ).isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    public void withdrawingFromANonOpenAccount_raisesAnError() {
        assertThatThrownBy(
                () -> bankingTransactionService.withdraw(closedAccountNumber, new BigDecimal(20))
        ).isInstanceOf(InactiveAccountException.class);

        assertThatThrownBy(
                () -> bankingTransactionService.withdraw(frozenAccountNumber, new BigDecimal(20))
        ).isInstanceOf(InactiveAccountException.class);
    }

}