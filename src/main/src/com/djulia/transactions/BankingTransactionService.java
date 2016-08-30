package com.djulia.transactions;

import java.math.BigDecimal;

public interface BankingTransactionService {
    WithdrawalResult withdraw(String accountId, BigDecimal amountToWithdraw);
}
