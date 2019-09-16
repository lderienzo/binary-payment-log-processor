package com.adhoc.homework.transactionlogparser.utils;

import static com.adhoc.homework.transactionlogparser.constants.Constants.FILE_PROCESSING_ERROR_MSG;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.adhoc.homework.transactionlogparser.ParserException;
import com.adhoc.homework.transactionlogparser.transactionlog.LogParsingException;

public final class FileUtils {

    public static DataInputStream getDataInputStreamFromPath(Path path) {
        try {
            return new DataInputStream(new BufferedInputStream(
                     Files.newInputStream(path, StandardOpenOption.READ)));
        } catch (IOException e) {
            throw new ParserException("Error reading log file.", e);
        }
    }

    public static void readFromBinaryStreamIntoByteArray(DataInputStream readFromStream, byte[] intoArray) {
        try {
            readFromStream.read(intoArray, 0, intoArray.length);
        } catch (IOException e) {
            throw new LogParsingException(FILE_PROCESSING_ERROR_MSG, e);
        }
    }
}
