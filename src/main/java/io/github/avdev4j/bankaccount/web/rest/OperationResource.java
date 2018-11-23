package io.github.avdev4j.bankaccount.web.rest;

import io.github.avdev4j.bankaccount.domain.Operation;
import io.github.avdev4j.bankaccount.service.OperationService;
import io.github.avdev4j.bankaccount.web.rest.vm.OperationVM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class OperationResource {

    private final OperationService operationService;

    public OperationResource(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping("/operations/deposit")
    public ResponseEntity<Operation> deposit(@RequestBody OperationVM operationVM) throws URISyntaxException {
        Operation operation = operationService.registerDeposit(operationVM.getAccountId(), operationVM.getAmount());

        return ResponseEntity.created(new URI("/api/operations/" + operation.getId())).body(operation);
    }

    @PostMapping("/operations/withdraw")
    public ResponseEntity<Operation> withdraw(@RequestBody OperationVM operationVM) throws URISyntaxException {
        Operation operation = operationService.registerWithdrawal(operationVM.getAccountId(), operationVM.getAmount());

        return ResponseEntity.created(new URI("/api/operations/" + operation.getId())).body(operation);
    }

}
