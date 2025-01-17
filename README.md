Trade Project
Technologies Used: Java Spring Boot, Java 11, Apache Kafka, Reactor Core, Kafka Streams, Kafka Connect

Description:
The Trade Project is a high-performance application designed for real-time processing of large-scale data. The project efficiently reads CSV files line by line and publishes the data to a Kafka topic, enabling seamless integration with downstream systems.

Key Features:
High-Speed Data Ingestion:

The application uses Reactor Core's Flux for non-blocking, asynchronous processing of CSV files, ensuring high throughput and low latency.
Supports processing millions of records in milliseconds.
Stream Processing with Kafka Streams:

Data from the Kafka topic is consumed by a KStream, where it undergoes transformation and enrichment.
Enrichment is performed using a GlobalKTable, which allows real-time lookups to add additional context or metadata to the processed records.
Data Persistence:

The enriched data is published to another Kafka topic.
A Kafka Sink Connector is configured to save the final data into a database, enabling further analytics or reporting.
Scalable Architecture:

Built on Apache Kafka, the project supports horizontal scaling to handle large workloads efficiently.
Designed to manage distributed data processing with fault tolerance and reliability.
Batch and Real-Time Flexibility:

The application supports both batch and real-time processing, making it versatile for various use cases.
Advantages:
Performance: Handles bulk data processing in milliseconds with minimal resource consumption.
Data Enrichment: The use of GlobalKTable ensures real-time enrichment during stream processing.
End-to-End Integration: From ingestion to transformation to persistence, the entire pipeline is automated.
Flexibility: Supports multiple data formats and easily integrates with different Kafka topics and connectors.
Use Cases:
High-volume trade data ingestion and processing.
Real-time data pipelines for financial, logistics, and IoT applications.
Bulk data migration with transformation and enrichment.
This project demonstrates the seamless integration of reactive programming, streaming, and database persistence for high-performance, scalable, and reliable data pipelines.
