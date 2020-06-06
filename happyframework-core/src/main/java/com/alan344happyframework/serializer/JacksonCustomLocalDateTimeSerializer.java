package com.alan344happyframework.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * @author ：AlanSun
 * @date ：2018/7/25 21:59
 */
public class JacksonCustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    public JacksonCustomLocalDateTimeSerializer() {
        super();
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator g, SerializerProvider provider) throws IOException {
        g.writeString(value.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli() + "");
    }
}
