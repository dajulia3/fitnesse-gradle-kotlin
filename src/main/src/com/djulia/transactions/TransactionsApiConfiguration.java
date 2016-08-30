package com.djulia.transactions;

import org.springframework.context.annotation.Bean;

public class TransactionsApiConfiguration {
    @Bean
    public BankingTransactionService bankingTransactionService(){
        return new InMemoryOnlyBankingTransactionService(new InMemoryAccountRepo());
    }
}
