package com.djulia.transactions

import org.springframework.beans.factory.annotation.Autowired
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

    @RequestMapping(value = "/accounts/withdrawals")
    fun makeWithdrawal(@RequestBody request: WithdrawalRequest): ResponseEntity<*> {
        val result = transactionService.withdraw(request.accountNumber, request.amount)

        return when (result.maybeError) {
            is WithdrawalResult.Error.InactiveAccountError -> ResponseEntity.status(400).body(TransactionsController.ErrorResponse("Inactive Account. Can't withdraw"))
            is WithdrawalResult.Error.InvalidWithdrawalAmountError -> ResponseEntity.status(400).body(TransactionsController.ErrorResponse("Invalid withdrawal"))
            is WithdrawalResult.Error.InsufficientFundsError -> ResponseEntity.status(400).body(TransactionsController.ErrorResponse(
                    "Insufficient funds. Need $" + result.maybeError.differenceInFunds + " to complete transaction."))

            is WithdrawalResult.Error.NoSuchAccountError -> ResponseEntity.status(400).body(TransactionsController.ErrorResponse("No account found with the id " + request.accountNumber))
            null -> {
                val body = WithdrawalResponse(result.updatedAccount?.balance)
                ResponseEntity.status(201).body(body)
            }
        }
        //DEFINITELY caught them all, and had to be exhaustive!!!
        // BLISS!!!
    }


    internal data class WithdrawalResponse(val remainingBalance: BigDecimal?)
    internal data class ErrorResponse(val errorMessage: String)
    internal data class WithdrawalRequest(val accountNumber: String, val amount: BigDecimal)
}

