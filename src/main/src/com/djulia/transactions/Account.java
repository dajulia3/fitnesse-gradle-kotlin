package com.djulia.transactions;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

class Account {
    private final String accountNumber;
    private final BigDecimal balance;
    private final Status status;

    @NotNull
    Account accountDebitedBy(BigDecimal amountToDebit) {
        return new Account(getAccountNumber(), getBalance().subtract(amountToDebit), getStatus());
    }

    public enum Status {
        OPEN,
        CLOSED,
        FROZEN
    }

    Account(String accountNumber, BigDecimal balance, Status status) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
    }

    public boolean balanceSufficientToCoverDebit(BigDecimal amountToWithdraw) {
        return accountDebitedBy(amountToWithdraw).getBalance().compareTo(new BigDecimal(0)) < 0;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Status getStatus() {
        return status;
    }

    public boolean accountIsActive() {
        return getStatus().equals(Status.OPEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (accountNumber != null ? !accountNumber.equals(account.accountNumber) : account.accountNumber != null)
            return false;
        return balance != null ? balance.equals(account.balance) : account.balance == null;

    }

    @Override
    public int hashCode() {
        int result = accountNumber != null ? accountNumber.hashCode() : 0;
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                '}';
    }

}
