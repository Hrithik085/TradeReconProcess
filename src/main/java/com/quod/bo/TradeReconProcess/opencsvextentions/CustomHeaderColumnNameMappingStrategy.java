package com.quod.bo.TradeReconProcess.opencsvextentions;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvBeanIntrospectionException;
import com.opencsv.exceptions.CsvChainedException;
import com.opencsv.exceptions.CsvFieldAssignmentException;

import java.util.Arrays;

public class CustomHeaderColumnNameMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

        @Override
        public T populateNewBean(String[] line) throws CsvBeanIntrospectionException, CsvFieldAssignmentException, CsvChainedException
        {
            Arrays.setAll(line, i -> {
                String source = line[i].trim();
                if (fieldMap.get(headerIndex.getHeaderIndex()[i].toUpperCase()).getField().getAnnotation(CsvUpperCase.class) != null) {
                    source = source.toUpperCase();
                }
                return source;
            });
            return super.populateNewBean(line);
        }
}
