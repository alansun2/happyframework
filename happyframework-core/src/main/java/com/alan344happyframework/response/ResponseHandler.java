package com.alan344happyframework.response;

import com.alan344happyframework.exception.BizException;
import com.alan344happyframework.util.StringUtils;

/**
 * @author ：AlanSun
 * @date ：2018/7/29 18:07
 */
public class ResponseHandler {

    public static <T> T responseHandler(AbstractResponse<T> response) {
        if (null == response) {
            throw new BizException("系统更新中，请稍后再试");
        }
        if (!StringUtils.isEmpty(response.getResultMessage())) {
            throw new BizException(response.getResultMessage());
        }
        return response.getData();
    }
}