package com.adhoc.homework.transactionlogparser.arguments;

import static com.adhoc.homework.transactionlogparser.TestConstants.*;
import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArgValidatorTest {
    private ArgValidator argValidator = new ArgValidator();

    @Test
    public void logFilePath_whenActualFilePathThenIsValidTrue() {
        // given/when/then
        assertThat(argValidator.isValidPath(VALID_LOG_FILE_PATH)).isTrue();
    }

    @Test
    public void logFilePath_whenInvalidThenIsValidFalse() {
        // given/when/then
        assertThat(argValidator.isValidPath("some/invalid/bogus/path/value")).isFalse();
    }

    @Test
    public void logFilePath_whenEmptyThenIsValidFalse() {
        // given/when/then
        assertThat(argValidator.isValidPath("")).isFalse();
    }

    @Test
    public void logFilePath_whenNullThenIsValidFalse() {
        // given/when/then
        assertThat(argValidator.isValidPath(null)).isFalse();
    }

    @Test
    public void userId_whenCorrectNumericFormatThenIsValidTrue() {
        // given/when/then
        assertThat(argValidator.isValidUserId(VALID_USER_ID)).isTrue();
    }

    @Test
    public void userId_whenValueTooLongThenIsValidFalse() {
        // given/when/then
        assertCallToIsValidUserIdThrowsArgExceptionForValue(VALID_USER_ID +"112");
    }

    @Test
    public void userId_whenTooShortThenIsValidFalse() {
        // given/when/then
        assertThat(argValidator.isValidUserId(VALID_USER_ID.substring(VALID_USER_ID.length()/2))).isFalse();
    }

    @Test
    public void userId_whenNonNumericThenIsValidFalse() {
        // given/when/then
        assertCallToIsValidUserIdThrowsArgExceptionForValue("this_is_an_invalid_non_numeric_value");
    }

    private void assertCallToIsValidUserIdThrowsArgExceptionForValue(String value) {
        Assertions.assertThrows(ArgException.class, () -> {
            argValidator.isValidUserId(value);
        });
    }
}
