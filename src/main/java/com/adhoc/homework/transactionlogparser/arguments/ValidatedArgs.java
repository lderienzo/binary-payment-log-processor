package com.adhoc.homework.transactionlogparser.arguments;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;

import com.google.common.base.Strings;

public final class ValidatedArgs {
    private final Path logFilePath;
    private final String userId;

    public ValidatedArgs(Path logFilePath, String userId) {
        this.logFilePath = logFilePath;
        this.userId = userId;
    }

    public ValidatedArgs() {
        logFilePath = FileSystems.getDefault().getPath("");
        userId = "";
    }

    public Path getPath() {
        return logFilePath;
    }

    public Optional<String> getUserId() {
        if (userIdIsPresent())
            return Optional.of(userId);
        else
            return Optional.empty();
    }

    private boolean userIdIsPresent() {
        return !Strings.isNullOrEmpty(userId);
    }
}
