package com.example.task.configuration;

import com.example.task.domain.BusinessCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectReader paymentObjectReader(ObjectMapper objectMapper) {
        return objectMapper.readerFor(BusinessCard.class);
    }


}
