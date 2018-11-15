package io.github.avdev4j.bankaccount.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class AccountNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public AccountNotFoundException() {
        super(ErrorConstants.ACCOUNT_NOT_FOUND_TYPE, "Account not found", Status.NOT_FOUND);
    }
}
