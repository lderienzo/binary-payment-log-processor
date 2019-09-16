package com.adhoc.homework.transactionlogparser.arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

final class ArgExtractor {
    private static final Logger LOG = LogManager.getLogger(ArgExtractor.class);
    private final String[] args;
    private final Arg argName;

    private ArgExtractor(Arg argName, String... args) {
        this.argName = argName;
        this.args = args;
    }

    public static String extractRequiredArg(Arg argName, String... args) {
        ArgExtractor argExtractor = new ArgExtractor(argName, args);
        return argExtractor.getArgFromArgsArray();
    }

    public static String extractOptionalArg(Arg argName, String... args) {
        ArgExtractor argExtractor = new ArgExtractor(argName, args);
        String argValue = "";
        try {
            argValue = argExtractor.getArgFromArgsArray();
        } catch (ArgException e) {
            LOG.debug("Optional argument of "+argName+ " is absent.");
        }
        return argValue;
    }

    private String getArgFromArgsArray() {
        return Arrays.stream(args)
                .filter(arg -> arg.contains(argName.toString()))
                .map(arg -> arg.substring(arg.indexOf('=') + 1))
                .collect(toSingleValue(argName.toString()));
    }

    private static <T> Collector<T, ?, T> toSingleValue(String argName) {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
                    throwExceptionIfListSizeNotEqualToOne(argName, list);
                    return list.get(0);
                }
        );
    }

    private static void throwExceptionIfListSizeNotEqualToOne(String argName, List list) {
        if (list.size() != 1) {
            throw new ArgException("Error extracting value from command line."
                    + (list.size() == 0 ? " Missing argument \"" + argName + "\"."
                    : " Multiple values present for argument \"" + argName + "\"."));
        }
    }
}
