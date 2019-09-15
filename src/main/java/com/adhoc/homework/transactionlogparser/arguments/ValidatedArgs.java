package com.adhoc.homework.transactionlogparser.arguments;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;

public final class ValidatedArgs {
    private final Path logFilePath;
    private final Optional<String> userId;

    public ValidatedArgs(Path logFilePath, Optional<String> userId) {
        this.logFilePath = logFilePath;
        this.userId = userId;
    }

    public ValidatedArgs() {
        logFilePath = FileSystems.getDefault().getPath("");
        userId = Optional.empty();
    }

    public Path getPath() {
        return logFilePath;
    }

    public Optional<String> getUserId() {
        return userId;
    }
}
