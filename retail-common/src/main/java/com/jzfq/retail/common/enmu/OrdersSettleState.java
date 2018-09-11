package com.jzfq.retail.common.enmu;

/**
 * @Title: OrdersSettleState
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月16日 16:18
 * @Description: 订单结算枚举
 */
public enum OrdersSettleState {

    _1(1, "待结算"),
    _2(2, "已结算"),
    _3(3, "拒绝"),
    _4(4, "未到结算日"),
    _10(10, "待对账"),
    _20(20, "待核账"),
    _30(30, "待放款"),
    _40(40, "已放款"),
    _50(50, "取消结算"),
    _60(60, "已提现");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    OrdersSettleState(int code, String message) {
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
