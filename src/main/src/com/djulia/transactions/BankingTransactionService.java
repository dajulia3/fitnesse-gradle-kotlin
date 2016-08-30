package com.djulia.transactions;

import java.math.BigDecimal;

public interface BankingTransactionService {
    Account withdraw(String accountId, BigDecimal amountToWithdraw);
}
