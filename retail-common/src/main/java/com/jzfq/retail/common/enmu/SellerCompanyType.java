package com.jzfq.retail.common.enmu;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月27日 16:34
 * @Description: 商品品牌审核状态
 */
public enum SellerCompanyType {

    SELF_SUPPORT(1,"自营"),
    MERCHANT_ENTRY(2,"商户入住");


    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    SellerCompanyType(int code, String message) {
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
