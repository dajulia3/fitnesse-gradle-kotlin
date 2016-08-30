package com.djulia.transactions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

public class JsonHelpers {

    public static String serializeContentForMvcTest(Object value){
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
