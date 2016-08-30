package com.djulia.transactions;

import java.math.BigDecimal;
import java.util.Optional;

class InMemoryOnlyBankingTransactionService implements BankingTransactionService {

    private final AccountRepo accountRepo;

    InMemoryOnlyBankingTransactionService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public WithdrawalResult withdraw(String accountId, BigDecimal amountToWithdraw) {
        Optional<Account> maybeAccount = accountRepo.findById(accountId);

        if(!maybeAccount.isPresent()) {
            return WithdrawalResult.error(WithdrawalResult.Error.NO_SUCH_ACCOUNT);
        }

        Account account = maybeAccount.get();

        if (!account.accountIsActive()) {
            return WithdrawalResult.error(WithdrawalResult.Error.INACTIVE_ACCOUNT);
        }

        if (withdrawalAmountAboveZero(amountToWithdraw)) {
            return WithdrawalResult.error(WithdrawalResult.Error.INVALID_WITHDRAWAL_AMOUNT);
        }

        if (account.balanceSufficientToCoverDebit(amountToWithdraw)) {
            return WithdrawalResult.error(WithdrawalResult.Error.INSUFFICIENT_FUNDS);
        }

        return WithdrawalResult.success(account.accountDebitedBy(amountToWithdraw));
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
