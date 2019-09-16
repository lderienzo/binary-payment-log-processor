package com.adhoc.homework.transactionlogparser.record;

import static com.adhoc.homework.transactionlogparser.utils.FileUtils.readFromBinaryStreamIntoByteArray;

import java.io.DataInputStream;

import org.apache.commons.codec.binary.Hex;


public class Record {
    private RecordType type;
    private String userId;
    private long timestamp;

    public Record(){}

    public Record(RecordType type, DataInputStream binaryStream) {
        this.type = type;
        this.timestamp = readUnixTimestamp(binaryStream);
        this.userId = readUserId(binaryStream);
    }

    private long readUnixTimestamp(DataInputStream binaryStream) {
        byte[] timeStampByteArray = readFourBytesFromBinaryStream(binaryStream);
        char[] recordTypeHexChars = encodeBytesToHexChars(timeStampByteArray);
        return encodeHexCharsToLong(recordTypeHexChars);
    }

    private byte[] readFourBytesFromBinaryStream(DataInputStream binaryStream) {
        byte[] fourByteArray = new byte[4];
        readFromBinaryStreamIntoByteArray(binaryStream, fourByteArray);
        return fourByteArray;
    }

    private char[] encodeBytesToHexChars(byte[] byteArrayToEncode) {
        return Hex.encodeHex(byteArrayToEncode);
    }

    private long encodeHexCharsToLong(char[] hexCharsToEncode) {
        return Long.parseLong(new String(hexCharsToEncode), 16);
    }

    private String readUserId(DataInputStream binaryStream) {
        byte[] userIdByteArray = readEightBytesFromBinaryStream(binaryStream);
        char[] recordTypeHexChars = encodeBytesToHexChars(userIdByteArray);
        return Long.toString(encodeHexCharsToLong(recordTypeHexChars));
    }

    protected byte[] readEightBytesFromBinaryStream(DataInputStream binaryStream) {
        byte[] eightByteArray = new byte[8];
        readFromBinaryStreamIntoByteArray(binaryStream, eightByteArray);
        return eightByteArray;
    }

    public final RecordType getType() {
        return type;
    }

    public final String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Record Type:[" + type.name() + "] Time Stamp:[" + timestamp + "] User ID:[" + userId + "]";
    }
}
