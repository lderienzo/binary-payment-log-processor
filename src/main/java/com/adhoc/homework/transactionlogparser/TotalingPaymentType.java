package com.adhoc.homework.transactionlogparser;

import org.joda.money.Money;

public interface TotalingPaymentType extends PaymentType {
        Money getPayment();
}
