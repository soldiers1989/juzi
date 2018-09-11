package com.jzfq.retail.common.enmu;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月28日 15:07
 * @Description: 分类-品牌-城市-区间价 状态
 */
public enum ProductCateBrandAreasStatus {

    CREATE(0, "待审核"),
    AUDITING_SUCCESS(1, "审核通过"),
    AUDITING_FAIL(2, "审核失败");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    ProductCateBrandAreasStatus(int code, String message) {
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
