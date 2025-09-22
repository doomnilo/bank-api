package com.tlchallenge.bankapi.exception;

public class TransferRejectedException extends RuntimeException {

    private final Long fromAccountId;
    private final Long toAccountId;
    private final String reason;

    public TransferRejectedException(Long fromAccountId, Long toAccountId, String reason) {
        super(String.format("Transfer from account %d to account %d was rejected: %s",
                fromAccountId, toAccountId, reason));
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.reason = reason;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public String getReason() {
        return reason;
    }
}
