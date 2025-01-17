package com.quod.bo.TradeReconProcess.storage;


import com.quod.bo.TradeReconProcess.broker.topics.Topic;
import com.quod.bo.TradeReconProcess.model.*;
import com.quod.bo.TradeReconProcess.properties.PropertiesProvider;
import com.quod.bo.TradeReconProcess.serialization.SerdesFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.sql.Types.*;

@Slf4j
@Service
public class StorageImpl implements Storage {
    @Autowired
    SerdesFactory serdesFactory;
    private final DataSource dataSource;

    public StorageImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void persistTransaction(TradeReconKey key, TradeRecon transaction) throws SQLException {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(transaction, "transaction");
        log.info("TRANSACTION KEY:::::::::: " + key);
        var sql = "INSERT INTO traderecon(ACCOUNTGROUPID, ALIVE, EXECUTIONDATETIME, INSTITUTIONID, INSTRID, INSTRTYPE" +
                ", INSTRUMENTNAME, INVESTORACCOUNT, INVESTORNAME, ISIN, LASTUPDATEDON, LISTINGID, LOCATIONID, MARKETTYPE, ORDERID, PRICE" +
                ", QTY, REMARKS, SIDE, SLABID, STTLNO, TRADESTATUS, TRANDATE, TRANSACTIONNO, UPDATED, VALUE, VENUEACTGRPNAME, VENUEID, VENUEINSTRID, VENUETRADEID, CURRENCYCODE)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var connection = dataSource.getConnection()) {
            try (var stmt = connection.prepareStatement(sql)) {
                setTransactionData(stmt, key, transaction);
                stmt.execute();
                if (!connection.getAutoCommit()) {
                    connection.commit();
                }
            } catch (SQLException e) {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
                throw e;
            }
        }
    }

    @Override
    public void processorReport(ProcessorReport transaction) throws SQLException {
        //   Objects.requireNonNull(key, "key");
      //  Objects.requireNonNull(transaction, "transaction");
         log.info(" processorReport::::::::: " + transaction);
        var sql = "INSERT INTO processorreport(ALIVE, LASTUPDATEDON, PROCESSDATE, PROCESSID, PROCESSNAME, REMARKS" +
                ", SOURCEFILE, STATUS, TOTALROWS" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (var connection = dataSource.getConnection()) {
            try (var stmt = connection.prepareStatement(sql)) {
                setProcessorData(stmt, transaction);
                stmt.execute();
                if (!connection.getAutoCommit()) {
                    connection.commit();
                }
            } catch (SQLException e) {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
                throw e;
            }
        }
    }

    private void setProcessorData(PreparedStatement stmt, ProcessorReport transaction) throws SQLException {
        if (transaction.getAlive() != null) {
            stmt.setString(1, "Y");
        } else {
            stmt.setNull(1, INTEGER);
        }
        stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

        stmt.setDate(3, Date.valueOf(LocalDate.now()));

        if (transaction.getProcessID() != null) {
            stmt.setString(4, transaction.getProcessID());
        } else {
            stmt.setNull(4, INTEGER);
        }
        if (transaction.getProcessName() != null) {
            stmt.setString(5, transaction.getProcessName());
        } else {
            stmt.setNull(5, INTEGER);
        }
        if (transaction.getRemarks() != null) {
            stmt.setString(6, transaction.getRemarks());
        } else {
            stmt.setNull(6, INTEGER);
        }
        if (transaction.getSourceFile() != null) {
            stmt.setString(7, transaction.getSourceFile());
        } else {
            stmt.setNull(7, INTEGER);
        }
        if (transaction.getStatus() != null) {
            stmt.setString(8, transaction.getStatus());
        } else {
            stmt.setNull(8, INTEGER);
        }
        if (transaction.getTotalRows() != null) {
            stmt.setLong(9, transaction.getTotalRows());
        } else {
            stmt.setNull(9, INTEGER);
        }

    }

    private void setTransactionData(PreparedStatement stmt, TradeReconKey key, TradeRecon transaction) throws SQLException {
        if (transaction.getAccountGroupId() != null) {
            stmt.setString(1, transaction.getAccountGroupId());
        } else {
            stmt.setNull(1, INTEGER);
        }
        if (transaction.getAlive() != null) {
            stmt.setString(2, transaction.getAlive());
        } else {
            stmt.setString(2, "Y");
        }
        if (transaction.getExcecutionDateTime() != null) {
            stmt.setString(3, transaction.getExcecutionDateTime());
        } else {
            stmt.setNull(3, INTEGER);
        }
        if (transaction.getInstitutionId() != null) {
            stmt.setLong(4, transaction.getInstitutionId());
        } else {
            stmt.setNull(4, INTEGER);
        }
        if (transaction.getInstrId() != null) {
            stmt.setString(5, transaction.getInstrId());
        } else {
            stmt.setNull(5, INTEGER);
        }
        if (transaction.getInstrType() != null) {
            stmt.setString(6, transaction.getInstrType());
        } else {
            stmt.setNull(6, INTEGER);
        }
        if (transaction.getInstrumentName() != null) {
            stmt.setString(7, transaction.getInstrumentName());
        } else {
            stmt.setNull(7, INTEGER);
        }
        if (transaction.getInvestorsAccount() != null) {
            stmt.setString(8, transaction.getInvestorsAccount());
        } else {
            stmt.setNull(8, INTEGER);
        }
        if (transaction.getInvestorName() != null) {
            stmt.setString(9, transaction.getInvestorName());
        } else {
            stmt.setNull(9, INTEGER);
        }
        if (transaction.getIsin() != null) {
            stmt.setString(10, transaction.getIsin());
        } else {
            stmt.setNull(10, INTEGER);
        }
        stmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));

        if (transaction.getListingId() != null) {
            stmt.setLong(12, transaction.getListingId());
        } else {
            stmt.setNull(12, INTEGER);
        }

        if (transaction.getLocationId() != null) {
            stmt.setLong(13, transaction.getLocationId());
        } else {
            stmt.setNull(13, INTEGER);
        }
        if (transaction.getMarketType() != null) {
            stmt.setString(14, transaction.getMarketType());
        } else {
            stmt.setNull(14, INTEGER);
        }
        if (transaction.getOrderID() != null) {
            stmt.setString(15, transaction.getOrderID());
        } else {
            stmt.setNull(15, INTEGER);
        }
        if (transaction.getPrice() != null) {
            stmt.setBigDecimal(16, transaction.getPrice());
        } else {
            stmt.setNull(16, INTEGER);
        }
        if (transaction.getQty() != null) {
            stmt.setLong(17, transaction.getQty());
        } else {
            stmt.setNull(17, INTEGER);
        }
        if (transaction.getRemarks() != null) {
            stmt.setString(18, transaction.getRemarks());
        } else {
            stmt.setNull(18, INTEGER);
        }
        if (transaction.getSide() != null) {
            stmt.setString(19, transaction.getSide());
        } else {
            stmt.setNull(19, INTEGER);
        }
        if (transaction.getSlabID() != null) {
            stmt.setString(20, transaction.getSlabID());
        } else {
            stmt.setNull(20, INTEGER);
        }
        if (transaction.getSttlNo() != null) {
            stmt.setLong(21, transaction.getSttlNo());
        } else {
            stmt.setNull(21, INTEGER);
        }
        if (transaction.getTradeStatus() != null) {
            stmt.setString(22, transaction.getTradeStatus());
        } else {
            stmt.setNull(22, INTEGER);
        }
        if (transaction.getTranDate() != null) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = null;
            try {
                date = sdf1.parse(transaction.getTranDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
            stmt.setDate(23, sqlStartDate);
        } else {
            stmt.setNull(23, DATE);
        }
        if (transaction.getTransactionNo() != null) {
            stmt.setString(24, key.getTransactionNo());
        } else {
            stmt.setNull(24, INTEGER);
        }
        if (transaction.getUpdated() != null) {
            stmt.setString(25, transaction.getUpdated());
        } else {
            stmt.setNull(25, INTEGER);
        }
        if (transaction.getValue() != null) {
            stmt.setBigDecimal(26, transaction.getValue());
        } else {
            stmt.setNull(26, INTEGER);
        }
        if (transaction.getVenueActGrpName() != null) {
            stmt.setString(27, transaction.getVenueActGrpName());
        } else {
            stmt.setNull(27, INTEGER);
        }
        if (transaction.getVenueId() != null) {
            stmt.setString(28, transaction.getVenueId());
        } else {
            stmt.setNull(28, INTEGER);
        }
        if (transaction.getVenueInstrID() != null) {
            stmt.setString(29, transaction.getVenueInstrID());
        } else {
            stmt.setNull(29, INTEGER);
        }
        if (transaction.getVenueTradeID() != null) {
            stmt.setLong(30, transaction.getVenueTradeID());
        } else {
            stmt.setNull(30, INTEGER);
        }
        if (transaction.getCurrencyCode() != null) {
            stmt.setString(31, transaction.getCurrencyCode());
        } else {
            stmt.setNull(31, INTEGER);
        }


    }


}