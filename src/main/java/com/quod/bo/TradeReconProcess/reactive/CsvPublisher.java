package com.quod.bo.TradeReconProcess.reactive;

import com.quod.bo.TradeReconProcess.opencsvextentions.CustomCsvToBeanBuilder;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;

public final class CsvPublisher<T> extends AbstractBlockingPublisher<T> {

    private final Path file;
    private final Class<T> clazz;

    private BufferedReader reader;
    private Iterator<T> iterator;

    public CsvPublisher(Path file, Class<T> clazz) {
        this.file = Objects.requireNonNull(file);
        this.clazz = Objects.requireNonNull(clazz);
    }

    @Override
    void open() throws Exception {
        if (reader == null) {
            reader = Files.newBufferedReader(file);
            iterator = new CustomCsvToBeanBuilder<>(reader, clazz)
                    .withType(clazz)
                    .withIgnoreEmptyLine(true)
                    .build()
                    .iterator();
        }
    }

    @Override
    T read() throws Exception {
        if (reader == null || iterator == null) {
            throw new IllegalStateException("closed");
        }
        return iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    void close() throws Exception {
        if (reader != null) {
            reader.close();
            iterator = null;
            reader = null;
        }
    }
}
