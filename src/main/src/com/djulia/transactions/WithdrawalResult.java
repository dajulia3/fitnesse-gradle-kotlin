package com.djulia.transactions;

import java.util.Optional;

public class WithdrawalResult {
    private final Optional<Account> updatedAccount;
    private final Optional<Error> error;

    public enum Error {
        INVALID_WITHDRAWAL_AMOUNT,
        INSUFFICIENT_FUNDS,
        INACTIVE_ACCOUNT,
        NO_SUCH_ACCOUNT
    }

    private WithdrawalResult(Error error) {
        this.error = Optional.of(error);
        this.updatedAccount = Optional.empty();
    }

    private WithdrawalResult(Account updatedAccount) {
        this.updatedAccount = Optional.of(updatedAccount);
        this.error = Optional.empty();

    }

    public static WithdrawalResult error(Error error) {
        return new WithdrawalResult(error);
    }

    public static WithdrawalResult success(Account updatedAccount) {
        return new WithdrawalResult(updatedAccount);
    }

    public boolean isSuccessful(){
        return !error.isPresent();
    }

    public Optional<Account> getUpdatedAccount() {
        return updatedAccount;
    }

    public Optional<Error> getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WithdrawalResult that = (WithdrawalResult) o;

        if (updatedAccount != null ? !updatedAccount.equals(that.updatedAccount) : that.updatedAccount != null)
            return false;
        return error != null ? error.equals(that.error) : that.error == null;

    }

    @Override
    public int hashCode() {
        int result = updatedAccount != null ? updatedAccount.hashCode() : 0;
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WithdrawalResult{" +
                "updatedAccount=" + updatedAccount +
                ", error=" + error +
                '}';
    }
}
