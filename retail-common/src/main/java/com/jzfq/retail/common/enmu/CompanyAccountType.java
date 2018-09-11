package com.jzfq.retail.common.enmu;

/**
 * @Title: CompanyAccountType
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月10日 10:31
 * @Description:
 */
public enum CompanyAccountType {

    TO_PUBLIC(1, "对公账户"),

    TO_PRIVATE(2, "个人账户");


    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    CompanyAccountType(int code, String message) {
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
