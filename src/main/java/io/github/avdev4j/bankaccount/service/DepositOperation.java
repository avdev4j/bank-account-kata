package io.github.avdev4j.bankaccount.service;

import io.github.avdev4j.bankaccount.domain.Operation;

import java.math.BigDecimal;

public interface DepositOperation {

    Operation registerDeposit(Long accountId, BigDecimal amount);
}
