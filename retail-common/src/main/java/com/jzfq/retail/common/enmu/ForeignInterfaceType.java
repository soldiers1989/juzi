package com.jzfq.retail.common.enmu;

/**
 * @Title: ForeignInterfaceType
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月10日 9:49
 * @Description: 对外接口日志请求类型
 */
public enum ForeignInterfaceType {

    REQ(1,"请求"),
    RES(2,"返回");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    ForeignInterfaceType(int code, String message) {
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
