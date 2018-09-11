package com.jzfq.retail.common.enmu;

import org.apache.commons.lang3.StringUtils;

/**
 * 请求失败重试类型状态码
 * @author liuwei
 */
public enum OrderStatus {

    ORDER_STATE_100(100, "待确认"),
    ORDER_STATE_110(110, "用户已确认,待支付"),
    ORDER_STATE_120(120, "待支付"),
//    ORDER_STATE_130(130, "交易复核中"),//支付成功
//    ORDER_STATE_135(135, "交易复核中"),//支付成功且风控进件成功
    ORDER_STATE_130(130, "支付成功且未推风控"),//支付成功
    ORDER_STATE_135(135, "支付成功且风控进件成功"),//支付成功且风控进件成功
    ORDER_STATE_140(140, "交易复核通过，待资匹"),
    ORDER_STATE_150(150, "交易复核失败"),
    ORDER_STATE_155(155, "资匹成功，待交货"),
    ORDER_STATE_160(160, "已收货，分期还款中"),
    ORDER_STATE_170(170, "已收货，分期还款中"),
    ORDER_STATE_180(180, "已完成"),
    ORDER_STATE_200(200, "已取消"),
    ORDER_STATE_210(210, "退货处理中"),
    ORDER_STATE_220(220, "已退货"),
    ORDER_STATE_230(230, "退货失败");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    OrderStatus(int code, String message) {
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

    public static String getMsgByCode(Integer code){
        OrderStatus[] orderStatuss = OrderStatus.values();
        if(code == null){
            return null;
        }
        for(OrderStatus orderStatus : orderStatuss){
            if(orderStatus.code == code){
                return orderStatus.getMessage();
            }
        }
        return null;
    }
}
