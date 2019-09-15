package com.adhoc.homework.transactionlogparser.record;

import java.io.DataInputStream;

public interface RecordFactory {
    Record makeRecord(RecordType type, DataInputStream inputStream);
}
