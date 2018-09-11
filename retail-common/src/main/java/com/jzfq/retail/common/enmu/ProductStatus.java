package com.jzfq.retail.common.enmu;

/**
 * @Title: ProductStatus
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月10日 10:42
 * @Description: 产品状态 1、刚创建；2、提交审核；3、审核通过；4、申请驳回；5、商品删除；6、上架；7、下架
 */
public enum ProductStatus {

    CREATE(1, "刚创建"),
    UNDER_REVIEW(2, "提交审核"),
    AUDITING_SUCCESS(3, "审核通过"),
    AUDITING_FAIL(4, "申请驳回"),
    DELETE(5, "商品删除"),
    EFFECTIVE(6, "上架"),
    INVALID(7,"下架");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    ProductStatus(int code, String message) {
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

    public static ProductStatus getEnum(Integer code){
        for (ProductStatus productStatus : ProductStatus.values()) {
            if (productStatus.getCode() == code) {
                return productStatus;
            }
        }
        return null;
    }

    public static ProductStatus getEnum(String message){
        for (ProductStatus productStatus : ProductStatus.values()) {
            if (productStatus.getMessage().equals(message)) {
                return productStatus;
            }
        }
        return null;
    }

}
