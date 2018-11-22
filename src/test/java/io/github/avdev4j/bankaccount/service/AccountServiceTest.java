package io.github.avdev4j.bankaccount.service;

import io.github.avdev4j.bankaccount.BankAccountKataApp;
import io.github.avdev4j.bankaccount.domain.Account;
import io.github.avdev4j.bankaccount.repository.AccountRepository;
import io.github.avdev4j.bankaccount.web.rest.errors.AccountNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.zalando.problem.Status;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankAccountKataApp.class)
public class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    private final long accountId = 1221123L;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        Account account = new Account();
        account.setId(accountId);
        account.setBalance(new BigDecimal("100.0"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.existsById(accountId)).thenReturn(true);
        when(accountRepository.save(any())).then(returnsFirstArg());
    }

    @Test
    public void findAccountByIdShouldReturnAccountByHisId() {
        Account account = accountService.findById(accountId);

        assertThat(account).isNotNull();
        assertThat(account.getId()).isEqualTo(accountId);
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

    @Test
    public void updateAccountShouldReturnTheValueUpdated() {
        Account account = accountService.findById(accountId);
        account.setBalance(BigDecimal.ONE);

        Account accountUpdated = accountService.update(account);

        assertThat(accountUpdated.getBalance()).isEqualTo(account.getBalance());
    }

    @Test
    public void updateAccountWithUnknownIDShouldThrowAccountNotFoundException() {
        Account account = new Account();
        account.setBalance(BigDecimal.ONE);

        try {
            accountService.update(account);
            fail();
        } catch (AccountNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Account not found");
            assertThat(e.getStatus()).isEqualTo(Status.NOT_FOUND);
        }
    }

    @Test
    public void updateAccountWithNullBalanceShouldThrowIllegalArgumentException() {
        Account account = accountRepository.findById(accountId).get();
        account.setBalance(null);

        try {
            accountService.update(account);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("the balance has to be defined");
        }
    }

}
