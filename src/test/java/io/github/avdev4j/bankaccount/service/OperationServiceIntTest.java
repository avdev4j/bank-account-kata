package io.github.avdev4j.bankaccount.service;

import io.github.avdev4j.bankaccount.BankAccountKataApp;
import io.github.avdev4j.bankaccount.domain.Account;
import io.github.avdev4j.bankaccount.repository.AccountRepository;
import io.github.avdev4j.bankaccount.repository.OperationRepository;
import io.github.avdev4j.bankaccount.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankAccountKataApp.class)
public class OperationServiceIntTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private OperationService operationService;

    private Account account;

    @Before
    public void init() {
        account = new Account();
        account.setBalance(new BigDecimal("10.00"));
        account.setUser(userRepository.getOne(1L));

        accountRepository.save(account);
    }

    @Test
    public void makeDepositShouldAddOperationAndUpdateAccount() {
        operationService.registerDeposit(account.getId(), new BigDecimal("250.00"));
        Account accountUpdated = accountRepository.findById(account.getId()).get();

        Assertions.assertThat(accountUpdated.getBalance()).isEqualTo(new BigDecimal("260.00"));
    }

    @Test
    public void makeWithdrawalShouldAddOperationAndUpdateAccount() {
        operationService.registerWithdrawal(account.getId(), new BigDecimal("250.00"));
        Account accountUpdated = accountRepository.findById(account.getId()).get();

        Assertions.assertThat(accountUpdated.getBalance()).isEqualTo(new BigDecimal("-240.00"));
    }

}
