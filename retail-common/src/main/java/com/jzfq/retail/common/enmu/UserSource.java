package com.jzfq.retail.common.enmu;

/**
 * 订单来源 order_user  user source
 * @author liuwei
 */
public enum UserSource {

    USER_SOURCE_1(1, "新零售用户"),
    USER_SOURCE_0(0, "商城用户");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    UserSource(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
