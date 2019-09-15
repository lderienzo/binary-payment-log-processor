package com.adhoc.homework.transactionlogparser.arguments;

import java.io.File;

import com.google.common.base.Strings;

final class ArgValidator {

    public static boolean isValidPath(String path) {
        if (valueAbsent(path))
            return false;
        return isReadableFile(path);
    }
    
    private static boolean valueAbsent(String path) {
        return Strings.isNullOrEmpty(path);
    }

    private static boolean isReadableFile(String path) {
        File f = new File(path);
        return f.isFile() && f.canRead();
    }

    public static boolean isValidUserId(String userId) {
        validIfParseableAsLong(userId);
        return validIfNineteenNumbersLong(userId);
    }

    private static void validIfParseableAsLong(String userId) {
        try {
            Long.toString(Long.parseLong(userId));
        } catch (NumberFormatException e) {
            throw new ArgException("Invalid value entered for \"id\"");
        }
    }

    private static boolean validIfNineteenNumbersLong(String userId) {
        return userId.length() == 19;
    }
}
