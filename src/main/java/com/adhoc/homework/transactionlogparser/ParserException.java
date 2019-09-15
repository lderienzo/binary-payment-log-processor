package com.adhoc.homework.transactionlogparser;

public class ParserException extends RuntimeException {
    public ParserException(String message, Exception e) {
        super(message, e);
    }
}
