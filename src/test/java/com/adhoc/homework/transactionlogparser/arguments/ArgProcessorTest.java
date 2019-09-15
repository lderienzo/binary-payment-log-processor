package com.adhoc.homework.transactionlogparser.arguments;

import static com.adhoc.homework.transactionlogparser.TestConstants.*;
import static com.adhoc.homework.transactionlogparser.arguments.Arg.*;
import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.jupiter.api.*;


public class ArgProcessorTest {
    private String[] args;

    @Test
    public void whenBothValuesValidThenValidatedArgsReturnedWithBothArgsPresent() {
        // given
        args = new String[]{"--"+TRANSACTION_LOG_FILE+"="+VALID_LOG_FILE_PATH,
                        "--"+USER_ID+"="+ VALID_USER_ID};
        // when
        ValidatedArgs validatedArgs = ArgProcessor.getValidatedArgs(args);
        // then
        assertAllArgsValid(validatedArgs);
    }

    private void assertAllArgsValid(ValidatedArgs validatedArgs) {
        assertValidatedArgsAreValid(validatedArgs, true);
    }

    private void assertValidatedArgsAreValid(ValidatedArgs validatedArgs, boolean userIdShouldBePresent) {
        assertThat(validatedArgs.getPath().toString()).isEqualTo(VALID_LOG_FILE_PATH);
        if (userIdShouldBePresent)
            assertThat(validatedArgs.getUserId().get()).isEqualTo(VALID_USER_ID);
        else
            assertThat(validatedArgs.getUserId().isPresent()).isFalse();
    }

    @Test
    public void whenOnlyValidPathEnteredThenOnlyValidPathReturned() {
        // given
        args = new String[]{"--"+TRANSACTION_LOG_FILE+"="+VALID_LOG_FILE_PATH};
        // when
        ValidatedArgs validatedArgs = ArgProcessor.getValidatedArgs(args);
        // then
        assertArgsValidWhenOnlyRequiredPathPresent(validatedArgs);
    }

    private void assertArgsValidWhenOnlyRequiredPathPresent(ValidatedArgs validatedArgs) {
        assertValidatedArgsAreValid(validatedArgs, false);
    }

    @Test
    public void whenValidPathPresentAndUserIdAbsentThenOnlyValidatedPathReturned() {
        // given
        args = new String[]{"--"+TRANSACTION_LOG_FILE+"="+VALID_LOG_FILE_PATH,"--"+USER_ID+"="};
        // when
        ValidatedArgs validatedArgs = ArgProcessor.getValidatedArgs(args);
        // then
        assertArgsValidWhenOnlyRequiredPathPresent(validatedArgs);
    }

    @Test
    public void whenValidPathAndOnlyUserIdValuePresentThenOnlyValidatedPathReturned() {
        // given
        args = new String[]{"--"+TRANSACTION_LOG_FILE+"="+VALID_LOG_FILE_PATH,"--="+VALID_USER_ID};
        // when
        ValidatedArgs validatedArgs = ArgProcessor.getValidatedArgs(args);
        // then
        assertArgsValidWhenOnlyRequiredPathPresent(validatedArgs);
    }

    @Test
    public void whenAllArgsAbsentThenException() {
        // given
        args = new String[]{};
        // when/then
        assertCallToProcessMethodThrowsParserArgsException();
    }

    @Test
    public void whenRequiredPathAbsentAndOptionalUserIdPresentThenException() {
        // given
        args = new String[]{"--"+USER_ID+"="+ VALID_USER_ID};
        // when/then
        assertCallToProcessMethodThrowsParserArgsException();
    }

    private void assertCallToProcessMethodThrowsParserArgsException() {
        Assertions.assertThrows(ArgException.class, () -> {   // (2) - then
            ArgProcessor.getValidatedArgs(args);             // (1) - when
        });
    }

    @Test
    public void whenRequiredPathValueAbsentThenException() {
        // given
        args = new String[]{"--"+TRANSACTION_LOG_FILE+"="};
        // when/then
        assertCallToProcessMethodThrowsParserArgsException();
    }

    @Test
    public void whenPathValueInValidThenException() {
        // given
        args = new String[]{"--"+TRANSACTION_LOG_FILE+"="+VALID_LOG_FILE_PATH+"/something/bogus"};
        // when/then
        assertCallToProcessMethodThrowsParserArgsException();
    }

    @Test
    public void whenValidPathButInvalidUserIdThenException() {
        // given
        args = new String[]{"--"+TRANSACTION_LOG_FILE+"="+VALID_LOG_FILE_PATH,
                                        "--"+USER_ID+"="+VALID_USER_ID+"222"};
        // when/then
        assertCallToProcessMethodThrowsParserArgsException();
    }
}
