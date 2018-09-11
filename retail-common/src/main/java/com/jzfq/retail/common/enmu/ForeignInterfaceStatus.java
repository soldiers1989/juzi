package com.jzfq.retail.common.enmu;

/**
 * @Title: ForeignInterfaceType
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月10日 9:49
 * @Description: 对外接口日志 状态
 */
public enum ForeignInterfaceStatus {
    SEND(0,"已发送"),
    SUCCESS(1, "成功"),
    FAILED(2, "失败"),
    UNKNOWN(3, "未知-失败");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    ForeignInterfaceStatus(int code, String message) {
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
