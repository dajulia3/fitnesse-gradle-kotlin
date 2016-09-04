package com.djulia.banking.transactions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

fun makeDefaultObjectMapper() : ObjectMapper {
    return ObjectMapper().registerKotlinModule()
}