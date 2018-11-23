package io.github.avdev4j.bankaccount.web.rest;

import io.github.avdev4j.bankaccount.domain.Account;
import io.github.avdev4j.bankaccount.domain.Operation;
import io.github.avdev4j.bankaccount.service.AccountService;
import io.github.avdev4j.bankaccount.service.OperationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BankAccountResource {

    private final AccountService accountService;
    private final OperationService operationService;

    public BankAccountResource(AccountService accountService, OperationService operationService) {
        this.accountService = accountService;
        this.operationService = operationService;
    }

    @GetMapping("/bankaccounts/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        return new ResponseEntity<>(accountService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/bankaccounts")
    public ResponseEntity<List<Account>> getAllAccountsByUserId(@RequestParam Long userId) {
        return new ResponseEntity<>(accountService.findAllByUserId(userId), HttpStatus.OK);
    }


    @GetMapping("/bankaccounts/{id}/operations")
    public ResponseEntity<List<Operation>> getAllOperationByAccount(@PathVariable Long id) {
        Account account = accountService.findById(id);
        return ResponseEntity.ok(operationService.findAllByAccount(account));
    }
}
