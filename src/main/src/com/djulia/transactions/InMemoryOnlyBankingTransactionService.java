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
            return WithdrawalResult.error(new WithdrawalResult.NoSuchAccountError());
        }

        Account account = maybeAccount.get();

        if (!account.accountIsActive()) {
            return WithdrawalResult.error(new WithdrawalResult.InactiveAccountError());
        }

        if (withdrawalAmountAboveZero(amountToWithdraw)) {
            return WithdrawalResult.error(new WithdrawalResult.InvalidWithdrawalAmountError());
        }

        if (account.balanceSufficientToCoverDebit(amountToWithdraw)) {
            return WithdrawalResult.error(new WithdrawalResult.InsufficientFundsError());
        }

        return WithdrawalResult.success(account.accountDebitedBy(amountToWithdraw));
    }

    private boolean withdrawalAmountAboveZero(BigDecimal amountToWithdraw) {
        return amountToWithdraw.compareTo(new BigDecimal(0)) <= 0;
    }

}
