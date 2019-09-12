package com.adhoc.homework.transactionlogparser;

import java.io.DataInputStream;

public class RecordFactoryImpl implements RecordFactory {

    @Override
    public Record makeRecord(RecordType type, DataInputStream inputStream) {
        Record record;
        switch (type) {
            case DEBIT:
            case CREDIT:
                record = new RecordWithDollarAmount(type, inputStream);
                break;
            case START_AUTOPAY:
            case END_AUTOPAY:
                record = new Record(type, inputStream);
                break;
            default: // shouldn't get here
                record = new Record();
        }
        return record;
    }
}
