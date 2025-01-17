package com.quod.bo.TradeReconProcess.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class JsonDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper mapper;
    private final Class<T> clazz;

    /**
     * @param mapper - ObjectMapper
     * @param clazz  - Class<T>
     */
    public JsonDeserializer(ObjectMapper mapper, Class<T> clazz) {
        this.mapper = Objects.requireNonNull(mapper, "mapper");
        this.clazz = Objects.requireNonNull(clazz, "clazz");
    }

    /**
     * @param topic - String
     * @param data  - byte[]
     * @return T
     */
    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        String stringData = new String(data, StandardCharsets.UTF_8);

        try {
            return mapper.readValue(stringData, clazz);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException("Unable to deserialize a message in the topic " + topic, e);
        }
    }
}

