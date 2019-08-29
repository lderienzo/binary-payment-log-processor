package com.adhoc.homework.transactionlogparser;

public class BinaryFileParsingException extends RuntimeException {

    public BinaryFileParsingException(String message, Exception e) {
        super(message, e);
    }

    public BinaryFileParsingException(String message) {
        super(message);
    }

    public BinaryFileParsingException(Exception e) {
        super(e);
    }
}
