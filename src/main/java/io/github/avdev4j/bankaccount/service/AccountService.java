package io.github.avdev4j.bankaccount.service;

import io.github.avdev4j.bankaccount.domain.Account;
import io.github.avdev4j.bankaccount.repository.AccountRepository;
import io.github.avdev4j.bankaccount.web.rest.errors.AccountNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account deposit(Long id, BigDecimal amount) {
        Account account = findById(id);
        account.setBalance(account.getBalance().add(amount));

        return update(account);
    }

    public Account withdraw(Long id, BigDecimal amount) {
        Account account = findById(id);
        account.setBalance(account.getBalance().subtract(amount));

        return update(account);
    }

    public Account findById(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(AccountNotFoundException::new);
    }

    protected Account update(Account account) {
        Assert.notNull(account.getBalance(), "the balance has to be defined");

        if (!accountRepository.existsById(account.getId())) {
            throw new AccountNotFoundException();
        }

        return accountRepository.save(account);
    }

    public List<Account> findAllByUserId(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }
}
