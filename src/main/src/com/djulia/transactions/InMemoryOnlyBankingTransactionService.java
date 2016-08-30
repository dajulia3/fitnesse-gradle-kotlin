package com.djulia.transactions;

import java.math.BigDecimal;

class InMemoryOnlyBankingTransactionService implements BankingTransactionService {

    private final AccountRepo accountRepo;

    InMemoryOnlyBankingTransactionService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public Account withdraw(String accountId, BigDecimal amountToWithdraw) {
        Account account = accountRepo.findById(accountId).orElseThrow(NoSuchAccountException::new);

        if (!account.accountIsActive()) {
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

    public static class NoSuchAccountException extends RuntimeException {
    }
}
