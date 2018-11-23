package io.github.avdev4j.bankaccount.service;

import io.github.avdev4j.bankaccount.BankAccountKataApp;
import io.github.avdev4j.bankaccount.domain.Account;
import io.github.avdev4j.bankaccount.domain.Operation;
import io.github.avdev4j.bankaccount.enumeration.OperationType;
import io.github.avdev4j.bankaccount.repository.AccountRepository;
import io.github.avdev4j.bankaccount.repository.OperationRepository;
import io.github.avdev4j.bankaccount.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankAccountKataApp.class)
public class OperationServiceIntTest {

    @Autowired
    private UserRepository userRepository;

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

        assertThat(accountUpdated.getBalance()).isEqualTo(new BigDecimal("260.00"));
    }

    @Test
    public void makeWithdrawalShouldAddOperationAndUpdateAccount() {
        operationService.registerWithdrawal(account.getId(), new BigDecimal("250.00"));
        Account accountUpdated = accountRepository.findById(account.getId()).get();

        assertThat(accountUpdated.getBalance()).isEqualTo(new BigDecimal("-240.00"));
    }

    @Test
    public void findAllByAccountShouldReturnAllOperationForThisAccount() {
        Operation operation1 = new Operation();
        operation1.setAccount(account);
        operation1.setAmount(BigDecimal.ONE);
        operation1.setType(OperationType.DEPOSIT);
        operation1.setBalanceAfterOperation(new BigDecimal("11.00"));

        operationRepository.save(operation1);

        Operation operation2 = new Operation();
        operation2.setAccount(account);
        operation2.setAmount(BigDecimal.ONE);
        operation2.setType(OperationType.WITHDRAWAL);
        operation2.setBalanceAfterOperation(new BigDecimal("11.00"));

        operationRepository.save(operation2);

        List<Operation> operations = operationService.findAllByAccount(account);

        assertThat(operations).isNotEmpty();
        assertThat(operations).size().isEqualTo(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findAllByAccountWithNullShouldThrowIllegalArgumentException() {
        operationService.findAllByAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findAllByAccountWithAccountWithoutIdShouldThrowIllegalArgumentException() {
        operationService.findAllByAccount(new Account());
    }
}
