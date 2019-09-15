package com.adhoc.homework.transactionlogparser;



import static com.adhoc.homework.transactionlogparser.FileUtils.getDataInputStreamFromPath;
import static com.adhoc.homework.transactionlogparser.TestUtils.getResourceFilePath;
import static org.assertj.core.api.Java6Assertions.assertThat;

import java.io.*;
import java.nio.file.*;

import org.junit.jupiter.api.*;

public class TransactionLogTest {

    private static TransactionLog transactionLog;

    @BeforeAll
    public static void init() {
        createTransactionLogFile();
    }

    private static void createTransactionLogFile() {
        Path pathToTransactionLogFile = getResourceFilePath("txnlog.dat");
        DataInputStream dataInputStream = getDataInputStreamFromPath(pathToTransactionLogFile);
        transactionLog = TransactionLog.createFromBinaryStream(dataInputStream);
    }

    @Test
    public void getBalanceForUserUsingVerifiableFirstRecord() {
        assertThat(transactionLog.getBalanceForUser("4136353673894269217")).isEqualTo(604.274335557087);
    }

    @Test
    public void getBalanceForUserAsPerAssignmentTask() {
        assertThat(transactionLog.getBalanceForUser("2456938384156277127")).isEqualTo(497.1698630676499);
    }

    @Test
    public void getBalanceForUserWithTwoTransactionsButOnlyOneWithBalance() {
        assertThat(transactionLog.getBalanceForUser("7979830878773245174")).isEqualTo(608.1343005023912);
    }

    @Test
    public void getTotalDollarsInCredits() {
        assertThat(transactionLog.totalDollarsInCredits()).isEqualTo(9366.019984181883);
    }

    @Test
    public void getTotalDollarsInDebits() {
        assertThat(transactionLog.totalDollarsInDebits()).isEqualTo(18203.69953340208);
    }

    @Test
    public void getNumberOfAutoPaysStarted() {
        assertThat(transactionLog.numberOfAutopaysStarted()).isEqualTo(10);
    }

    @Test
    public void getNumberOfAutoPaysEnded() {
        assertThat(transactionLog.numberOfAutopaysEnded()).isEqualTo(8);
    }
}
