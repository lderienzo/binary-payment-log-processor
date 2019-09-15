package com.adhoc.homework.transactionlogparser.arguments;

public class ArgException extends RuntimeException {

    public ArgException(Exception e) {
        super(e);
    }

    public ArgException(String message) {
            super(message);
    }

    public ArgException(String message, Exception e) {
        super(message, e);
    }
}
