package com.jzfq.retail.common.enmu;

/**
 * 订单来源 order_base source
 * @author liuwei
 */
public enum OrderSource {

    ORDER_SOURCE_1(1, "PC"),
    ORDER_SOURCE_2(2, "H5"),
    ORDER_SOURCE_3(3, "IOS"),
    ORDER_SOURCE_4(4, "ANDROID"),
    ORDER_SOURCE_5(5, "商户小程序");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    OrderSource(int code, String message) {
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
