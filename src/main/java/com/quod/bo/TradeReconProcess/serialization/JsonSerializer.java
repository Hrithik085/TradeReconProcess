package com.quod.bo.TradeReconProcess.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class JsonSerializer<T> implements Serializer<T> {

    private final ObjectMapper mapper;

    /**
     * @param mapper - ObjectMapper
     */
    public JsonSerializer(ObjectMapper mapper) {
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    /**
     * @param topic - String
     * @param data  - T
     * @return byte[]
     */
    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }

        try {
            return mapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException("Unable to serialize a message in the topic " + topic, e);
        }
    }
}
