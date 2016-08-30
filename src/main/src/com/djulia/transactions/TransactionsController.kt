package com.djulia.transactions

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.math.BigDecimal
import java.util.Objects

@RestController
class TransactionsController
@Autowired
constructor(transactionService: BankingTransactionService) {
    private val transactionService: BankingTransactionService

    init {
        this.transactionService = Objects.requireNonNull(transactionService)
    }

    @RequestMapping(value = "/accounts/withdrawals")
    fun makeWithdrawal(@RequestBody request: WithdrawalRequest): ResponseEntity<*> {
        val result = transactionService.withdraw(request.accountNumber, request.amount)

        val errorResponse = result.match(
                { successfullyUpdatedAccount ->
                    val body = WithdrawalResponse(successfullyUpdatedAccount.balance)
                    ResponseEntity.status(201).body(body)
                },
                { inactive -> ResponseEntity.status(400).body(TransactionsController.ErrorResponse("Inactive Account. Can't withdraw")) },
                { invalid -> ResponseEntity.status(400).body(TransactionsController.ErrorResponse("Invalid withdrawal")) },
                { insufficient ->
                    ResponseEntity.status(400).body(TransactionsController.ErrorResponse(
                            "Insufficient funds. Need $" + insufficient.differenceInFunds + " to complete transaction."))
                }
        ) { noSuchAccount -> ResponseEntity.status(400).body(TransactionsController.ErrorResponse("No account found with the id " + request.accountNumber)) }

        return errorResponse
        //DEFINITELY caught them all, as long as match is kept up to date (more likely since all changes to error types
        // are local to that file)!!!
        //much better, but does it buy us anything more??
    }


    data class WithdrawalResponse(val remainingBalance: BigDecimal?) {
    }

    data class ErrorResponse(val errorMessage: String) {
    }
}
