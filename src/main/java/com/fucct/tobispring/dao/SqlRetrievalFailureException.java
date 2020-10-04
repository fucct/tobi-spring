package com.fucct.tobispring.dao;

public class SqlRetrievalFailureException extends RuntimeException {
    public SqlRetrievalFailureException(final String message) {
        super(message);
    }

    public SqlRetrievalFailureException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
