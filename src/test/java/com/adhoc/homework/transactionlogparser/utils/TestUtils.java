package com.adhoc.homework.transactionlogparser.utils;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {

    public static Path getResourceFilePath(String fileName) {
        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        return FileSystems.getDefault().getPath(absolutePath, fileName);
    }
}
