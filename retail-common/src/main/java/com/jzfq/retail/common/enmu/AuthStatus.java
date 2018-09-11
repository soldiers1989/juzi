package com.jzfq.retail.common.enmu;

/**
 * 认证中心认证状态
 * @author liuwei
 */
public enum AuthStatus {

    ORDER_SOURCE_0(0, "未认证"),
    ORDER_SOURCE_1(1, "认证审核中"),
    ORDER_SOURCE_2(2, "认证审核成功"),
    ORDER_SOURCE_3(3, "认证审核失败");
    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    AuthStatus(int code, String message) {
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
