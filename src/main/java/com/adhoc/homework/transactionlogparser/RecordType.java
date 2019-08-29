package com.adhoc.homework.transactionlogparser;

public enum RecordType {
    DEBIT(0x00), CREDIT(0x01), START_AUTOPAY(0x02), END_AUTOPAY(0x03);

    int typeHex;

    RecordType(int typeHex) {
        this.typeHex = typeHex;
    }

    public int get() {
        return typeHex;
    }
}
