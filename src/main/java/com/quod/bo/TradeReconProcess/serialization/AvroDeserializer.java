package com.quod.bo.TradeReconProcess.serialization;

import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;

public final class AvroDeserializer<T> implements Deserializer<T> {

    private final SchemaRegistryClientDelegate client;
    private final AvroMapper mapper;
    private final Class<T> clazz;
    private boolean isKey;

    /**
     * @param client - SchemaRegistryClientDelegate
     * @param mapper - AvroMapper
     * @param clazz  - Class<T>
     */
    public AvroDeserializer(SchemaRegistryClientDelegate client, AvroMapper mapper, Class<T> clazz) {
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
     * @param data  - byte[]
     * @return T
     */
    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            var buffer = ByteBuffer.wrap(data);

            var magic = buffer.get();
            if (magic != SchemaRegistryClientDelegate.WIRE_FORMAT_MAGIC_BYTE) {
                throw new IllegalArgumentException("Unknown magic byte");
            }

            var schemaId = buffer.getInt();
            var schema = client.getSchemaById(schemaId, topic, isKey);
            if (schema == null) {
                schema = client.lookupSchema(clazz, topic, isKey);
            }

            var payload = new byte[buffer.remaining()];
            buffer.get(payload);
            return mapper.readerFor(clazz).with(schema).readValue(payload);
        } catch (Exception e) {
            throw new RuntimeException("Unable to deserialize a message in the topic " + topic, e);
        }
    }
}
