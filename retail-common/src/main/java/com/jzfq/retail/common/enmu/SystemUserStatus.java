package com.jzfq.retail.common.enmu;

/**
 * 系统用户状态码
 * @author lw
 */
public enum SystemUserStatus {
    NORMAL(1, "正常"),
    FROZEN(2, "冻结"),
    DELETE(3, "删除");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    SystemUserStatus(int code, String message) {
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

    public static SystemUserStatus getEnum(Integer code){
        for (SystemUserStatus systemUserStatus : SystemUserStatus.values()) {
            if (systemUserStatus.getCode() == code) {
                return systemUserStatus;
            }
        }
        return null;
    }
}
