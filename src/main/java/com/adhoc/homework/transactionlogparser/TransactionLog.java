package com.adhoc.homework.transactionlogparser;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

public class TransactionLog implements ProprietaryFormatBinaryFile {

    private static final String PROPRIETARY_PROTOCOL = "MPS7";
    private static final String FILE_PROCESSING_ERROR_MSG = "Error processing proprietary binary transaction file.";
    private static final int MAX_RECORDS = 100;
    private DataInputStream fileInputStream;
    private List<Transaction> transactions;
    private Header header;

    private TransactionLog(DataInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
        this.header = Header.read(fileInputStream);
    }

    public static TransactionLog process(DataInputStream fileInputStream) {
        // Read header
        // -make sure its valid
        TransactionLog log = new TransactionLog(fileInputStream);
        if (log.getHeader().isValid()) {
            log.processRows();
        }
        else
            throw new BinaryFileParsingException(FILE_PROCESSING_ERROR_MSG + " Invalid header data.");

        return log;
    }

    private Header getHeader() {
        return header;
    }

    private void processRows() {
        // -obtain number of records
        //      - use number of records to process records
        // Process Records
        // -read record type to understand how to process each row
        //      - process each row according to record type.
        int bytesToRead = 4;

        // BEGIN PROCESSING ROWS
        int startAutopayCounter = 0;
        int endAutopayCounter = 0;

        try {
            for (int i = 0; i < header.getNumberOfRecords(); i++) {
                int readFrom = 0;
                bytesToRead = 12;
                int recordTypeHex = readRecordTypeHexInt(fileInputStream);
                switch (recordTypeHex) {
                    case 0:
                    case 1:
                        bytesToRead = 20;
                        break;
                    case 2:
                        startAutopayCounter++;
                        break;
                    case 3:
                        endAutopayCounter++;
                        break;
                    default:
                        // shouldn't get here
                }
                byte[] restOfLine = new byte[bytesToRead];
                fileInputStream.read(restOfLine, readFrom, bytesToRead);
                System.out.println(String.copyValueOf(Hex.encodeHex(restOfLine)));
            }
            // BEGIN PROCESSING ROWS

            // BEGIN REPORTING
            System.out.println("START AUTOPAYS: " + startAutopayCounter);
            System.out.println("END AUTOPAYS: " + endAutopayCounter);
            // END REPORTING

        } catch(IOException e) {
            throw new BinaryFileParsingException(FILE_PROCESSING_ERROR_MSG, e);
        }
    }

    private static int readRecordTypeHexInt(DataInputStream paymentLog) throws IOException {
        byte[] recordTypeArray = new byte[1];
        paymentLog.read(recordTypeArray, 0, 1);
        char[] hexChars = Hex.encodeHex(recordTypeArray);
        int decimal = Integer.parseInt(new String(hexChars), 16);
        return decimal;
    }

    public long totalDollarsInDebits() {
        return 0;
    }

    public long totalDollarsInCredits() {
        return 0;
    }

    public int numberOfAutopaysStarted() {
        return 0;
    }

    public int numberOfAutopaysEnded() {
        return 0;
    }

    public float getBalanceForUser(String id) {
        return 0;
    }

    public static class Header {

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
            bytesActuallyRead = readProtocolFormatFromInputStream(protocalFormatArray,0, protocalFormatArray.length);
            if (bytesActuallyRead != protocalFormatArray.length)
                throw new BinaryFileParsingException(FILE_PROCESSING_ERROR_MSG + " Wrong number of bytes read while determining protocol format.");
            return new String(protocalFormatArray);
        }

        private int readProtocolFormatFromInputStream(byte[] readIntoArray, int offsetToBeginReadingFrom, int bytesToRead) {
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
            return recordNumberArray[3];    //TODO: this doesn't feel 100% ok. Feels like we should convert an unsigned 32 bit int
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
