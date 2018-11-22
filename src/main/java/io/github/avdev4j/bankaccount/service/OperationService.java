package io.github.avdev4j.bankaccount.service;

import io.github.avdev4j.bankaccount.domain.Account;
import io.github.avdev4j.bankaccount.domain.Operation;
import io.github.avdev4j.bankaccount.enumeration.OperationType;
import io.github.avdev4j.bankaccount.repository.OperationRepository;
import io.github.avdev4j.bankaccount.web.rest.errors.OperationNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OperationService implements DepositOperation, WithdrawalOperation {

    private final OperationRepository operationRepository;
    private final AccountService accountService;

    public OperationService(OperationRepository operationRepository, AccountService accountService) {
        this.operationRepository = operationRepository;
        this.accountService = accountService;
    }

    public Operation findById(Long id) {
        return operationRepository.findById(id)
            .orElseThrow(OperationNotFoundException::new);
    }


    public Operation registerDeposit(Long accountId, BigDecimal amount) {
        Account account = accountService.deposit(accountId, amount);

        Operation operation = generateDepositOperation(account, amount);

        return operationRepository.save(operation);
    }

    public Operation registerWithdrawal(Long accountId, BigDecimal amount) {
        Account account = accountService.withdraw(accountId, amount);

        Operation operation = generateWithdrawalOperation(account, amount);

        return operationRepository.save(operation);
    }

    private Operation generateDepositOperation(Account account, BigDecimal amount) {
        return generateOperation(account, amount, OperationType.DEPOSIT);
    }

    private Operation generateWithdrawalOperation(Account account, BigDecimal amount) {
        return generateOperation(account, amount, OperationType.WITHDRAWAL);
    }

    private Operation generateOperation(Account account, BigDecimal amount, OperationType type) {
        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setAmount(amount);
        operation.setType(type);
        operation.setBalanceAfterOperation(account.getBalance());

        return operation;
    }
}
