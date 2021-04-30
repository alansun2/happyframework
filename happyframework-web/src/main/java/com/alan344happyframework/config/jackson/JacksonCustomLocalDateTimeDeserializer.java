package com.alan344happyframework.config.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author ：AlanSun
 * @date ：2018/7/29 16:50
 */
public class JacksonCustomLocalDateTimeDeserializer extends JSR310DateTimeDeserializerBase<LocalDateTime> {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public JacksonCustomLocalDateTimeDeserializer(DateTimeFormatter formatter) {
        super(LocalDateTime.class, formatter);
    }

    protected JacksonCustomLocalDateTimeDeserializer(JacksonCustomLocalDateTimeDeserializer jacksonCustomLocalDateTimeDeserializer, boolean leniency) {
        super(jacksonCustomLocalDateTimeDeserializer, leniency);
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(parser.getNumberValue().longValue()), ZoneId.systemDefault());
        }

        if (parser.hasTokenId(JsonTokenId.ID_STRING)) {
            String string = parser.getText().trim();
            if (string.length() == 0) {
                return null;
            }

            try {
                if (_formatter == DEFAULT_FORMATTER) {
                    // JavaScript by default includes time and zone in JSON serialized Dates (UTC/ISO instant format).
                    if (string.length() > 10 && string.charAt(10) == 'T') {
                        if (string.endsWith("Z")) {
                            return LocalDateTime.ofInstant(Instant.parse(string), ZoneOffset.UTC);
                        } else {
                            return LocalDateTime.parse(string, DEFAULT_FORMATTER);
                        }
                    }
                }

                return LocalDateTime.parse(string, _formatter);
            } catch (DateTimeException e) {
                return _handleDateTimeException(context, e, string);
            }
        }
        return _handleUnexpectedToken(context, parser, "Expected array or string.");
    }

    @Override
    protected JSR310DateTimeDeserializerBase<LocalDateTime> withDateFormat(DateTimeFormatter dateTimeFormatter) {
        return new JacksonCustomLocalDateTimeDeserializer(dateTimeFormatter);
    }

    @Override
    protected JacksonCustomLocalDateTimeDeserializer withLeniency(Boolean leniency) {
        return new JacksonCustomLocalDateTimeDeserializer(this, leniency);
    }

    @Override
    protected JacksonCustomLocalDateTimeDeserializer withShape(JsonFormat.Shape shape) {
        return this;
    }
}
