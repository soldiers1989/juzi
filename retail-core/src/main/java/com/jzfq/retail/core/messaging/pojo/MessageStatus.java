package com.jzfq.retail.core.messaging.pojo;

/**
 * @author lagon
 * @time 2016/12/26 14:38
 * @description 消息状态枚举
 */
public enum MessageStatus {

    SUCCESS("0","调用成功"),
    FAILURE("-1","调用失败"),
    UNKNOWN("-2","未知错误"),
    ERROR("-3","系统异常"),
    AUTH_FAILURE("100","无访问权限"),
    INVALID_REQUEST("101","非法请求"),
    INTERNAL_SERVER_ERROR("103","服务器内部错误");

    private String code;
    private String message;

    MessageStatus(String code, String message) {
        this.code=code;
        this.message=message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage(Object... params){
        return String.format(message, params);
    }

    @Override
    public String toString() {
        return code+":"+message;
    }

}

