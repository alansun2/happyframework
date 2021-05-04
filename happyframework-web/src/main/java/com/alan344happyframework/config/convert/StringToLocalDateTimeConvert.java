package com.alan344happyframework.config.convert;

import com.alan344happyframework.constants.SeparatorConstants;
import com.alan344happyframework.exception.BizException;
import com.alan344happyframework.util.DateUtils;
import com.alan344happyframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author AlanSun
 * @date 2019/8/26 17:26
 */
@Slf4j
public class StringToLocalDateTimeConvert implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(@Nullable String source) {
        if (!StringUtils.isEmpty(source)) {
            LocalDateTime localDateTime;
            try {
                if (!source.contains(SeparatorConstants.COLON)) {
                    localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(source)), ZoneId.systemDefault());
                } else {
                    localDateTime = LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DateUtils.DATE_FORMAT));
                }
            } catch (Exception e) {
                log.error(source + "：时间格式错误，请传入毫秒时间戳或yyyy-MM-dd HH:mm:ss，第一选择是毫秒时间戳");
                throw new BizException(source + "：时间格式错误，请传入毫秒时间戳或yyyy-MM-dd HH:mm:ss，第一选择是毫秒时间戳");
            }
            return localDateTime;
        }
        return null;
    }
}
