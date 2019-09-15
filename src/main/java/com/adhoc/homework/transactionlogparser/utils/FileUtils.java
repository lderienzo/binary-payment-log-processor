package com.adhoc.homework.transactionlogparser.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.adhoc.homework.transactionlogparser.ParserException;

public final class FileUtils {

    public static DataInputStream getDataInputStreamFromPath(Path path) {
        try {
            return new DataInputStream(new BufferedInputStream(
                     Files.newInputStream(path, StandardOpenOption.READ)));
        } catch (IOException e) {
            throw new ParserException("Error reading log file.", e);
        }
    }
}
