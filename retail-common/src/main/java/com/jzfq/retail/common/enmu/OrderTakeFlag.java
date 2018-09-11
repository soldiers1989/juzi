package com.jzfq.retail.common.enmu;

/**
 * @Title: OrderTakeFlag
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月04日 10:56
 * @Description: 订单邮寄方式
 */
public enum OrderTakeFlag {
    EXTRACTION(0, "自提"),
    MAILING(1, "邮寄");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    OrderTakeFlag(int code, String message) {
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
