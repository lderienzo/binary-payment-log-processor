package com.adhoc.homework.transactionlogparser.record;

import java.io.DataInputStream;

public class RecordFactoryImpl implements RecordFactory {

    @Override
    public Record makeRecord(RecordType type, DataInputStream inputStream) {
        switch (type) {
            case DEBIT:
            case CREDIT:
                return new RecordWithDollarAmount(type, inputStream);
            case START_AUTOPAY:
            case END_AUTOPAY:
                return new Record(type, inputStream);
            default: // shouldn't get here
                return new Record();
        }
    }
}
