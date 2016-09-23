package com.djulia.acceptancetests.fixtures;

import java.math.BigDecimal;

public class SimpleInterest {

    private  BigDecimal initialBalance;
    private BigDecimal apr;
    private int timePeriod;

    public void setInitialBalance(int initialBalance){
        this.initialBalance = new BigDecimal(initialBalance);
    }

    public BigDecimal earnedInterest(){
        return BigDecimal.TEN;
    }

    public BigDecimal newAccountBalance(){
        return BigDecimal.ONE;
    }

    public void setAPR(String APR) {
    }

    public void setTimePeriodMonths(String TimePeriodMonths) {
    }

    public void setEarnedInterest(String EarnedInterest) {
    }

    public void setNewAccountBalance(String NewAccountBalance) {
    }
}
