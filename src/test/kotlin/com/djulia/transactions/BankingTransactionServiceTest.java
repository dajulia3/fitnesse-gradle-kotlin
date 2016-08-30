package com.djulia.transactions;

import com.djulia.transactions.BankingTransactionService.InactiveAccountException;
import com.djulia.transactions.BankingTransactionService.InvalidWithdrawalAmountException;
import org.junit.Test;

import java.math.BigDecimal;

import static com.djulia.transactions.BankingTransactionService.InsufficientFundsException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BankingTransactionServiceTest {
    private BankingTransactionService bankingTransactionService = new BankingTransactionService();
    private Account closedAccount = new Account("123456789ABC", new BigDecimal(1500), Account.Status.CLOSED);
    private Account accountWith1500Balance = new Account("123456789ABC", new BigDecimal(1500), Account.Status.OPEN);
    private Account frozenAccount = new Account("123456789ABC", new BigDecimal(1500), Account.Status.FROZEN);

    @Test
    public void withdrawReturnsNewAccountWithLowerBalance_whenWithdrawalUnderCurrentBalance() throws Exception {
        Account result = bankingTransactionService.withdraw(accountWith1500Balance, new BigDecimal(100));

        assertThat(result.getBalance()).isEqualTo(new BigDecimal(1400));
    }

    @Test
    public void withdrawingZeroDollarsOrLess_raisesAnError() throws Exception {
        assertThatThrownBy(
                () -> bankingTransactionService.withdraw(accountWith1500Balance, new BigDecimal(0))
        ).isInstanceOf(InvalidWithdrawalAmountException.class);

        assertThatThrownBy(
                () -> bankingTransactionService.withdraw(accountWith1500Balance, new BigDecimal(-1))
        ).isInstanceOf(InvalidWithdrawalAmountException.class);
    }

    @Test
    public void withdrawingMoreThanAccountBalance_raisesAnError() throws Exception {
        assertThatThrownBy(
                () -> bankingTransactionService.withdraw(accountWith1500Balance, new BigDecimal(1501))
        ).isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    public void withdrawingFromANonOpenAccount_raisesAnError(){
        assertThatThrownBy(
                () -> bankingTransactionService.withdraw(closedAccount, new BigDecimal(1500))
        ).isInstanceOf(InactiveAccountException.class);

        assertThatThrownBy(
                () -> bankingTransactionService.withdraw(frozenAccount, new BigDecimal(1500))
        ).isInstanceOf(InactiveAccountException.class);
    }

}