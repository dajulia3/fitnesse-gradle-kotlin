package com.djulia.banking.transactions

import org.springframework.context.annotation.Bean

class TransactionsApiConfiguration {
    @Bean
    fun bankingTransactionService(): BankingTransactionService {
        return InMemoryOnlyBankingTransactionService(InMemoryAccountRepo())
    }
}
