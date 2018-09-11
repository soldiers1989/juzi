package com.jzfq.retail.common.enmu;

/**
 * 字典表
 * @author liuwei
 */
public enum DictionaryVal {

    OPEN("OPEN", "开启位置校验空"),
    CLOSE("CLOSE", "关闭位置校验");

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回结果描述
     */
    private String message;

    DictionaryVal(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
