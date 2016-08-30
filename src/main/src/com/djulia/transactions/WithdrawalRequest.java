package com.djulia.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class WithdrawalRequest {
    @JsonProperty
    private String accountNumber;
    @JsonProperty
    private BigDecimal amount;

    protected WithdrawalRequest(){}
    public WithdrawalRequest(@JsonProperty("accountNumber") String accountNumber,
                             @JsonProperty("amount") BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WithdrawalRequest that = (WithdrawalRequest) o;

        if (accountNumber != null ? !accountNumber.equals(that.accountNumber) : that.accountNumber != null)
            return false;
        return amount != null ? amount.equals(that.amount) : that.amount == null;

    }

    @Override
    public int hashCode() {
        int result = accountNumber != null ? accountNumber.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WithdrawalRequest{" +
                "accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                '}';
    }
}
