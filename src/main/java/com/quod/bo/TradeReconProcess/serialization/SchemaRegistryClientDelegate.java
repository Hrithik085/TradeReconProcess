package com.quod.bo.TradeReconProcess.serialization;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.quod.bo.TradeReconProcess.properties.PropertiesProvider;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.avro.Schema;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public final class SchemaRegistryClientDelegate {

    public final static byte WIRE_FORMAT_MAGIC_BYTE = NumberUtils.BYTE_ZERO;
    public final static int WIRE_FORMAT_SCHEMA_ID_FIELD_SIZE = Integer.BYTES;

    private final static String SUBJECT_KEY_SUFFIX = "-key";
    private final static String SUBJECT_VALUE_SUFFIX = "-value";
    private final static int SUBJECT_NOT_FOUND_ERROR_CODE = 40401;

    private final AvroMapper mapper;
    private final int cacheCapacity;
    private final SchemaRegistryClient client;

    private final Cache<String, SchemaMetadata> cacheSubjectToSchemaMetadata;

    private final Map<String, Cache<Integer, AvroSchema>> cacheByVersion = new ConcurrentHashMap<>();
    private final Map<String, Cache<Integer, AvroSchema>> cacheById = new ConcurrentHashMap<>();
    private final Map<String, Cache<AvroSchema, Integer>> cacheSchemaToId = new ConcurrentHashMap<>();

    /**
     * @param mapper        - AvroMapper
     * @param cacheCapacity - int
     */
    public SchemaRegistryClientDelegate(AvroMapper mapper, int cacheCapacity) {
        this.mapper = Objects.requireNonNull(mapper, "mapper");
        if (cacheCapacity < NumberUtils.INTEGER_ONE) {
            throw new IllegalArgumentException("Invalid cache capacity: " + cacheCapacity);
        }
        this.cacheCapacity = cacheCapacity;

        var schemaRegistryUrl = PropertiesProvider.KAFKA_STREAMS.getProperty(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG);
        if (StringUtils.isBlank(schemaRegistryUrl)) {
            throw new IllegalArgumentException("The property " + KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG + " is not set");
        }

        this.cacheSubjectToSchemaMetadata = CacheBuilder.newBuilder()
                .maximumSize(this.cacheCapacity)
                .expireAfterWrite(Duration.ofMinutes(5))
                .build();

        this.client = new CachedSchemaRegistryClient(schemaRegistryUrl, cacheCapacity);
    }

    /**
     * @param clazz - Class<?>
     * @param topic - String
     * @param isKey - boolean
     * @return AvroSchema
     * @throws - RestClientException
     * @throws - java.io.IOException
     * @throws - java.util.concurrent.ExecutionException
     */
    public AvroSchema lookupSchema(Class<?> clazz, String topic, boolean isKey) throws RestClientException, IOException, ExecutionException {
        var subject = topic + (isKey ? SUBJECT_KEY_SUFFIX : SUBJECT_VALUE_SUFFIX);
        try {
            var metadata = retrieveSchemaMetadata(subject);
            return retrieveSchemaByVersion(subject, metadata.getVersion());
        } catch (ExecutionException e) {
            if (e.getCause() instanceof RestClientException) {
                var cause = (RestClientException) e.getCause();
                if (cause.getErrorCode() == SUBJECT_NOT_FOUND_ERROR_CODE) {
                    return registerSchema(clazz, subject);
                }
            }
            throw e;
        }
    }

    /**
     * @param id    - int
     * @param topic - String
     * @param isKey - boolean
     * @return AvroSchema
     * @throws - ExecutionException
     */
    public AvroSchema getSchemaById(int id, String topic, boolean isKey) throws ExecutionException {
        var subject = topic + (isKey ? SUBJECT_KEY_SUFFIX : SUBJECT_VALUE_SUFFIX);
        try {
            return retrieveSchemaById(subject, id);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof RestClientException) {
                var cause = (RestClientException) e.getCause();
                if (cause.getErrorCode() == SUBJECT_NOT_FOUND_ERROR_CODE) {
                    return null;
                }
            }
            throw e;
        }
    }

    /**
     * @param schema - AvroSchema
     * @param topic  - String
     * @param isKey  - boolean
     * @return int
     * @throws -ExecutionException
     */
    public int getSchemaId(AvroSchema schema, String topic, boolean isKey) throws ExecutionException {
        var subject = topic + (isKey ? SUBJECT_KEY_SUFFIX : SUBJECT_VALUE_SUFFIX);
        try {
            return retrieveIdBySchema(subject, schema);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof RestClientException) {
                var cause = (RestClientException) e.getCause();
                if (cause.getErrorCode() == SUBJECT_NOT_FOUND_ERROR_CODE) {
                    return NumberUtils.INTEGER_ZERO;
                }
            }
            throw e;
        }
    }

    /**
     * @param clazz   - Class<?>
     * @param subject - String
     * @return AvroSchema
     * @throws -RestClientException
     * @throws -IOException
     */
    private AvroSchema registerSchema(Class<?> clazz, String subject) throws IOException, RestClientException {
        var schema = generateSchema(clazz);
        var schemaToRegister = new io.confluent.kafka.schemaregistry.avro.AvroSchema(schema.getAvroSchema().toString());
        client.register(subject, schemaToRegister);
        return schema;
    }

    /**
     * @param clazz - Class<?>
     * @return AvroSchema
     * @throws -JsonMappingException
     */
    private AvroSchema generateSchema(Class<?> clazz) throws JsonMappingException {
        var generator = new AvroSchemaGenerator();
        generator.enableLogicalTypes();
        mapper.acceptJsonFormatVisitor(clazz, generator);
        return generator.getGeneratedSchema();
    }

    /**
     * @param subject - String
     * @return SchemaMetadata
     * @throws -ExecutionException
     */
    private SchemaMetadata retrieveSchemaMetadata(String subject) throws ExecutionException {
        return cacheSubjectToSchemaMetadata.get(subject, () -> client.getLatestSchemaMetadata(subject));
    }

    /**
     * @param subject - String
     * @param version - int
     * @return AvroSchema
     * @throws -ExecutionException
     */
    private AvroSchema retrieveSchemaByVersion(String subject, int version) throws ExecutionException {
        var cache = cacheByVersion.computeIfAbsent(subject, key -> createNewCache());
        return cache.get(version, () -> {
            var registeredSchema = client.getByVersion(subject, version, false);
            var parsedSchema = new Schema.Parser().parse(registeredSchema.getSchema());
            return new AvroSchema(parsedSchema);
        });
    }

    /**
     * @param subject - String
     * @param id      - int
     * @return AvroSchema
     * @throws -ExecutionException
     */
    private AvroSchema retrieveSchemaById(String subject, int id) throws ExecutionException {
        var cache = cacheById.computeIfAbsent(subject, key -> createNewCache());
        return cache.get(id, () -> {
            var registeredSchema = client.getSchemaById(id);
            var parsedSchema = new Schema.Parser().parse(registeredSchema.canonicalString());
            return new AvroSchema(parsedSchema);
        });
    }

    /**
     * @param subject - String
     * @param schema  - AvroSchema
     * @return int
     * @throws -ExecutionException
     */
    private int retrieveIdBySchema(String subject, AvroSchema schema) throws ExecutionException {
        var cache = cacheSchemaToId.computeIfAbsent(subject, key -> createNewCache());
        return cache.get(schema, () -> {
            var parsedSchema = new io.confluent.kafka.schemaregistry.avro.AvroSchema(schema.getAvroSchema().toString());
            return client.getId(subject, parsedSchema);
        });
    }

    /**
     * @return <K, V> Cache<K, V>
     */
    private <K, V> Cache<K, V> createNewCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(cacheCapacity)
                .build();
    }
}
