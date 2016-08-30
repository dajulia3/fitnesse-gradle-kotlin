package com.djulia.transactions;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class InMemoryAccountRepoTest {

    InMemoryAccountRepo repo = new InMemoryAccountRepo();

    @Test
    public void findById_whenNoAccountRecordFound(){
        assertThat(repo.findById("12345ABC")).isEqualTo(Optional.empty());
    }

    @Test
    public void findById_returnsTheAccount_whenRecordFound(){
        Account account = new Account("12345ABC", new BigDecimal(500), Account.Status.OPEN);
        repo.save(account);

        Optional<Account> result = repo.findById("12345ABC");

        assertThat(result).isEqualTo(Optional.of(account));
    }


}
