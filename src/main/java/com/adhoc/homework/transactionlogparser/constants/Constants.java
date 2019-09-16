package com.adhoc.homework.transactionlogparser.constants;

import static com.adhoc.homework.transactionlogparser.arguments.Arg.TRANSACTION_LOG_FILE;
import static com.adhoc.homework.transactionlogparser.arguments.Arg.USER_ID;

public class Constants {
    public static final String USAGE_MSG = "\nUsage:\n" + "java -cp \"binary-payment-log-processor.jar\" com.adhoc.homework.transactionlogparser.Parser" +
            " --" + TRANSACTION_LOG_FILE + "=<file_path> " +
            " [--" + USER_ID + "=<user_id>]\n";
    public static final String FILE_PROCESSING_ERROR_MSG = "Error processing proprietary binary transaction file.";
    public static final String ERROR_SETTING_FILE_PATH_MSG = "Error obtaining log file path.";
    public static final String PROPRIETARY_PROTOCOL = "MPS7";
    public static final int MAX_RECORDS = 100;
}
