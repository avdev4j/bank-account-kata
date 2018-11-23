package io.github.avdev4j.bankaccount.web.rest;

import io.github.avdev4j.bankaccount.domain.Operation;
import io.github.avdev4j.bankaccount.enumeration.OperationType;
import io.github.avdev4j.bankaccount.service.OperationService;
import io.github.avdev4j.bankaccount.web.rest.vm.OperationVM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class OperationResource {

    private final OperationService operationService;

    public OperationResource(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping("/operations")
    public ResponseEntity<Operation> create(@RequestBody @Valid OperationVM operationVM) throws URISyntaxException {
        Operation operation;

        if (isDepositOperation(operationVM)) {
            operation = operationService.registerDeposit(operationVM.getAccountId(), operationVM.getAmount());
        } else {
            operation = operationService.registerWithdrawal(operationVM.getAccountId(), operationVM.getAmount());
        }

        return ResponseEntity.created(new URI("/api/operations/" + operation.getId())).body(operation);
    }

    private boolean isDepositOperation(OperationVM operationVM) {
        return OperationType.DEPOSIT.equals(operationVM.getType());
    }

}
