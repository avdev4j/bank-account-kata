package io.github.avdev4j.bankaccount.service;

import io.github.avdev4j.bankaccount.domain.Account;
import io.github.avdev4j.bankaccount.domain.Operation;
import io.github.avdev4j.bankaccount.enumeration.OperationType;
import io.github.avdev4j.bankaccount.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OperationService {

    private final OperationRepository operationRepository;
    private final AccountService accountService;

    public OperationService(OperationRepository operationRepository, AccountService accountService) {
        this.operationRepository = operationRepository;
        this.accountService = accountService;
    }

    public Operation registerDeposit(Long accountId, BigDecimal amount) {
        Account account = accountService.deposit(accountId, amount);

        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setAmount(amount);
        operation.setType(OperationType.DEPOSIT);
        operation.setBalanceAfterOperation(account.getBalance());

        return operationRepository.save(operation);
    }
}
