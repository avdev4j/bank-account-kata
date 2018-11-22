package io.github.avdev4j.bankaccount.web.rest;

import io.github.avdev4j.bankaccount.BankAccountKataApp;
import io.github.avdev4j.bankaccount.domain.Operation;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        assertThat(operationRepository.findAll()).isEmpty();

        OperationVM operationVM = new OperationVM();
        operationVM.setAccountId(1L);
        operationVM.setAmount(new BigDecimal("100"));

        restOperationMockMvc.perform(post("/api/operations/deposit")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationVM)))
            .andExpect(status().isCreated());

        assertThat(operationRepository.findAll()).size().isEqualTo(1);
    }

    @Test
    @Transactional
    public void addWithdrawOperation() throws Exception {
        assertThat(operationRepository.findAll()).isEmpty();

        OperationVM operationVM = new OperationVM();
        operationVM.setAccountId(1L);
        operationVM.setAmount(new BigDecimal("100"));

        restOperationMockMvc.perform(post("/api/operations/withdraw")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operationVM)))
            .andExpect(status().isCreated());

        assertThat(operationRepository.findAll()).size().isEqualTo(1);
    }

    @Test
    @Transactional
    public void getById() throws Exception {
        Operation operation = new Operation();
        operation.setType(OperationType.DEPOSIT);
        operation.setAmount(new BigDecimal("100"));
        operation.setAccount(accountRepository.getOne(1L));
        operation.setBalanceAfterOperation(new BigDecimal("200"));

        operationRepository.save(operation);

        restOperationMockMvc.perform(get("/api/operations/" + operation.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.amount").value(operation.getAmount()))
            .andExpect(jsonPath("$.type").value(operation.getType().toString()))
            .andExpect(jsonPath("$.balanceAfterOperation").value(operation.getBalanceAfterOperation()));
    }
}
