package processor;



import static org.assertj.core.api.Java6Assertions.assertThat;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import com.google.common.base.Strings;

public class ProcessorTest {

    @Test
    public void testProcessor() throws IOException {
        readFile();
    }

    private void readFile() throws IOException {
        Path resourceDirectory = Paths.get("src","test","resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        Path pathToPaymentLogFile = FileSystems.getDefault().getPath(absolutePath, "txnlog.dat");

//        byte[] fileBytes;
//        try {
//            fileBytes = Files.readAllBytes(pathToPaymentLogFile);
//            System.out.println(Arrays.toString(fileBytes));
//            String string = new String(fileBytes);
//            System.out.println(string);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


//        ByteBuffer paymentLogByteBuffer;
//        try {

        // The interpretation of bytes is up to the program.
//        paymentLogByteBuffer = ByteBuffer.wrap(Files.readAllBytes(pathToPaymentLogFile));

        // BEGIN READ HEADER DATA
//        int offsetToBeginReadingFrom = 0;
//        int bytesToRead = 4;
//        byte[] protocalFormatArray = new byte[bytesToRead];
//        paymentLogByteBuffer.get(protocalFormatArray, offsetToBeginReadingFrom, bytesToRead);
//        System.out.println(Strings.nullToEmpty(new String(protocalFormatArray, StandardCharsets.US_ASCII)));
//
//        offsetToBeginReadingFrom = 0;
//        bytesToRead = 1;
//        byte[] versionArray = new byte[bytesToRead];
//        paymentLogByteBuffer.get(versionArray, offsetToBeginReadingFrom, bytesToRead);
//        System.out.println(versionArray[0]);
//
//        offsetToBeginReadingFrom = 0;
//        bytesToRead = 4;
//        byte[] recordNumberArray = new byte[bytesToRead];
//        paymentLogByteBuffer.get(recordNumberArray, offsetToBeginReadingFrom, bytesToRead);
//        System.out.println(recordNumberArray[3]);
//        // END READ HEADER DATA
//
//        // BEGIN READ ROW DATA
//        offsetToBeginReadingFrom = 0;
//        bytesToRead = 1;
//        byte[] recordTypeEnumArray = new byte[bytesToRead];
//        paymentLogByteBuffer.get(recordTypeEnumArray, offsetToBeginReadingFrom, bytesToRead);
//        System.out.println(recordTypeEnumArray[0]);


            // END READ ROW DATA


//            System.out.println(getUInt32(recordNumberArray));

            
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//        Resources.asByteSource(ClassLoader.getSystemResource("txnlog.dat"));



        try (DataInputStream paymentLog = new DataInputStream(new BufferedInputStream(
                        Files.newInputStream(pathToPaymentLogFile, StandardOpenOption.READ)))) {

            // manipulate stream

            int offsetToBeginReadingFrom = 0;
            int bytesToRead = 4;
            byte[] protocalFormatArray = new byte[bytesToRead];
            paymentLog.read(protocalFormatArray, offsetToBeginReadingFrom, bytesToRead);
            System.out.println(writeByteArrayAsString(protocalFormatArray));

            byte version = paymentLog.readByte();
            System.out.println(version);

            byte[] recordNumberArray = new byte[bytesToRead];
            paymentLog.read(recordNumberArray, offsetToBeginReadingFrom, bytesToRead);
            System.out.println(recordNumberArray[3]);

            int debitCounter = 0;
            int creditCounter = 0;
            int startAutopayCounter = 0;
            int endAutopayCounter = 0;
            int otherCounter = 0;
            for (int i = 0; i < recordNumberArray[3]; i++) {

                String recordTypeHex = null;
                try {
                    recordTypeHex = readRecordTypeHexValue(paymentLog);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e ) {
                    throw new RuntimeException(e);
                }

                int readFrom = 0;
                bytesToRead = 12;

                if (recordTypeHex.equals("0x00") || recordTypeHex.equals("0x01")) {
                    bytesToRead = 20;
                    if (recordTypeHex.equals("0x00")) {
                        debitCounter++;
                    }
                    if (recordTypeHex.equals("0x01")) {
                        creditCounter++;
                    }
                }
                else if (recordTypeHex.equals("0x02")) {
                    startAutopayCounter++;
                }
                else if (recordTypeHex.equals("0x03")) {
                    endAutopayCounter++;
                }
                else {
                    otherCounter++;
                }

                byte[] restOfLine = new byte[bytesToRead];
                paymentLog.read(restOfLine, readFrom, bytesToRead);
System.out.println(String.copyValueOf(Hex.encodeHex(restOfLine)));
            }

            System.out.println("NUM DEBITS: " + debitCounter);
            System.out.println("NUM CREDITS: " + creditCounter);
            System.out.println("START AUTOPAYS: " + startAutopayCounter);
            System.out.println("END AUTOPAYS: " + endAutopayCounter);
            System.out.println("OTHER: " + otherCounter);
            
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readRecordTypeHexValue(DataInputStream paymentLog) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        byte[] recordTypeArray = new byte[1];
        paymentLog.read(recordTypeArray, 0, 1);
        char[] hexChars = Hex.encodeHex(recordTypeArray);
System.out.println(hexChars);
//        Method m = Hex.class.getDeclaredMethod("toDigit", Character.TYPE, Integer.TYPE);
//        m.setAccessible(true);
//        for (int i = 0; i < hexChars.length; i++) {
//            System.out.println(m.invoke(hexChars[i], 0));
//        }
        return "0x" + String.copyValueOf(Hex.encodeHex(recordTypeArray));
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
