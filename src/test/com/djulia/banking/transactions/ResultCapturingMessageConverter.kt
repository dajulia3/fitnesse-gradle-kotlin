package com.djulia.banking.transactions

import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import java.io.IOException

internal class ResultCapturingMessageConverter : HttpMessageConverter<Any> {
    private var result: Any? = null

    override fun canRead(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return true
    }

    override fun canWrite(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return true
    }

    override fun getSupportedMediaTypes(): List<MediaType> {
        return listOf(MediaType.ALL)
    }

    @Throws(IOException::class, HttpMessageNotReadableException::class)
    override fun read(clazz: Class<*>, inputMessage: HttpInputMessage): Any {
        return makeDefaultObjectMapper().readValue(inputMessage.body, clazz)
    }

    @Throws(IOException::class, HttpMessageNotWritableException::class)
    override fun write(o: Any, contentType: MediaType, outputMessage: HttpOutputMessage) {
        result = o
    }

    fun getResult(): Any {
        return result ?: throw RuntimeException("Oops." + "Are you sure you didn't forget to invoke the controller method in MockMvc?")
    }
}
