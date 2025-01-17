package com.quod.bo.TradeReconProcess.dataprovider;

import com.quod.bo.TradeReconProcess.broker.topics.Topic;
import com.quod.bo.TradeReconProcess.model.ProcessRequestResponse;
import com.quod.bo.TradeReconProcess.model.ProcessorReport;
import com.quod.bo.TradeReconProcess.model.ProcessorReportKey;
import com.quod.bo.TradeReconProcess.properties.PropertiesProvider;
import com.quod.bo.TradeReconProcess.reactive.CsvPublisher;
import com.quod.bo.TradeReconProcess.serialization.SerdesFactory;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Component
public abstract class CsvDataProvider<K, V> {


    SerdesFactory serdesFactory = new SerdesFactory();

    private static final Logger logger = LoggerFactory.getLogger(CsvDataProvider.class);
    private final Path folder;
    private final long checkIntervalMs;
    private final Class<V> valueClass;

    private final KafkaSender<K, V> sender;
    private final Set<Path> processedFiles = new ConcurrentSkipListSet<>();

    public CsvDataProvider(Path folder, long checkIntervalMs, Class<V> valueClass, SerdesFactory serdesFactory) {
        Objects.requireNonNull(folder, "folder");
        Objects.requireNonNull(valueClass, "valueClass");
        this.folder = folder;
        this.checkIntervalMs = checkIntervalMs;
        this.valueClass = valueClass;
        SenderOptions<K, V> senderOptions = SenderOptions.create(PropertiesProvider.KAFKA_PRODUCER.getProperties());
        senderOptions = senderOptions
                .withKeySerializer(serdesFactory.createAvroSerde(getKeyClass()).serializer())
                .withValueSerializer(serdesFactory.createAvroSerde(getValueClass()).serializer())
                .maxInFlight(1000000);
        this.sender = KafkaSender.create(senderOptions);
    }

    private List<Path> getFiles() {
        try (var stream = Files.list(folder)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".csv"))
                    .filter(path -> !processedFiles.contains(path))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void start(boolean infinitely) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                for (var file : getFiles()) {
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    try {
                        processFile(file).get();
                    } finally {
                        processedFiles.add(file);
                    }
                }
                if (!infinitely) {
                    break;
                }
                Thread.sleep(checkIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("Exception during CSV files processing", e);
            }
        }
    }

    public void startInfinitely() {
        start(true);
        sender.close();
    }

    public void startOnePass() {
        start(false);
    }

    protected Future<Void> processFile(Path file) {

        var future = new CompletableFuture<Void>();
        AtomicInteger i = new AtomicInteger();
        var source = file.getFileName();
        var publisher = Flux.from(new CsvPublisher<>(file, valueClass))
                .map(it -> {
                    //     source =file.getFileName();
                    i.getAndIncrement();
                    var record = createProducerRecord(it);
                    return SenderRecord.create(record, record.key());
                });
        sender.send(publisher)
                .doOnError(t -> {
                    Random rand = new Random();
                    var processorReport = new ProcessorReport();
                    var key = new ProcessorReportKey();
//                    String id = String.format("%04d", rand.nextInt(10000));
//                    key.setProcessID(id);
                    processorReport.setProcessID("1");
                    processorReport.setProcessDate(String.valueOf(LocalDate.now()));
                    processorReport.setProcessName("TRP");
                    processorReport.setStatus("N");
                    processorReport.setAlive("Y");
                    processorReport.setTotalRows(i.longValue());
                    processorReport.setSourceFile(String.valueOf(source));
                    String start = StringUtils.substringBefore(processorReport.getSourceFile(), "_");
                    String processNme = StringUtils.substringAfter(processorReport.getSourceFile(), "_");
                    String processNAme1 = StringUtils.substringBefore(processNme, ".");
                    logger.info("1234321:::::::::" +processNAme1);
                    processorReport.setProcessName(processNAme1);
                    key.setProcessID(start);
                    var producer = createProducer();
                    var record = new ProducerRecord<>(Topic.PROCESS_RESPONSE.getName(), key, processorReport);
                    producer.send(record);
                    logger.error("CSV file processing error {}", file, t);
                    Path folder = Paths.get("TradeReconProcess/input_csv/processed/ErrorFile.csv");
                    try {
                        Files.move(file, folder, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    future.completeExceptionally(t);
                })
                .doOnComplete(() -> {
                    var processorReport = new ProcessorReport();
                    Random rand = new Random();
                    var key = new ProcessorReportKey();
//                    String id = String.format("%04d", rand.nextInt(10000));
//                    key.setProcessID(id);
                    processorReport.setProcessID("1");
                    processorReport.setProcessName("TRP");
                    processorReport.setStatus("Y");
                    processorReport.setProcessDate(String.valueOf(LocalDate.now()));
                    processorReport.setTotalRows(i.longValue());
                    processorReport.setAlive("Y");
                    processorReport.setSourceFile(String.valueOf(source));
                    String start = StringUtils.substringBefore(processorReport.getSourceFile(), "_");
                    String processNme = StringUtils.substringAfter(processorReport.getSourceFile(), "_");
                    String processNAme1 = StringUtils.substringBefore(processNme, ".");
                    logger.info("1234321:::::::::" +processNAme1);
                    processorReport.setProcessName(processNAme1);
                    key.setProcessID(start);
                    var producer = createProducer();
                    var record = new ProducerRecord<>(Topic.PROCESS_RESPONSE.getName(), key, processorReport);
                    producer.send(record);
                    Path folder = Paths.get("TradeReconProcess/input_csv/processed/ProcessedFile.csv");
                    try {
                        Files.move(file, folder, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    logger.info("CSV file processed successfully {}", file);
                    future.complete(null);
                })
                .subscribe();

        return future;
    }

    public Producer<ProcessorReportKey, ProcessorReport> createProducer() {
        return new KafkaProducer<>(
                PropertiesProvider.KAFKA_PRODUCER.getProperties(), serdesFactory.createAvroSerde(ProcessorReportKey.class).serializer(),
                serdesFactory.createAvroSerde(ProcessorReport.class).serializer());
    }

    protected abstract Class<K> getKeyClass();

    protected abstract Class<V> getValueClass();

    protected abstract ProducerRecord<K, V> createProducerRecord(V value);
}
