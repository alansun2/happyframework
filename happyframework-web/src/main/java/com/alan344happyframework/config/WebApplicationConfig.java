package com.alan344happyframework.config;

import com.alan344happyframework.config.convert.StringToLocalDateConvert;
import com.alan344happyframework.config.convert.StringToLocalDateTimeConvert;
import com.alan344happyframework.properties.SystemProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * @author AlanSun
 * @date 2020/7/16 17:41
 * <p>
 * web 配置
 **/
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class WebApplicationConfig implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SystemProperties systemProperties;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML));
        converter.setObjectMapper(objectMapper);
        converters.add(systemProperties.getJsonMappingIndex(), converter);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToLocalDateTimeConvert());
        registry.addConverter(new StringToLocalDateConvert());
    }
}
