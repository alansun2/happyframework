package com.alan344happyframework.constants;

/**
 * @author AlanSun
 * @date 2020/8/13 9:24
 */
public enum BaseErrorCode implements ErrorCode {
    /**
     * 账号无效或未登录
     */
    UN_LOGIN(400, "账号无效或未登录"),
    /**
     * 登录过期，请重新登录
     */
    TOKEN_EXPIRED(401, "登录过期，请重新登录"),
    /**
     * token 不合法
     */
    TOKEN_ILLEGAL(402, "token 不合法"),
    /**
     * 权限不足
     */
    PERMISSION_DENIED(403, "权限不足"),
    /**
     * 系统繁忙，请稍后重试
     */
    SYSTEM_BUSY(411, "系统繁忙，请稍后重试"),
    /**
     * 请求参数有误
     */
    BAD_REQUEST(499, "请求参数有误"),
    /**
     * 未知异常，请联系管理员
     */
    INTERNAL_SERVER_ERROR(500, "未知异常，请联系管理员"),
    /**
     * 系统数据异常
     */
    SYS_DATA_ERROR(501, "系统数据异常"),


    /**
     * 新增失败
     */
    INSERT_ERROR(900, "新增失败"),
    /**
     * 更新失败，数据可能被删除，请刷新后重试
     */
    UPDATE_ERROR(901, "更新失败，数据可能被删除，请刷新后重试"),
    /**
     * 删除失败
     */
    DELETE_ERROR(903, "删除失败"),

    /**
     * param valid error
     */
    INTERNAL_PARAM_VALID_ERROR(1001, "param valid error"),
    /**
     * 数据不存在，或已被删除，请刷新页面
     */
    DATA_NULL_OR_DELETE(1002, "数据不存在，或已被删除，请刷新页面"),
    /**
     * 数据不存在
     */
    NULL_ERROR(1003, "数据不存在"),
    /**
     * 数据已被删除
     */
    ALREADY_DELETE_ERROR(1004, "数据已被删除"),
    ;

    private final int code;
    private final String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    BaseErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
