package com.adhoc.homework.transactionlogparser.arguments;

import static com.adhoc.homework.transactionlogparser.TestConstants.*;
import static com.adhoc.homework.transactionlogparser.arguments.Arg.*;
import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArgExtractorTest {
    private String[] args;

    @Test
    public void logFilePath_whenValidCommandAndArgumentPresentThenOk() {
        // given
        args = new String[]{"--"+TRANSACTION_LOG_FILE+"="+ VALID_LOG_FILE_PATH};
        // when
        String extractedFilePathValue = ArgExtractor.extractRequiredArg(TRANSACTION_LOG_FILE, args);
        // then
        assertThat(extractedFilePathValue).isEqualTo(VALID_LOG_FILE_PATH);
    }

    @Test
    public void logFilePath_whenMissingValueThenEmptyStringReturned() {
        // given
        args = new String[]{"--"+TRANSACTION_LOG_FILE+"="};
        // when
        String extractedFilePathValue = ArgExtractor.extractRequiredArg(TRANSACTION_LOG_FILE, args);
        // then
        assertThat(extractedFilePathValue).isEmpty();
    }

    @Test
    public void logFilePath_whenRequiredArgMissingThenException() {
        // given
        args = new String[]{};
        // when/then
        assertCallToExtractRequiredArgThrowsParserArgsException(TRANSACTION_LOG_FILE);
    }

    private void assertCallToExtractRequiredArgThrowsParserArgsException(Arg argToExtract) {
        Assertions.assertThrows(ArgException.class, () -> {        // (2) - then
            ArgExtractor.extractRequiredArg(argToExtract, args);  // (1) - when
        });
    }

    @Test
    public void userId_whenValidCommandAndArgumentPresentThenOk() {
        // given
        args = new String[]{"--"+USER_ID+"="+ VALID_USER_ID};
        // when
        String extractedFilePathValue = ArgExtractor.extractRequiredArg(USER_ID, args);
        // then
        assertThat(extractedFilePathValue).isEqualTo(VALID_USER_ID);
    }

    @Test
    public void userId_whenMissingValueThenEmptyStringReturned() {
        // given
        args = new String[]{"--"+USER_ID+"="};
        // when
        String extractedFilePathValue = ArgExtractor.extractRequiredArg(USER_ID, args);
        // then
        assertThat(extractedFilePathValue).isEmpty();
    }

    @Test
    public void whenNoArgsPassedThenException() {
        // given
        args = new String[]{};
        // when/then
        assertCallToExtractRequiredArgThrowsParserArgsException(USER_ID);
    }

    @Test
    public void userId_whenOnlyCommandValueThenException() {
        // given
        args = new String[]{"--="+ VALID_USER_ID};
        // when/then
        assertCallToExtractRequiredArgThrowsParserArgsException(USER_ID);
    }
}
