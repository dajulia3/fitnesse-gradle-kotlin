package com.djulia.banking.api.transactions

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

import java.math.BigDecimal

object JsonHelpers {

    val objectMapper = ObjectMapper().registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule())

    fun serializeContentForMvcTest(value: Any): String {
        try {
            return objectMapper.writeValueAsString(value)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }

    }
}
