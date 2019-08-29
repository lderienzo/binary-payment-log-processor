package com.adhoc.homework.transactionlogparser;

import java.io.DataInputStream;
import java.io.IOException;

public class BinaryFile {

    private static final String PROPRIETARY_PROTOCOL = "MPS7";
    private static final int MAX_RECORDS = 100;

    private BinaryFile() {}

    public static class Header {

        private DataInputStream fileStreamWithHeader;
        private String binaryProtocolFormat;
        private int bytesActuallyRead;
        private int numberOfRecords = 101;
        private int bytesToRead = 4;
        private int version = 0;


        public static Header readFromFile(DataInputStream fileStreamWithHeader) {
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
                throw new BinaryFileParsingException("Error reading header information. Wrong number of bytes read.");
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
                throw new BinaryFileParsingException("Error reading header information. Wrong number of bytes read.");
            return recordNumberArray[3];    //TODO: this doesn't feel 100% ok. Feels like we should convert an unsigned 32 bit int
        }

        private int readNumberOfRecordsFromInputStream(byte[] readIntoArray, int offsetToBeginReadingFrom, int bytesToRead) {
            return readDataFromInputStreamIntoArray(readIntoArray, offsetToBeginReadingFrom, bytesToRead);
        }

        public boolean isValid() {
            return binaryProtocolFormat.equals(BinaryFile.PROPRIETARY_PROTOCOL)
                    && numberOfRecords <= MAX_RECORDS && version > 0;
        }

        public int getNumberOfRecords() {
            return numberOfRecords;
        }
    }

}
