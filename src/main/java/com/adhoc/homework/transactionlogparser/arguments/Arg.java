package com.adhoc.homework.transactionlogparser.arguments;

public enum Arg {
    TRANSACTION_LOG_FILE {
        @Override
        public String toString() {
            return "log-file-path";
        }
    },
    USER_ID {
        @Override
        public String toString() {
            return "user-id-for-balance";
        }
    }
}
