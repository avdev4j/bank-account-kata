package io.github.avdev4j.bankaccount.web.rest;

import io.github.avdev4j.bankaccount.BankAccountKataApp;
import io.github.avdev4j.bankaccount.enumeration.OperationType;
import io.github.avdev4j.bankaccount.repository.AccountRepository;
import io.github.avdev4j.bankaccount.repository.OperationRepository;
import io.github.avdev4j.bankaccount.service.OperationService;
import io.github.avdev4j.bankaccount.web.rest.errors.ExceptionTranslator;
import io.github.avdev4j.bankaccount.web.rest.vm.OperationVM;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankAccountKataApp.class)
public class OperationResourceIntTest {

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private OperationService operationService;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private AccountRepository accountRepository;

    private MockMvc restOperationMockMvc;

    @Before
    public void setup() {
        OperationResource operationResource = new OperationResource(operationService);

        this.restOperationMockMvc = MockMvcBuilders.standaloneSetup(operationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter)
            .build();
    }

    @Test
    @Transactional
    public void addDepositOperation() throws Exception {
        OperationVM operationVM = new OperationVM();
        operationVM.setAccountId(1L);
        operationVM.setAmount(new BigDecimal("100"));
        operationVM.setType(OperationType.DEPOSIT);

        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationVM)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.amount").value(operationVM.getAmount()))
            .andExpect(jsonPath("$.type").value(OperationType.DEPOSIT.toString()));
    }

    @Test
    @Transactional
    public void addWithdrawOperation() throws Exception {
        OperationVM operationVM = new OperationVM();
        operationVM.setAccountId(1L);
        operationVM.setAmount(new BigDecimal("100"));
        operationVM.setType(OperationType.WITHDRAWAL);

        restOperationMockMvc.perform(post("/api/operations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationVM)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.amount").value(operationVM.getAmount()))
            .andExpect(jsonPath("$.type").value(OperationType.WITHDRAWAL.toString()));
    }

}
