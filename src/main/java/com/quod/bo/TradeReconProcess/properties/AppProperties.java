package com.quod.bo.TradeReconProcess.properties;

import java.util.Objects;

public class AppProperties {

    private static final String CSV_CHECK_INTERVAL_MS = "csv-check-interval-ms";
    private static final String CSV_READER_CONCURRENCY = "csv-reader-concurrency";
    private static final String INPUT_TRANSACTIONS_CSV_FOLDER = "input-transactions-csv-folder";

    public static long getCsvCheckIntervalMs() {
        String value = PropertiesProvider.APP.getProperty(CSV_CHECK_INTERVAL_MS);
        Objects.requireNonNull(value, CSV_CHECK_INTERVAL_MS + " should be provided in properties");
        return Long.parseLong(value);
    }

    public static int getCsvReaderConcurrency() {
        String value = PropertiesProvider.APP.getProperty(CSV_READER_CONCURRENCY);
        Objects.requireNonNull(value, CSV_READER_CONCURRENCY + " should be provided in properties");
        return Integer.parseInt(value);
    }

    public static String getInputTransactionsCsvFolder() {
        String value = PropertiesProvider.APP.getProperty(INPUT_TRANSACTIONS_CSV_FOLDER);
        Objects.requireNonNull(value, INPUT_TRANSACTIONS_CSV_FOLDER + " should be provided in properties");
        return value;
    }
}
