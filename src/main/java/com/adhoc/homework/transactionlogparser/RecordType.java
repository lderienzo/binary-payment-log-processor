package com.adhoc.homework.transactionlogparser;

public enum RecordType {
    DEBIT(0x00), CREDIT(0x01), START_AUTOPAY(0x02), END_AUTOPAY(0x03);

    int type;

    RecordType(int type) {
        this.type = type;
    }

    public int get() {
        return type;
    }
}
