package com.jzfq.retail.common.enmu;

/**
 * @Title: OrderType
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月06日 10:40
 * @Description: orderBase订单类型
 */
public enum OrderType {
    _1(1, "扫码店订单"),
    _2(2, "便利店订单");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    OrderType(int code, String message) {
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
