package com.quod.bo.TradeReconProcess.opencsvextentions;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.MappingStrategy;

import java.io.Reader;

public class CustomCsvToBeanBuilder<T>  extends CsvToBeanBuilder<T> {

    public CustomCsvToBeanBuilder(Reader reader, Class<T> valueClass) {
        super(reader);
        MappingStrategy<T> mappingStrategy = new com.quod.bo.TradeReconProcess.opencsvextentions.CustomHeaderColumnNameMappingStrategy<>();
        withMappingStrategy(mappingStrategy);
        mappingStrategy.setType(valueClass);
    }
}
