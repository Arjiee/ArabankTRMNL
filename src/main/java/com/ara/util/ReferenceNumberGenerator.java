package com.ara.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Generates unique transaction reference numbers.
 *
 * <p>Format: {@code TXN<yyyyMMddHHmmss><9-digit-sequence>}
 * <br>Example: {@code TXN20240601143022000000001}
 *
 * <p>The {@link AtomicLong} counter is safe for concurrent use within a single JVM.
 * For a multi-node setup this should be replaced with a database sequence or UUID.
 */
public final class ReferenceNumberGenerator {

    private static final String PREFIX = "TXN";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static final AtomicLong SEQUENCE = new AtomicLong(0L);

    private ReferenceNumberGenerator() {
    }

    public static String generate() {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        long seq = SEQUENCE.incrementAndGet();
        return String.format("%s%s%09d", PREFIX, timestamp, seq);  // 9-digit zero-padded sequence
    }
}
