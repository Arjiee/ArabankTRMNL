package com.ara.util;

import java.util.concurrent.atomic.AtomicInteger;


//incremental account number creation NOT RANDOM
public class AccountNumberGenerator {

    public static String accountNumberGenerator(long databaseId) {
        long baseSequence = 1000000000L + databaseId;

        long branch = (baseSequence / 1000000) % 10000;   // 4 digits
        long customer = (baseSequence / 100) % 10000;     // 4 digits
        long type = baseSequence % 100;                   // 2 digits

        return String.format("%04d-%04d-%02d", branch, customer, type);
    }

}
