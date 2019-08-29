package com.adhoc.homework.transactionlogparser;

import java.util.List;

public class TransactionLog extends BinaryFileWithProprietaryProtocol {

    private List<PaymentType> paymentTypes;

    public long totalDollarsInDebits() {
        return 0;
    }

    public long totalDollarsInCredits() {
        return 0;
    }

    public int numberOfAutopaysStarted() {
        return 0;
    }

    public int numberOfAutopaysEnded() {
        return 0;
    }


    public float getBalanceForUser(String id) {
        return 0;
    }

}

