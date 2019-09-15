package com.adhoc.homework.transactionlogparser;

import static com.adhoc.homework.transactionlogparser.FileUtils.getDataInputStreamFromPath;
import static com.adhoc.homework.transactionlogparser.arguments.Arg.*;

import java.io.DataInputStream;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.adhoc.homework.transactionlogparser.arguments.ArgException;
import com.adhoc.homework.transactionlogparser.arguments.ArgProcessor;
import com.adhoc.homework.transactionlogparser.arguments.ValidatedArgs;

public final class Parser {
    private static final Logger LOG = LogManager.getLogger(Parser.class);
    private static final String USAGE_MSG = "\nUsage:\n" + "binary-payment-log-processor.jar" +
            " --" + TRANSACTION_LOG_FILE + "=<path> " +
            " --" + USER_ID + "=<user_id>\n";
    private TransactionLog transactionLog;
    private ValidatedArgs validatedArgs;

    public static void main(String... args) {
        new Parser().run(args);
    }

    public void run(String... args) {
        validatedArgs = getValidatedArgs(args);
        DataInputStream inputStream = getBinaryStreamFromFilePath(validatedArgs.getPath());
        transactionLog = TransactionLog.createFromBinaryStream(inputStream);
        printAnswersToConsole();
    }

    private ValidatedArgs getValidatedArgs(String... args) {
        ValidatedArgs validatedArgs = new ValidatedArgs();
        try {
            validatedArgs = ArgProcessor.getValidatedArgs(args);
        } catch (ArgException e) {
            LOG.error("Error processing arguments.", e);
            System.out.println(USAGE_MSG);
        }
        return validatedArgs;
    }

    private DataInputStream getBinaryStreamFromFilePath(Path path) {
        return getDataInputStreamFromPath(path);
    }

    private void printAnswersToConsole() {
        System.out.println("======================================================\n");
        System.out.println("Total Amount of Debits:\n$" +transactionLog.totalDollarsInDebits()+"\n");
        System.out.println("Total Amount of Credits:\n$" +transactionLog.totalDollarsInCredits()+"\n");
        System.out.println("Number of Autopays Started:\n" +transactionLog.numberOfAutopaysStarted()+"\n");
        System.out.println("Number of Autopays Ended:\n" + transactionLog.numberOfAutopaysEnded()+"\n");
        if (validatedArgs.getUserId().isPresent())
            System.out.println("Balance of User ID \""+validatedArgs.getUserId().get()+"\":\n$"
                    +transactionLog.getBalanceForUser((validatedArgs.getUserId().get()))+"\n");
        System.out.println("======================================================");
    }
}
