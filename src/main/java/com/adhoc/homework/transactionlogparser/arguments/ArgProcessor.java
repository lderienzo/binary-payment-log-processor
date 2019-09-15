package com.adhoc.homework.transactionlogparser.arguments;

import static com.adhoc.homework.transactionlogparser.arguments.Arg.*;

import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Optional;

import com.google.common.base.Strings;

public final class ArgProcessor {
    private String[] args;
    private Optional<String> userId;
    private Path logFilePath;
    private static final String ERROR_SETTING_FILE_PATH_MSG = "Error obtaining log file path.";


    public static ValidatedArgs getValidatedArgs(String... args) {
        return new ArgProcessor(args).getValidatedArgs();
    }

    private ArgProcessor(String... args) {
        this.args = args;
    }

    private ValidatedArgs getValidatedArgs() {
        processTransactionLogFilePathArg();
        processUserId();
        return new ValidatedArgs(logFilePath, userId);
    }

    private void processTransactionLogFilePathArg() {
        String valueEntered = extractFilePathFromArgs();
        validateFilePath(valueEntered);
        setFilePath(valueEntered);
    }

    private String extractFilePathFromArgs() {
        return ArgExtractor.extractRequiredArg(TRANSACTION_LOG_FILE, args);
    }

    private void validateFilePath(String path) {
        if (invalidFilePath(path))
            throw new ArgException(ERROR_SETTING_FILE_PATH_MSG + " Invalid log file path.");
    }

    private boolean invalidFilePath(String path) {
        return !ArgValidator.isValidPath(path);
    }

    private void setFilePath(String path) {
        try {
            logFilePath = FileSystems.getDefault().getPath(path);
        } catch (InvalidPathException e) {
            throw new ArgException(ERROR_SETTING_FILE_PATH_MSG + " Illegal path characters encountered.", e);
        }
    }

    private void processUserId() {
        Optional<String> valueEntered = extractUserId();
        if (valueEntered.isPresent())
            validateUserId(valueEntered.get());
        setUserId(valueEntered);
    }

    private Optional<String> extractUserId() {
        String userId = ArgExtractor.extractOptionalArg(USER_ID, args);
        if (Strings.isNullOrEmpty(userId))
            return Optional.empty();
        return Optional.of(userId);
    }

    private void validateUserId(String id) {
        ArgValidator.isValidUserId(id);
    }

    private void setUserId(Optional<String> id) {
        userId = id;
    }

    public Path getLogFilePath() {
        return logFilePath;
    }

    public Optional<String> getUserId() {
        return userId;
    }
}
