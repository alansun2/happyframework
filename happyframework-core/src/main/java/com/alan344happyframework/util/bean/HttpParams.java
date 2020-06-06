package com.alan344happyframework.util.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.message.BasicHeader;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class HttpParams {
    private String url;
    private BasicHeader[] headers;
    private String strEntity;
}
