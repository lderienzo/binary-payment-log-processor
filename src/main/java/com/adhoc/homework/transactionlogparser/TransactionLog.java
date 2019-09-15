package com.adhoc.homework.transactionlogparser;

import static com.adhoc.homework.transactionlogparser.RecordType.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Hex;


public final class TransactionLog implements ProprietaryFormatBinaryFile {

    private static final String FILE_PROCESSING_ERROR_MSG = "Error processing proprietary binary transaction file.";
    private static final String PROPRIETARY_PROTOCOL = "MPS7";
    private static final int MAX_RECORDS = 100;
    private DataInputStream fileInputStream;
    private List<Record> transactionRecords;
    private Header header;

    private TransactionLog(DataInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
        this.header = Header.read(fileInputStream);
    }

    public static TransactionLog createFromBinaryStream(DataInputStream fileInputStream) {
        TransactionLog log = new TransactionLog(fileInputStream);
        if (log.getHeader().isValid())
            log.processRecords();
        else
            throw new BinaryFileParsingException(FILE_PROCESSING_ERROR_MSG + " Invalid header data.");
        return log;
    }

    private Header getHeader() {
        return header;
    }

    private void processRecords() {
        transactionRecords = new ArrayList<>(header.getNumberOfRecords());
        for (int i = 0; i < header.getNumberOfRecords(); i++) {
            RecordType recordType = readRecordTypeEnum(fileInputStream);
            Record record = new RecordFactoryImpl().makeRecord(recordType, fileInputStream);
            transactionRecords.add(record);
        }
    }

    private RecordType readRecordTypeEnum(DataInputStream inputStream) {
        int recordTypeHex = readRecordType(inputStream);
        return RecordType.values()[recordTypeHex];
    }

    private static int readRecordType(DataInputStream inputStream) {
        byte[] recordTypeByte = readSingleByte(inputStream);
        char[] recordTypeHexChars = encodeBytesToHexChars(recordTypeByte);
        int recordTypeDecimal = encodeHexCharsToInt(recordTypeHexChars);
        return recordTypeDecimal;
    }

    private static byte[] readSingleByte(DataInputStream binaryStream) {
        byte[] singleByteArray = new byte[1];
        readFromBinaryStreamIntoByteArray(binaryStream, singleByteArray);
        return singleByteArray;
    }

    private static char[] encodeBytesToHexChars(byte[] byteArrayToEncode) {
        return Hex.encodeHex(byteArrayToEncode);
    }

    // TODO - DRY PRINCIPLE VIOLATION -- consolodate stream reading/manipulating activites in it's own class and reuse.
    private static void readFromBinaryStreamIntoByteArray(DataInputStream readFromStream, byte[] intoArray) {
        try {
            readFromStream.read(intoArray, 0, intoArray.length);
        } catch (IOException e) {
            throw new BinaryFileParsingException(FILE_PROCESSING_ERROR_MSG, e);
        }
    }

    private static int encodeHexCharsToInt(char[] hexCharsToEncode) {
        return Integer.parseInt(new String(hexCharsToEncode), 16);
    }

    public double totalDollarsInDebits() {
        return transactionRecords.stream()
                .filter(record -> record.getType() == DEBIT)
                .mapToDouble(record -> ((RecordWithDollarAmount)record)
                        .getDollarAmount()).sum();
    }

    public double totalDollarsInCredits() {
        return transactionRecords.stream()
                .filter(record -> record.getType() == CREDIT)
                .mapToDouble(record -> ((RecordWithDollarAmount)record)
                        .getDollarAmount()).sum();
    }

    public long numberOfAutopaysStarted() {
        return transactionRecords.stream()
                .filter(record -> record.getType() == START_AUTOPAY).count();
    }

    public long numberOfAutopaysEnded() {
        return transactionRecords.stream()
                .filter(record -> record.getType() == END_AUTOPAY).count();
    }

    public double getBalanceForUser(String userId) {
        List<Record> userTransactions = getTransactionsForUser(userId);
        return sumTransactionAmounts(userTransactions);
    }

    private double sumTransactionAmounts(List<Record> transactionRecords) {
        return transactionRecords.stream()
                .filter(record -> (record.getType() == CREDIT || record.getType() == DEBIT))
                .mapToDouble(record -> ((RecordWithDollarAmount)record)
                .getDollarAmount()).sum();
    }

    private List<Record> getTransactionsForUser(String userId) {
        return transactionRecords.stream()
                .filter(record -> record.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    static class Header {

        private DataInputStream fileStreamWithHeader;
        private String binaryProtocolFormat;
        private int bytesActuallyRead;
        private int numberOfRecords = 101;
        private int bytesToRead = 4;
        private int version = 0;

        public static Header read(DataInputStream fileStreamWithHeader) {
            return new Header(fileStreamWithHeader);
        }

        private Header(DataInputStream fileStreamWithHeader) {
            this.fileStreamWithHeader = fileStreamWithHeader;
            binaryProtocolFormat = readProtocolFormat();
            version = readVersion();
            numberOfRecords = readNumberOfRecords();
        }

        private String readProtocolFormat() {
            byte[] protocalFormatArray = new byte[bytesToRead];
            bytesActuallyRead = readProtocolFormatFromInputStream(protocalFormatArray, protocalFormatArray.length);
            if (bytesActuallyRead != protocalFormatArray.length)
                throw new BinaryFileParsingException(FILE_PROCESSING_ERROR_MSG + " Wrong number of bytes read while determining protocol format.");
            return new String(protocalFormatArray);
        }

        private int readProtocolFormatFromInputStream(byte[] readIntoArray, int bytesToRead) {
            return readDataFromInputStreamIntoArray(readIntoArray,0, bytesToRead);
        }

        private int readDataFromInputStreamIntoArray(byte[] readIntoArray, int offsetToBeginReadingFrom, int bytesToRead) {
            try  {
                return fileStreamWithHeader.read(readIntoArray, offsetToBeginReadingFrom, bytesToRead);
            } catch (IOException e) {
                throw new BinaryFileParsingException(e);
            }
        }

        private int readVersion() {
            try {
                return version = fileStreamWithHeader.read();
            } catch (IOException e) {
                throw new BinaryFileParsingException(e);
            }
        }

        private int readNumberOfRecords() {
            byte[] recordNumberArray = new byte[bytesToRead];
            bytesActuallyRead = readNumberOfRecordsFromInputStream(recordNumberArray, 0, recordNumberArray.length);
            if (bytesActuallyRead != recordNumberArray.length)
                throw new BinaryFileParsingException(FILE_PROCESSING_ERROR_MSG + " Wrong number of bytes read while determining number of transaction records.");
            return recordNumberArray[3];
        }

        private int readNumberOfRecordsFromInputStream(byte[] readIntoArray, int offsetToBeginReadingFrom, int bytesToRead) {
            return readDataFromInputStreamIntoArray(readIntoArray, offsetToBeginReadingFrom, bytesToRead);
        }

        public boolean isValid() {
            return binaryProtocolFormat.equals(TransactionLog.PROPRIETARY_PROTOCOL)
                    && numberOfRecords <= MAX_RECORDS && version > 0;
        }

        public int getNumberOfRecords() {
            return numberOfRecords;
        }
    }
}
