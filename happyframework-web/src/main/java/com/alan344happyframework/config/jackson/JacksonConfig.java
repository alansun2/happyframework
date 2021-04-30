package com.alan344happyframework.config.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.tuoyang.base.util.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author AlanSun
 * @date 2020/7/17 10:30
 */
@Configuration
public class JacksonConfig {

    @Bean
    @ConditionalOnMissingBean(value = ObjectMapper.class)
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new JacksonCustomLocalDateSerializer());
        javaTimeModule.addSerializer(LocalDateTime.class, new JacksonCustomLocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new JacksonCustomLocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtils.DATE_FORMAT)));
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}
