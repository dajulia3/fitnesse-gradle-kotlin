package com.djulia.transactions;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class WithdrawalResult {
    private final Optional<Account> updatedAccount;
    private final Optional<Error> maybeError;

    //Now we want to associate information with our errors!
    public static abstract class Error {
        @Override
        public boolean equals(Object obj) {
            return obj!= null && this.getClass().equals(obj.getClass());
        }

        @Override
        public int hashCode() {
            return this.getClass().toString().hashCode();
        }
        
    }

    public static class InvalidWithdrawalAmountError extends Error {
    }

    public static class InsufficientFundsError extends Error {
    }

    public static class InactiveAccountError extends Error {
    }

    public static class NoSuchAccountError extends Error {
    }

    private WithdrawalResult(Error error) {
        this.maybeError = Optional.of(error);
        this.updatedAccount = Optional.empty();
    }

    private WithdrawalResult(Account updatedAccount) {
        this.updatedAccount = Optional.of(updatedAccount);
        this.maybeError = Optional.empty();

    }

    public static WithdrawalResult error(Error error) {
        return new WithdrawalResult(error);
    }

    public static WithdrawalResult success(Account updatedAccount) {
        return new WithdrawalResult(updatedAccount);
    }

    public boolean isSuccessful() {
        return !maybeError.isPresent();
    }

    public Optional<Account> getUpdatedAccount() {
        return updatedAccount;
    }

    public <T> T match(Function<Account, T> successHandlerWithUpdatedAccount,
                       Function<InactiveAccountError, T> inactiveAccountHandler,
                       Function<InvalidWithdrawalAmountError, T> invalidWithdrawalAmountHandler,
                       Function<InsufficientFundsError, T> insufficientFundsHandler,
                       Function<NoSuchAccountError, T> noSuchAccountError
    ) {
        if (maybeError.isPresent()) {
            Error error = maybeError.get();

            //Yucky, but it works.:
            if (error instanceof InvalidWithdrawalAmountError) {
                return invalidWithdrawalAmountHandler.apply((InvalidWithdrawalAmountError) error);
            } else if (error instanceof InsufficientFundsError) {
                return insufficientFundsHandler.apply((InsufficientFundsError) error);
            } else if (error instanceof InactiveAccountError) {
                return inactiveAccountHandler.apply((InactiveAccountError) error);
            } else if (error instanceof NoSuchAccountError) {
                return noSuchAccountError.apply((NoSuchAccountError) error);
            } else {
                throw new RuntimeException("should never get here! If we got here, " +
                        "maybe a dev forgot to add a handler for a new error type???");
            }
        }
        return successHandlerWithUpdatedAccount.apply(updatedAccount.get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WithdrawalResult that = (WithdrawalResult) o;

        if (updatedAccount != null ? !updatedAccount.equals(that.updatedAccount) : that.updatedAccount != null)
            return false;
        return maybeError != null ? maybeError.equals(that.maybeError) : that.maybeError == null;

    }

    @Override
    public int hashCode() {
        int result = updatedAccount != null ? updatedAccount.hashCode() : 0;
        result = 31 * result + (maybeError != null ? maybeError.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WithdrawalResult{" +
                "updatedAccount=" + updatedAccount +
                ", maybeError=" + maybeError +
                '}';
    }
}
