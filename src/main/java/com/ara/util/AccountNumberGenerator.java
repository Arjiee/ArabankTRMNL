package com.ara.util;

import java.util.concurrent.atomic.AtomicInteger;


//incremental account number creation NOT RANDOM
public class AccountNumberGenerator {

    // Bank-specific configurations
    private static String bankCode = "";
    private static String branchCode = "";

    // Thread-safe incremental counter (starts at 1)
    private static AtomicInteger customerCounter = new AtomicInteger(1);


    // Initializes the generator with fixed bank and branch codes.

    public AccountNumberGenerator(String bankCode, String branchCode) {
        this.bankCode = bankCode;
        this.branchCode = branchCode;
        this.customerCounter = new AtomicInteger(1);
    }

    // Generates and returns the next valid, incremental bank account number.
    public static synchronized String generateNextAccountNumber() {
        // 1. Get the next incremental ID
        int nextId = customerCounter.getAndIncrement();

        // 2. Format the customer ID to a fixed width of 5 digits (e.g., 00001, 00002)
        String customerIdStr = String.format("%05d", nextId);

        // 3. Assemble the base number (11 digits total)
        String baseNumber = bankCode + branchCode + customerIdStr;

        // 4. Calculate the 12th check digit using the Luhn algorithm
        int checkDigit = calculateLuhnCheckDigit(baseNumber);

        // 5. Return the final account number
        return baseNumber + checkDigit;
    }

    /**
     * Helper method to calculate the Luhn check digit.
     */
    private static int calculateLuhnCheckDigit(String baseNumber) {
        int sum = 0;
        boolean alternate = true;

        for (int i = baseNumber.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(baseNumber.charAt(i));

            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        int mod = sum % 10;
        return (mod == 0) ? 0 : (10 - mod);
    }

}
