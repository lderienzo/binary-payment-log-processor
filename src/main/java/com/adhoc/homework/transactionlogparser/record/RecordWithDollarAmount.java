package com.adhoc.homework.transactionlogparser.record;

import java.io.DataInputStream;
import java.nio.ByteBuffer;

public final class RecordWithDollarAmount extends Record {

    private final double dollarAmount;

    public RecordWithDollarAmount(RecordType type, DataInputStream binaryStream) {
        super(type, binaryStream);
        this.dollarAmount = readDollarAmount(binaryStream);
    }

    private double readDollarAmount(DataInputStream binaryStream) {
        byte[] dollarAmountByteArray = readEightBytesFromBinaryStream(binaryStream);
        ByteBuffer buffer = ByteBuffer.wrap(dollarAmountByteArray);
        return buffer.getDouble();
    }

    public double getDollarAmount() {
        return dollarAmount;
    }

    @Override
    public String toString() {
        return super.toString() + " Dollar Amount:[" + dollarAmount + "]";
    }
}
