package com.djulia.transactions;

import java.math.BigDecimal;

public class BankingTransactionService {

    public Account withdraw(Account account, BigDecimal amountToWithdraw) {
        if(!account.accountIsActive()){
            throw new InactiveAccountException();
        }

        if (withdrawalAmountAboveZero(amountToWithdraw)) {
            throw new InvalidWithdrawalAmountException();
        }

        if (account.balanceSufficientToCoverDebit(amountToWithdraw)) {
            throw new InsufficientFundsException();
        }

        return account.accountDebitedBy(amountToWithdraw);
    }

    private boolean withdrawalAmountAboveZero(BigDecimal amountToWithdraw) {
        return amountToWithdraw.compareTo(new BigDecimal(0)) <= 0;
    }

    public static class InvalidWithdrawalAmountException extends RuntimeException {

    }

    public static class InsufficientFundsException extends RuntimeException {

    }

    public static class InactiveAccountException extends RuntimeException {
    }
}
