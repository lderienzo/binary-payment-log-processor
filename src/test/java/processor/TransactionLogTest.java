package processor;



import static org.assertj.core.api.Java6Assertions.assertThat;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.adhoc.homework.transactionlogparser.TransactionLog;

public class TransactionLogTest {

    private static TransactionLog transactionLog;

    @BeforeAll
    public static void init() {
        processLogFile();
    }

    private static void processLogFile() {
        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        Path pathToTransactionLogFile = FileSystems.getDefault().getPath(absolutePath, "txnlog.dat");
        try (DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(
                        Files.newInputStream(pathToTransactionLogFile, StandardOpenOption.READ)))) {
            transactionLog = TransactionLog.process(dataInputStream);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
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
