package com.alan344happyframework.bean.response;


import vip.tuoyang.base.exception.BizException;

/**
 * @author ：AlanSun
 * @date ：2018/7/29 18:07
 */
public class ResponseHandler {

    public static <T> T responseHandler(CommonResponse<T> response) {
        if (null == response) {
            throw new BizException("第三方系统更新中，请稍后再试");
        }

        if (response.getCode() != CommonResponse.COMMON_SUCCESS) {
            throw new BizException(response.getMessage());
        }
        return response.getData();
    }

    public static <T> T responseHandler(CommonResponse<T> response, String errorMessage) {
        if (null == response) {
            throw new BizException(errorMessage);
        }

        if (response.getCode() != CommonResponse.COMMON_SUCCESS) {
            throw new BizException(errorMessage + ": " + response.getMessage());
        }
        return response.getData();
    }
}
