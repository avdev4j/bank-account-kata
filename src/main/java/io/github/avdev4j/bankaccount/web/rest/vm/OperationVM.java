package io.github.avdev4j.bankaccount.web.rest.vm;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OperationVM {

    @NotNull
    private Long accountId;

    @NotNull
    private BigDecimal amount;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
