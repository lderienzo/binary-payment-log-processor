package com.adhoc.homework.transactionlogparser.transactionlog;

public class LogParsingException extends RuntimeException {

    public LogParsingException(String message, Exception e) {
        super(message, e);
    }

    public LogParsingException(String message) {
        super(message);
    }

    public LogParsingException(Exception e) {
        super(e);
    }
}
