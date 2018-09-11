package com.jzfq.retail.common.enmu;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月27日 16:34
 * @Description: 商品品牌审核状态
 */
public enum SellerCompayStatus {

    UNDER_REVIEW(1,"提交申请"),
    AUDIT_SUCCESS(2,"审核通过"),
    PAY_DEPOSIT(3,"删除"),
    AUDIT_FAILURE(4,"审核失败");


    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    SellerCompayStatus(int code, String message) {
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
