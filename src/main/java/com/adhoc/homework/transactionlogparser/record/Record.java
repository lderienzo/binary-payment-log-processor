package com.adhoc.homework.transactionlogparser.record;

import java.io.DataInputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Hex;

import com.adhoc.homework.transactionlogparser.transactionlog.LogParsingException;

public class Record {
    private static final String FILE_PROCESSING_ERROR_MSG = "Error processing proprietary binary transaction file.";
    private RecordType type;
    private long timestamp;
    private String userId;

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

    private void readFromBinaryStreamIntoByteArray(DataInputStream readFromStream, byte[] intoArray) {
        try {
            readFromStream.read(intoArray, 0, intoArray.length);
        } catch (IOException e) {
            throw new LogParsingException(FILE_PROCESSING_ERROR_MSG, e);
        }
    }

    protected char[] encodeBytesToHexChars(byte[] byteArrayToEncode) {
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

    public final long getTimestamp() {
        return timestamp;
    }

    public final String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Record Type:[" + type.name() + "] Time Stamp:[" + timestamp + "] User ID:[" + userId + "]";
    }
}
