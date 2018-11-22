package io.github.avdev4j.bankaccount.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class OperationNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public OperationNotFoundException() {
        super(null, "Operation not found", Status.NOT_FOUND);
    }
}
