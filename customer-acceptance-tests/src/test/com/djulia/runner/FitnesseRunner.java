package com.djulia.runner;

import fitnesse.junit.FitNesseRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(FitNesseRunner.class)
@FitNesseRunner.Suite("BankingTransactionsCalculationSuite")
@FitNesseRunner.FitnesseDir(".")
@FitNesseRunner.OutputDir("./build/fitnesse-results")
class FitnesseRunner {
    @Test
    public void run(){
        System.out.println("running");
    }
}