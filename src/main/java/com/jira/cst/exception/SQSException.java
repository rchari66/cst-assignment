package com.jira.cst.exception;

/**
 * Exception for SQS related operations
 */
public class SQSException extends Exception {
    public SQSException(String msg) {
        super(msg);
    }
}
