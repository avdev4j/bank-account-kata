package io.github.avdev4j.bankaccount.service;

import io.github.avdev4j.bankaccount.BankAccountKataApp;
import io.github.avdev4j.bankaccount.domain.Account;
import io.github.avdev4j.bankaccount.domain.User;
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
import java.util.ArrayList;
import java.util.List;
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

    private final long accountId1 = 1221123L;
    private final long accountId2 = 1221124L;
    private User user;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setId(Long.MAX_VALUE);

        Account account1 = new Account();
        account1.setId(accountId1);
        account1.setBalance(new BigDecimal("100.00"));
        account1.setUser(user);

        Account account2 = new Account();
        account2.setId(accountId2);
        account2.setBalance(new BigDecimal("-100.00"));
        account2.setUser(user);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);

        when(accountRepository.findById(accountId1)).thenReturn(Optional.of(account1));
        when(accountRepository.existsById(accountId1)).thenReturn(true);
        when(accountRepository.save(any())).then(returnsFirstArg());
        when(accountRepository.findAllByUserId(user.getId())).thenReturn(accounts);
    }

    @Test
    public void findAccountByIdShouldReturnAccountByHisId() {
        Account account = accountService.findById(accountId1);

        assertThat(account).isNotNull();
        assertThat(account.getId()).isEqualTo(accountId1);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("100.00"));
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
    public void makeADepositOnAAccountShouldReturnTheAccountUpdated() {
        Account accountUpdated = accountService.deposit(accountId1, BigDecimal.ONE);

        assertThat(accountUpdated.getBalance()).isEqualTo(new BigDecimal("101.00"));
    }

    @Test
    public void updateAccountShouldReturnTheValueUpdated() {
        Account account = accountService.findById(accountId1);
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
        Account account = accountRepository.findById(accountId1).get();
        account.setBalance(null);

        try {
            accountService.update(account);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("the balance has to be defined");
        }
    }

    @Test
    public void getAllAccountsByUserShouldReturnACollectionOfAccount() {
        List<Account> accounts = accountService.findAllByUserId(user.getId());

        assertThat(accounts).isNotEmpty();
        assertThat(accounts.get(0).getId()).isEqualTo(accountId1);
        assertThat(accounts.get(0).getBalance()).isEqualTo(new BigDecimal("100.00"));
        assertThat(accounts.get(1).getId()).isEqualTo(accountId2);
        assertThat(accounts.get(1).getBalance()).isEqualTo(new BigDecimal("-100.00"));
    }

}
