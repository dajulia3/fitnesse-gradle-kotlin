package com.djulia.banking.transactions.web

import com.djulia.banking.transactions.domain.BankingTransactionService
import com.djulia.banking.transactions.domain.WithdrawalResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
internal class TransactionsController {
    private val transactionService: BankingTransactionService

    @Autowired
    constructor(transactionService: BankingTransactionService) {
        this.transactionService = transactionService
    }

    @org.springframework.web.bind.annotation.RequestMapping(value = "/accounts/withdrawals")
    fun makeWithdrawal(@org.springframework.web.bind.annotation.RequestBody request: WithdrawalRequest): org.springframework.http.ResponseEntity<*> {
        val result = transactionService.withdraw(request.accountNumber, request.amount)

        return when (result.maybeError) {
            is WithdrawalResult.Error.InactiveAccountError -> org.springframework.http.ResponseEntity.status(400).body(ErrorResponse("Inactive Account. Can't withdraw"))
            is WithdrawalResult.Error.InvalidWithdrawalAmountError -> org.springframework.http.ResponseEntity.status(400).body(ErrorResponse("Invalid withdrawal"))
            is WithdrawalResult.Error.InsufficientFundsError -> org.springframework.http.ResponseEntity
                    .status(400)
                    .body(ErrorResponse(
                            "Insufficient funds. Need $" + result.maybeError.differenceInFunds + " to complete transaction."))

            is WithdrawalResult.Error.NoSuchAccountError -> org.springframework.http.ResponseEntity.status(400).body(ErrorResponse("No account found with the id " + request.accountNumber))
            null -> {
                val body = WithdrawalResponse(result.updatedAccount?.balance)
                org.springframework.http.ResponseEntity.status(201).body(body)
            }
        }
        //DEFINITELY caught them all, and had to be exhaustive!!!
        // BLISS!!!
    }


    internal data class WithdrawalResponse(val remainingBalance: BigDecimal?)
    internal data class ErrorResponse(val errorMessage: String)
    internal data class WithdrawalRequest(val accountNumber: String, val amount: BigDecimal)
}

