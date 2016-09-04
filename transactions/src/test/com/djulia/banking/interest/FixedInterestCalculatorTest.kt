package com.djulia.banking.interest

import org.junit.Test
import java.math.BigDecimal
import java.time.Period

class FixedInterestCalculatorTest {
    val calculator = FixedInterestCalculator()

    @Test
    fun calculateInterest() {
        calculator.calculateInterest(BigDecimal(1000), Period.ofDays(15))
    }

}