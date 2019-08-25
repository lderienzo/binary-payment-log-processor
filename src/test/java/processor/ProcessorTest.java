package processor;



import static org.assertj.core.api.Java6Assertions.assertThat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.common.base.Strings;
import com.google.common.io.Resources;

public class ProcessorTest {

    @Test
    public void testProcessor() throws IOException {
        readFile();
    }

    private void readFile() throws IOException {
        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        Path pathToPaymentLogFile = FileSystems.getDefault().getPath(absolutePath, "txnlog.dat");

//        int bytesRead = 0;
//        int headerByteSize = 9;
//        byte[] headerBuffer = new byte[headerByteSize];
//        try(InputStream input = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))) {
//            bytesRead = input.(headerBuffer);
//
//            // manipulate stream
//        } catch(IOException e) {
//            throw new RuntimeException(e);
//        }

//        assertThat(bytesRead).isEqualTo(headerByteSize);

        byte[] fileBytes;
        try {
            fileBytes = Files.readAllBytes(pathToPaymentLogFile);
            System.out.println(Arrays.toString(fileBytes));
//            String string = new String(fileBytes);
//            System.out.println(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        ByteBuffer paymentLogByteBuffer;
//        try {

            // The interpretation of bytes is up to the program.
            paymentLogByteBuffer = ByteBuffer.wrap(Files.readAllBytes(pathToPaymentLogFile));

            // BEGIN READ HEADER DATA
            int offsetToBeginReadingFrom = 0;
            int bytesToRead = 4;
            byte[] protocalFormatArray = new byte[bytesToRead];
            paymentLogByteBuffer.get(protocalFormatArray, offsetToBeginReadingFrom, bytesToRead);
            System.out.println(Strings.nullToEmpty(new String(protocalFormatArray, StandardCharsets.US_ASCII)));

            offsetToBeginReadingFrom = 0;
            bytesToRead = 1;
            byte[] versionArray = new byte[bytesToRead];
            paymentLogByteBuffer.get(versionArray, offsetToBeginReadingFrom, bytesToRead);
            System.out.println(versionArray[0]);

            offsetToBeginReadingFrom = 0;
            bytesToRead = 4;
            byte[] recordNumberArray = new byte[bytesToRead];
            paymentLogByteBuffer.get(recordNumberArray, offsetToBeginReadingFrom, bytesToRead);
            System.out.println(recordNumberArray[3]);
            // END READ HEADER DATA






//            System.out.println(getUInt32(recordNumberArray));



            
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//        Resources.asByteSource(ClassLoader.getSystemResource("txnlog.dat"));
    }


    private long getUInt32(byte[] bytes) {
        long value = bytes[0] & 0xFF;
        value |= (bytes[1] << 8) & 0xFFFF;
        value |= (bytes[2] << 16) & 0xFFFFFF;
        value |= (bytes[3] << 24) & 0xFFFFFFFF;
        return value;
    }

    private void getMagicProtocalFormatString(int beginByte, int endByte) {



    }

}
