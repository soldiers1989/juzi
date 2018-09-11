package com.jzfq.retail.common.enmu;

/**
 * GPS校验标志
 * @author caishijian
 * @version V1.0
 * @date 2018年07月30日 12:57
 */
public enum GpsCheckFlag {

    SELLER_LOGIN("LOGIN", "商户登录"),
    SELLER_CREATE_ORDER("CREATE", "商户创建订单"),
    USER_REGISTER_AND_AUTHENTICATION("AUTH", "用户注册认证"),
    USER_CONFIRM_ORDER("CONFIRM", "用户确认订单"),
    USER_PAY_ORDER("PAY", "用户支付订单")
    ;

    private String flag;

    private String description;

    GpsCheckFlag(String flag, String description) {
        this.flag = flag;
        this.description = description;
    }

    public String getFlag() {
        return flag;
    }

    public String getDescription() {
        return description;
    }

    public static GpsCheckFlag getEnum(String flag){
        for (GpsCheckFlag gpsCheckFlag : GpsCheckFlag.values()) {
            if (gpsCheckFlag.getFlag().equals(flag)) {
                return gpsCheckFlag;
            }
        }
        return null;
    }
}
