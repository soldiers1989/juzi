package com.jzfq.retail.common.enmu;

/**
 * @Title: SellerStatus
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年06月29日 14:15
 * @Description: 商户状态枚举类型
 */
public enum SellerStatus {


    OPEN_ACCOUNT_FAILED(1, "开户失败"),
    OPEN_ACCOUNT_SUCCESS(2, "开户成功"),
    ACCOUNT_FROZEN(3, "冻结");
    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    SellerStatus(int code, String message) {
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

    public static boolean checkStatus(int code) {
        if (code < 0) {
            return false;
        }
        boolean statusInside = false;
        for (SellerStatus status : SellerStatus.values()) {
            if (code == status.getCode()) {
                statusInside = true;
                break;
            }
        }
        return statusInside;
    }
}
