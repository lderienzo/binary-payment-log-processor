package com.adhoc.homework.transactionlogparser;

public class ParserException extends RuntimeException {

    public ParserException(Exception e) {
        super(e);
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Exception e) {
        super(message, e);
    }
}
