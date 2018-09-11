package com.jzfq.retail.common.enmu;

/**
 * @Author MaoLixia
 * @Date 2018/8/22 14:28
 * 商户店铺类型
 */
public enum SellerStoreTypeEnum {
    STORE_TYPE_2("2", "2S", ""),
    STORE_TYPE_3("3", "4S", ""),
    STORE_TYPE_4("4", "POP商户", ""),
    STORE_TYPE_5("5", "线下商户", "扫码店"),
    STORE_TYPE_6("6", "便利店", "店中店"),
    STORE_TYPE_7("7", "车主白条", "车主白条");

    private String code;
    private String message;
    private String frontMessage;

    SellerStoreTypeEnum(String code, String message, String frontMessage) {
        this.code = code;
        this.message = message;
        this.frontMessage = frontMessage;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getFrontMessage() {
        return frontMessage;
    }

    public static String getFrontMessage(String code){
        for (SellerStoreTypeEnum str : SellerStoreTypeEnum.values()){
            if(code.equals(str.getCode())){
                return str.getFrontMessage();
            }
        }
        return null;
    }
}
