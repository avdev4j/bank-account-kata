package io.github.avdev4j.bankaccount.service;

import io.github.avdev4j.bankaccount.BankAccountKataApp;
import io.github.avdev4j.bankaccount.domain.Account;
import io.github.avdev4j.bankaccount.domain.Operation;
import io.github.avdev4j.bankaccount.enumeration.OperationType;
import io.github.avdev4j.bankaccount.repository.OperationRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankAccountKataApp.class)
public class OperationServiceTest {


    @MockBean
    private AccountService accountService;

    @MockBean
    private OperationRepository operationRepository;

    @Autowired
    private OperationService operationService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("100.00"));

        when(accountService.findById(account.getId())).thenReturn(account);
        when(accountService.update(account)).then(returnsFirstArg());
        when(accountService.deposit(anyLong(), any())).thenReturn(account);
        when(accountService.withdrawal(anyLong(), any())).thenReturn(account);
        when(operationRepository.save(any())).then(returnsFirstArg());
    }

    @Test
    public void makeDepositShouldCreateADepositOperation() {
        Operation operation = operationService.registerDeposit(1L, new BigDecimal("250.00"));

        Assertions.assertThat(operation).isNotNull();
        Assertions.assertThat(operation.getAmount()).isEqualTo(new BigDecimal("250.00"));
        Assertions.assertThat(operation.getType()).isEqualTo(OperationType.DEPOSIT);
        Assertions.assertThat(operation.getBalanceAfterOperation()).isEqualTo(new BigDecimal("100.00"));
    }

    @Test
    public void makeWithdrawalShouldCreateAWithdrawalOperation() {
        Operation operation = operationService.registerWithdrawal(1L, new BigDecimal("250.00"));

        Assertions.assertThat(operation).isNotNull();
        Assertions.assertThat(operation.getAmount()).isEqualTo(new BigDecimal("250.00"));
        Assertions.assertThat(operation.getType()).isEqualTo(OperationType.WITHDRAWAL);
        Assertions.assertThat(operation.getBalanceAfterOperation()).isEqualTo(new BigDecimal("100.00"));
    }

}
