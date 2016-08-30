package com.djulia.transactions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
public class TransactionsController {
    private final BankingTransactionService transactionService;

    @Autowired
    public TransactionsController(BankingTransactionService transactionService) {
        this.transactionService = Objects.requireNonNull(transactionService);
    }

    @RequestMapping(value = "/accounts/withdrawals")
    public ResponseEntity<?> makeWithdrawal(@RequestBody WithdrawalRequest request ) {
            Account accountAfterWithdrawal = transactionService.withdraw(request.getAccountNumber(), request.getAmount());
            WithdrawalResponse body = new WithdrawalResponse(accountAfterWithdrawal.getBalance());
            return ResponseEntity.status(201).body(body);
    }



    public static class WithdrawalResponse {
        private BigDecimal remainingBalance;

        public WithdrawalResponse(BigDecimal remainingBalance) {
            this.remainingBalance = remainingBalance;
        }

        public BigDecimal getRemainingBalance() {
            return remainingBalance;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WithdrawalResponse that = (WithdrawalResponse) o;

            return remainingBalance != null ? remainingBalance.equals(that.remainingBalance) : that.remainingBalance == null;

        }

        @Override
        public int hashCode() {
            return remainingBalance != null ? remainingBalance.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "WithdrawalResponse{" +
                    "remainingBalance=" + remainingBalance +
                    '}';
        }
    }

}
