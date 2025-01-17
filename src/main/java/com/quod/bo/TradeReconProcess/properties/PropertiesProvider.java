package com.quod.bo.TradeReconProcess.properties;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author - tra865
 * @date - 28-04-2022
 */
public enum PropertiesProvider {

    APP("app.properties"),
    KAFKA_PRODUCER("kafka-producer.properties"),
    KAFKA_CONSUMER("kafka-consumer.properties"),
    GLOBAL_PROPERTY("GlobalProperties.properties"),
    KAFKA_STREAMS("kafka-streams.properties");


    private final Properties properties;

    /**
     * @param fileName - String
     */
    PropertiesProvider(String fileName) {
        properties = new Properties();
        Path path = getConfigPath().resolve(fileName);
        try (InputStream is = new FileInputStream(path.toFile())) {
            properties.load(is);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to load resource: " + fileName, e);
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public static Path getConfigPath() {

 Path config = Paths.get("TradeReconProcess/config");
     //  Path config = Paths.get("config");

        if (Files.notExists(config)) {
            throw new UncheckedIOException("Configuration path does not exist", new FileNotFoundException("config"));
        }
        if (!Files.isDirectory(config)) {
            throw new UncheckedIOException("Configuration path is not a directory", new IOException("config"));
        }

        Path dev = config.resolve("dev");
        if (Files.exists(dev) && Files.isDirectory(dev)) {
            return dev;
        }

        return config;
    }
}
