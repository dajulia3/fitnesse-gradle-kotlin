package com.djulia.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class ResultCapturingMessageConverter implements HttpMessageConverter<Object> {
    private Optional<?> result;

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.ALL);
    }

    @Override
    public Object read(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
       return new ObjectMapper().readValue(inputMessage.getBody(), clazz);
    }

    @Override
    public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        result = Optional.of(o);
//        new ObjectMapper().writeValue(outputMessage.getBody(), o);
    }

    public Object getResult() {
        return result.orElseThrow(
                () -> new RuntimeException("Oops." +
                        "Are you sure you didn't forget to invoke the controller method in MockMvc?")
        );
    }
}
