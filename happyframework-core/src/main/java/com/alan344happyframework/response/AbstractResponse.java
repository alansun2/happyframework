package com.alan344happyframework.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * @author AlanSun
 * @date 2017年3月17日 下午4:43:51
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public abstract class AbstractResponse<T> {
    /**
     * 数据
     */
    private T data;
    /**
     * 接口处理状态码
     */
    private Integer resultCode;
    /**
     * 返回信息
     */
    private String resultMessage;
    /**
     * 系统时间
     */
    private long systemTime = Instant.now().toEpochMilli();

    public AbstractResponse(int resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public AbstractResponse(T data) {
        this.data = data;
    }

    public AbstractResponse(int resultCode) {
        this.resultCode = resultCode;
    }

    public AbstractResponse(String resultMessage) {
        this.resultMessage = resultMessage;
    }
}
