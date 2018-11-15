package io.github.avdev4j.bankaccount.service;

import io.github.avdev4j.bankaccount.BankAccountKataApp;
import io.github.avdev4j.bankaccount.domain.Account;
import io.github.avdev4j.bankaccount.repository.AccountRepository;
import io.github.avdev4j.bankaccount.web.rest.errors.AccountNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Status;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankAccountKataApp.class)
@Transactional
public class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Before
    public void init() {
        Account account = new Account();
        account.setId(1221123L);
        account.setBalance(new BigDecimal("100.0"));

        when(accountRepository.getOne(1221123L)).thenReturn(account);
    }

    @Test
    public void findAccountByIdShouldReturnAccountByHisId() {
        Account account = accountService.findById(1221123L);

        assertThat(account).isNotNull();
        assertThat(account.getId()).isEqualTo(1221123L);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("100.0"));
    }

    @Test
    public void findAccountByAWrongIdShouldThrow() {
        try {
            accountService.findById(Long.MAX_VALUE);
            fail("Find account by id with an unknown id should fail");
        } catch (AccountNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Account not found");
            assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND);
        }
    }

}
