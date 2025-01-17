//package com.quod.bo.tradereconciliationprocess.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.quod.bo.tradereconciliationprocess.model.TradeReconMessage;
//import com.quod.bo.tradereconciliationprocess.model.Transaction;
//import com.quod.bo.tradereconciliationprocess.broker.producer.ProcessResponseProducer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.SqlParameterSource;
//import org.springframework.jdbc.core.simple.SimpleJdbcCall;
//import org.springframework.stereotype.Service;
//
//import java.text.ParseException;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
///**
// * @author - tra865
// * @date - 10-05-2022
// */
//@Service
//public class TransactionServiceImpl implements TransactionService {
//    private final static Logger LOG = LoggerFactory.getLogger(TransactionServiceImpl.class);
//
////        @PersistenceContext
////    private EntityManager em;
//
////    @Autowired
////    private DataSource dataSource;
//  private JdbcTemplate jdbcTemplate;
//    private SimpleJdbcCall simpleJdbcCall;
////
////    @PostConstruct
//
//    public TransactionServiceImpl(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
////    private void postConstruct() {
////        jdbcTemplate = new JdbcTemplate(dataSource);
////        jdbcTemplate.setResultsMapCaseInsensitive(true);
////    }
//
////private EntityManager em;
//
////    @PersistenceContext
////    public void setEntityManager(final EntityManager entityManager){
////        this.em = entityManager;
////    }
//
//    @Autowired
//    ProcessResponseProducer processResponseProducer;
//
//    @Override
//    public List<Transaction> findTransactionByVenueIdAndDate(String tranDate, String venueId) {
////        LOG.info("tranDate: {}, venueId: {}", tranDate, venueId);
////        LOG.info("entity manager " + em);
////        var txnList = findAllTransaction();
////        //LOG.info("TRANSACTION MESSAGE!!!" + txnList);
////        var txn = txnList.stream()
////                .filter(c -> c.getVenueId().equals(venueId) && c.getTranDate().equals(LocalDate.parse(tranDate)))
////                .collect(Collectors.toList());
////        //LOG.info("TRANSACTION " + txn);
////        return txn;
//          return null;
//    }
//
//    @Override
//    public List<Transaction> findAllTransaction() {
////        Query query = em.createNamedQuery("getAllTransaction");
////        List<Transaction> txnList = query.getResultList();
////        return txnList;
//        return null;
//    }
//
//    @Override
//    public List<TradeReconMessage> findAllTransactionTemp() {
////        LOG.info("entity manager!!!!: " + em);
////        Query query = em.createNamedQuery("findByVenueIdAndDateTranTemp");
////        List<TradeReconMessage> txnTmpList = query.getResultList();
////        //txnTmpList.size();
////
////        return txnTmpList;
//         return null;
//    }
//
//    @Override
//    public List<TradeReconMessage> findTransactionTempByVenueIdByDate(String tranDate, String venueId) {
////        LOG.info("tranDate: {}, venueId: {}", tranDate, venueId);
////        var txnTempList = findAllTransactionTemp();
////        //LOG.info("TRANSACTION TEMP!!!" + txnTempList);
////        var txn = txnTempList.stream()
////                .filter(c -> c.getVenueId().equals(venueId) && c.getTranDate().equals(tranDate))
////                .collect(Collectors.toList());
////        return txn;
//        LOG.info("tradeReconmessage sql :" + simpleJdbcCall);
//        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
//                .withCatalogName("transactiontmp_pkg")
//                .withFunctionName("transactiontmp_r")
//                .returningResultSet("ref_output",
//                        BeanPropertyRowMapper.newInstance(TradeReconMessage.class));
//        LOG.info("tradeReconmessage sql :" + simpleJdbcCall);
//        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
//                .addValue("p_TransactionNo", null)
//                .addValue("p_TranDate", "2022-09-05")
//                .addValue("p_VenueId", "NSE")
//                .addValue("p_SttlNo", null)
//                .addValue("p_AccountGroupId", null)
//                .addValue("p_LocationId", null)
//                .addValue("p_VenueScripId", null)
//                .addValue("p_InstrId", null)
//                .addValue("p_BuySell", null)
//                .addValue("p_Qty", null)
//                .addValue("p_SignedQty", null)
//                .addValue("p_Rate", null)
//                .addValue("p_OrderNo", null)
//                .addValue("p_TradeNo", null)
//                .addValue("p_TradeTime", null)
//                .addValue("p_ContractNoteNo", null)
//                .addValue("p_ModifyCount", null)
//                .addValue("p_TransactionType", null)
//                .addValue("p_OrderType", null)
//                .addValue("p_OrderTime", null)
//                .addValue("p_TradedCode", null)
//                .addValue("p_Channel", null)
//                .addValue("p_LastUpdatedOn", null)
//                .addValue("p_Euser", null)
//                .addValue("p_ManualEntry", null)
//                .addValue("p_InstitutionId", null)
//                .addValue("p_Remarks", null)
//                .addValue("p_TradeStatus", null)
//                .addValue("p_CurrencyCode", null)
//                .addValue("p_Alive", null)
//                .addValue("p_ProcStatus", null);
//        var tradeReconMessageList = simpleJdbcCall.executeFunction(List.class, sqlParameterSource);
//        LOG.info("File has been uploaded"+tradeReconMessageList);
//        if (tradeReconMessageList == null) {
//            return Collections.emptyList();
//        } else {
//            LOG.info("TradeRecon:" + tradeReconMessageList);
//            return tradeReconMessageList;
//        }
//
//
//    }
//
//
//    @Override
//    public List<TradeReconMessage> findMissMatches(String tranDate, String venueId) throws ParseException, JsonProcessingException {
//        LOG.info("Finding Miss matches:");
//        long start = System.currentTimeMillis();
//        var txnTempList = findTransactionTempByVenueIdByDate(tranDate, venueId);
//        var txnTempCount = txnTempList.size();
//        var txnList = findTransactionByVenueIdAndDate(tranDate, venueId);
//        var txnCount = txnList.size();
//
//        LOG.info("TRANSACTION SIZE  :" + txnCount);
//        LOG.info("TRANSACTIONTEMP SIZE  :" + txnTempCount);
////        LOG.info("TRANSACTIONTEMP field  :" + txnTempList);
////        LOG.info("TRANSACTION field  :" + txnList);
//
//        if (txnTempCount > txnCount) {
//            if (txnCount == 0) {
//                // return by flagging all transaction as mismatched
//                LOG.info("return by flagging all transaction as mismatched");
//
//            } else {
//
//                //find mismatches with trade number
//
//                List<TradeReconMessage> missMatches = txnTempList.stream().filter(two -> txnList.stream()
//                                .noneMatch(one -> one.getTradeNo().equals(two.getTradeNo())))
//                        .collect(Collectors.toList());
//                LOG.info("TRANSACTION field  :" + missMatches);
//
//                //var missMatchesToDb = addMissMatches(missMatches);
//                long stop = System.currentTimeMillis();
//                LOG.info("start time:" + start);
//                LOG.info("stop time:" + stop);
//                LOG.info("Total Time taken to complete process:" + (stop - start) / 1000);
//                LOG.info(" //find mismatches with trade number");
//                return missMatches;
//            }
//        } else {
//            // check volume
//            var txnVolume = Optional.of(txnList.stream()
//                    .map(a -> a.getQty() * (a.getRate()))
//                    .reduce(0, Integer::sum));
//            LOG.info("TRANSACTION VOLUME  :" + txnVolume.get());
//
//            var txnTmpVolume = Optional.of(txnTempList.stream()
//                    .map(a -> a.getQty() * (a.getRate()))
//                    .reduce(0L, Long::sum));
//            LOG.info("TRANSACTIONTMP  VOLUME  :" + txnTmpVolume.get());
//
//        }
//        return null;
//    }
//
////    @Transactional
////    public TradeReconMessage addMissMatches(List<TradeReconMessage> tradeReconMessages) throws ParseException, JsonProcessingException {
////
////        LOG.info("Zoned date time: " + ZonedDateTime.now());
////        LOG.info("Date time: " + LocalDateTime.now());
////        LOG.info("Date: " + LocalDate.now());
////        LOG.info("Parsed date time: " + LocalDateTime.parse("2007-03-06T11:11:10.411"));
//////        LOG.info("Parsed Zoned date time: " + ZonedDateTime.parse(tradeReconMessages.get().getTradeTime()).getZone());
//////        LOG.info("Parsed Zoned date time offset: " + ZonedDateTime.parse(tradeReconMessages.get().getTradeTime()).getOffset());
////        for (TradeReconMessage trm : tradeReconMessages) {
////            StoredProcedureQuery query = em.createNamedStoredProcedureQuery("addMissMatch");
////            query.setParameter("p_TransactionNo", trm.getTransactionNo());
////            String tranDate = trm.getTranDate();
////            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
////            LocalDate localDate = LocalDate.parse(tranDate, formatter);
////            query.setParameter("p_TranDate", localDate);
////            query.setParameter("p_VenueId", trm.getVenueId());
////            query.setParameter("p_SttlNo", trm.getSttlNo());
////            query.setParameter("p_AccountGroupId", trm.getAccountGroupId());
////            query.setParameter("p_LocationId", trm.getLocationId());
////            query.setParameter("p_VenueScripId", trm.getVenueScripId());
////            query.setParameter("p_InstrId", trm.getInstrId());
////            query.setParameter("p_BuySell", trm.getBuySell());
////            query.setParameter("p_Qty", trm.getQty());
////            query.setParameter("p_SignedQty", trm.getSignedQty());
////            query.setParameter("p_Rate", trm.getRate());
////            query.setParameter("p_OrderNo", trm.getOrderNo());
////            query.setParameter("p_TradeNo", trm.getTradeNo());
////            query.setParameter("p_TradeTime", ZonedDateTime.parse(trm.getTradeTime()));
////            query.setParameter("p_ContractNoteNo", trm.getContractNoteNo());
////            query.setParameter("p_ModifyCount", trm.getModifyCount());
////            query.setParameter("p_TransactionType", trm.getTransactionType());
////            query.setParameter("p_OrderType", trm.getOrderType());
////            query.setParameter("p_OrderTime", ZonedDateTime.parse(trm.getOrderTime()));
////            query.setParameter("p_TradedCode", trm.getTradedCode());
////            query.setParameter("p_Channel", trm.getChannel());
////            query.setParameter("p_LastUpdatedOn", ZonedDateTime.now());
////            query.setParameter("p_Euser", trm.getEUser());
////            query.setParameter("p_ManualEntry", trm.getManualEntry());
////            query.setParameter("p_InstitutionId", trm.getInstitutionId());
////            query.setParameter("p_Remarks", trm.getRemarks());
////            query.setParameter("p_TradeStatus", trm.getTradeStatus());
////            query.setParameter("p_CurrencyCode", trm.getCurrencyCode());
////            query.setParameter("p_Alive", trm.getAlive());
////            query.setParameter("p_ProcStatus", "Y");
////
////            LOG.info("MISSMATCHES ADDED TO TABLE TO TABLE");
////
////            LOG.info("MISSMATCHES exec = " + query.executeUpdate());
////            ProcessRequestResponse response = new ProcessRequestResponse();
////            response.setMessage("MISS MATCHES ADDED TO DB");
////            response.setFileName("txnupdated.csv");
////            response.setVenueId(trm.getVenueId());
////            response.setDate(trm.getTranDate());
////            response.setNumberOfTradesInSystem("2");
////            response.setNumberOfTradesUploaded("4");
////            response.setMismatches(String.valueOf(tradeReconMessages.size()));
////            processResponseProducer.sendMessage(response);
////
////            return null;
////
////        }
////        return null;
////    }
//
//
//}
