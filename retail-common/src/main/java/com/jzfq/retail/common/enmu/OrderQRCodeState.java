package com.jzfq.retail.common.enmu;

/**
 * @Title: OrderQRCodeState
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月24日 15:46
 * @Description:
 */
public enum OrderQRCodeState {


    ORDER_QRCODE_STATE_0(0, "未使用"),
    ORDER_QRCODE_STATE_1(1, "已失效"),
    ORDER_QRCODE_STATE_2(2, "已使用");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    OrderQRCodeState(int code, String message) {
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
