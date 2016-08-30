package com.djulia.transactions

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

import java.math.BigDecimal

object JsonHelpers {

    public val objectMapper = ObjectMapper().registerModule(KotlinModule())

    fun serializeContentForMvcTest(value: Any): String {
        try {
            return objectMapper.writeValueAsString(value)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }

    }
}
