package io.github.avdev4j.bankaccount.service;

import io.github.avdev4j.bankaccount.domain.Operation;

import java.math.BigDecimal;

public interface WithdrawalOperation {

    Operation registerWithdrawal(Long accountId, BigDecimal amount);
}
