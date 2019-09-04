package processor;



import static org.assertj.core.api.Java6Assertions.assertThat;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.Test;

import com.adhoc.homework.transactionlogparser.TransactionLog;
import com.google.common.base.Strings;

public class ProcessorTest {

    @Test
    public void testProcessor() {
        readFile();
    }

    private void readFile() {
        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        Path pathToTransactionLogFile = FileSystems.getDefault().getPath(absolutePath, "txnlog.dat");
        try (DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(
                        Files.newInputStream(pathToTransactionLogFile, StandardOpenOption.READ)))) {
            TransactionLog transactionLog = TransactionLog.process(dataInputStream);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String writeByteArrayAsString(byte[] arrayToPrint) {
        return Strings.nullToEmpty(new String(arrayToPrint, StandardCharsets.US_ASCII));
    }

    private long getUInt32(byte[] bytes) {
        long value = bytes[0] & 0xFF;
        value |= (bytes[1] << 8) & 0xFFFF;
        value |= (bytes[2] << 16) & 0xFFFFFF;
        value |= (bytes[3] << 24) & 0xFFFFFFFF;
        return value;
    }
}
