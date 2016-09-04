package com.djulia.transactions

import org.junit.Test

import java.math.BigDecimal
import java.util.Optional

import org.assertj.core.api.Java6Assertions.assertThat

class InMemoryAccountRepoTest {

    internal var repo = InMemoryAccountRepo()

    @Test
    fun findById_whenNoAccountRecordFound() {
        assertThat(repo.findById("12345ABC")).isEqualTo(null)
    }

    @Test
    fun findById_returnsTheAccount_whenRecordFound() {
        val account = Account("12345ABC", BigDecimal(500), Account.Status.OPEN)
        repo.save(account)

        val result = repo.findById("12345ABC")

        assertThat(result).isEqualTo(account)
    }


}
