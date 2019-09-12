package com.adhoc.homework.transactionlogparser;

import java.io.DataInputStream;

public interface RecordFactory {
    Record makeRecord(RecordType type, DataInputStream inputStream);
}
