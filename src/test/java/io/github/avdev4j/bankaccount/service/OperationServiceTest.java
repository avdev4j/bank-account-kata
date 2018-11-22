package io.github.avdev4j.bankaccount.service;

import io.github.avdev4j.bankaccount.BankAccountKataApp;
import io.github.avdev4j.bankaccount.repository.OperationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankAccountKataApp.class)
public class OperationServiceTest {

    @MockBean
    private OperationRepository operationRepository;

    @Autowired
    private OperationService operationService;

    @Before
    public void init() {

    }

    @Test
    public void makeDepositShouldAddOperationAndUpdateAccount() {

    }

}
