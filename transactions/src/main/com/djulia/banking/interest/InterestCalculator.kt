package com.djulia.banking.interest

import java.math.BigDecimal

interface InterestCalculator {
    fun calculateInterest(amount: BigDecimal, timePeriod: java.time.Period)
}