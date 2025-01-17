package com.quod.bo.TradeReconProcess.serialization;

import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;

public final class AvroSerializer<T> implements Serializer<T> {

    private final SchemaRegistryClientDelegate client;
    private final AvroMapper mapper;
    private final Class<T> clazz;

    private boolean isKey;

    /**
     * @param client - SchemaRegistryClientDelegate
     * @param mapper - AvroMapper
     * @param clazz  - Class<T>
     */
    public AvroSerializer(SchemaRegistryClientDelegate client, AvroMapper mapper, Class<T> clazz) {
        this.client = Objects.requireNonNull(client, "client");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
        this.clazz = Objects.requireNonNull(clazz, "clazz");
    }

    /**
     * @param configs - Map<String, ?>
     * @param isKey   - boolean
     * @return void
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        this.isKey = isKey;
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

        try (var output = new ByteArrayOutputStream()) {
            var schema = client.lookupSchema(clazz, topic, isKey);
            var schemaId = client.getSchemaId(schema, topic, isKey);
            var value = mapper.writerFor(clazz).with(schema).writeValueAsBytes(data);
            output.write(SchemaRegistryClientDelegate.WIRE_FORMAT_MAGIC_BYTE);
            output.write(ByteBuffer.allocate(SchemaRegistryClientDelegate.WIRE_FORMAT_SCHEMA_ID_FIELD_SIZE).putInt(schemaId).array());
            output.write(value);
            return output.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Unable to serialize a message in the topic " + topic, e);
        }
    }
}

