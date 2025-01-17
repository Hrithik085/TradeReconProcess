package com.quod.bo.TradeReconProcess.serialization;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.jsr310.AvroJavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.quod.bo.TradeReconProcess.model.IsKey;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.StateStore;
import org.apache.kafka.streams.processor.StreamPartitioner;
import org.springframework.stereotype.Component;

import java.util.Objects;
@Component
public final class SerdesFactory {

    private static final int DEFAULT_CACHE_CAPACITY = 16;

    private final JsonMapper jsonMapper;
    private final AvroMapper avroMapper;
    private final SchemaRegistryClientDelegate client;

    public SerdesFactory() {
        jsonMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        avroMapper = AvroMapper.builder()
                .addModule(new AvroJavaTimeModule())
                .build();

        client = new SchemaRegistryClientDelegate(avroMapper, DEFAULT_CACHE_CAPACITY);
    }

    /**
     * @param clazz - Class<?>
     * @return <V> Consumed<String, V>
     */
    public <V> Consumed<String, V> consumeJson(Class<V> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        return Consumed.with(Serdes.String(), createJsonSerde(clazz));
    }

    /**
     * @param keyClazz   -Class<K>
     * @param valueClazz -Class<V>
     * @return <K extends IsKey, V> Consumed<K, V>
     */
    public <K extends IsKey, V> Consumed<K, V> consumeJson(Class<K> keyClazz, Class<V> valueClazz) {
        Objects.requireNonNull(keyClazz, "keyClazz");
        Objects.requireNonNull(valueClazz, "valueClazz");
        return Consumed.with(createJsonSerde(keyClazz), createJsonSerde(valueClazz));
    }

    /**
     * @param clazz -Class<V>
     * @return <V> Grouped<String, V>
     */
    public <V> Grouped<String, V> groupedJson(Class<V> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        return Grouped.with(Serdes.String(), createJsonSerde(clazz));
    }

    /**
     * @param keyClazz   -Class<K>
     * @param valueClazz -Class<V>
     * @return <K extends IsKey, V> Grouped<K, V>
     */
    public <K extends IsKey, V> Grouped<K, V> groupedJson(Class<K> keyClazz, Class<V> valueClazz) {
        Objects.requireNonNull(keyClazz, "keyClazz");
        Objects.requireNonNull(valueClazz, "valueClazz");
        return Grouped.with(createJsonSerde(keyClazz), createJsonSerde(valueClazz));
    }

    /**
     * @param keyClazz   -Class<K>
     * @param valueClazz -Class<V>
     * @return <K extends IsKey, V, S extends StateStore> Materialized<K, V, S>
     */
    public <K extends IsKey, V, S extends StateStore> Materialized<K, V, S> materializeJson(Class<K> keyClazz, Class<V> valueClazz) {
        Objects.requireNonNull(keyClazz, "keyClazz");
        Objects.requireNonNull(valueClazz, "valueClazz");
        return Materialized.with(createJsonSerde(keyClazz), createJsonSerde(valueClazz));
    }

    /**
     * @param partitioner -StreamPartitioner<String, V>
     * @param clazz       -Class<V>
     * @return <V> Produced<String, V>
     */
    public <V> Produced<String, V> produceJson(Class<V> clazz, StreamPartitioner<String, V> partitioner) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(partitioner, "partitioner");
        return Produced.with(Serdes.String(), createJsonSerde(clazz), partitioner);
    }

    /**
     * @param clazz -Class<V>
     * @return <V> Consumed<String, V>
     */
    public <V> Consumed<String, V> consumeAvro(Class<V> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        return Consumed.with(Serdes.String(), createAvroSerde(clazz));
    }

    /**
     * @param clazz -Class<V>
     * @return <V> Consumed<Integer, V>
     */
    public <V> Consumed<Integer, V> consumeAvroInt(Class<V> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        return Consumed.with(Serdes.Integer(), createAvroSerde(clazz));
    }

    /**
     * @param keyClazz   -Class<K>
     * @param valueClazz -Class<V>
     * @return <K extends IsKey, V> Consumed<K, V>
     */
    public <K extends IsKey, V> Consumed<K, V> consumeAvro(Class<K> keyClazz, Class<V> valueClazz) {
        Objects.requireNonNull(keyClazz, "keyClazz");
        Objects.requireNonNull(valueClazz, "valueClazz");
        return Consumed.with(createAvroSerde(keyClazz), createAvroSerde(valueClazz));
    }

    /**
     * @param partitioner -StreamPartitioner<String, V>
     * @param clazz       -Class<V>
     * @return <V> Produced<String, V>
     */
    public <V> Produced<String, V> produceAvro(Class<V> clazz, StreamPartitioner<String, V> partitioner) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(partitioner, "partitioner");
        return Produced.with(Serdes.String(), createAvroSerde(clazz), partitioner);
    }

    /**
     * @param clazz -Class<V>
     * @return <V> Produced<String, V>
     */
    public <V> Produced<String, V> produceAvro(Class<V> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        return Produced.with(Serdes.String(), createAvroSerde(clazz));
    }

    /**
     * @param keyClazz    -Class<K>
     * @param valueClazz  -Class<V>
     * @param partitioner -StreamPartitioner<String, V>
     * @return <K extends IsKey, V> Produced<K, V>
     */

    public <K extends IsKey, V> Produced<K, V> produceAvro(Class<K> keyClazz, Class<V> valueClazz, StreamPartitioner<K, V> partitioner) {
        Objects.requireNonNull(keyClazz, "keyClazz");
        Objects.requireNonNull(valueClazz, "valueClazz");
        Objects.requireNonNull(partitioner, "partitioner");
        return Produced.with(createAvroSerde(keyClazz), createAvroSerde(valueClazz), partitioner);
    }

    /**
     * @param keyClazz   -Class<K>
     * @param valueClazz -Class<V>
     * @return <K extends IsKey, V> Produced<K, V>
     */
    public <K extends IsKey, V> Produced<K, V> produceAvro(Class<K> keyClazz, Class<V> valueClazz) {
        Objects.requireNonNull(keyClazz, "keyClazz");
        Objects.requireNonNull(valueClazz, "valueClazz");
        return Produced.with(createAvroSerde(keyClazz), createAvroSerde(valueClazz));
    }

    /**
     * @param clazz -Class<T>
     * @return <T> Serde<T>
     */
    public <T> Serde<T> createJsonSerde(Class<T> clazz) {
        Serializer<T> serializer = new JsonSerializer<>(jsonMapper);
        Deserializer<T> deserializer = new JsonDeserializer<>(jsonMapper, clazz);
        return new Serdes.WrapperSerde<>(serializer, deserializer);
    }

    /**
     * @param clazz -Class<T>
     * @return <T> Serde<T>
     */
    public <T> Serde<T> createAvroSerde(Class<T> clazz) {
        boolean isKey = IsKey.class.isAssignableFrom(clazz);

        Serializer<T> serializer = new AvroSerializer<>(client, avroMapper, clazz);
        serializer.configure(null, isKey);

        Deserializer<T> deserializer = new AvroDeserializer<>(client, avroMapper, clazz);
        deserializer.configure(null, isKey);

        return new Serdes.WrapperSerde<>(serializer, deserializer);
    }
}
