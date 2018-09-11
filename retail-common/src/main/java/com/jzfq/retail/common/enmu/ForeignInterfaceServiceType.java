package com.jzfq.retail.common.enmu;

/**
 * 对外接口日志描述信息   call_accounts_operation_log  call_assets_operation_log  call_risk_operation_log  service_type
 *
 * @author liuwei
 */
public enum ForeignInterfaceServiceType {
    DESCRIBE_100(100, "商户入驻"),
    DESCRIBE_110(110, "风控进件"),
    DESCRIBE_120(120, "开立商户额度账户"),
    DESCRIBE_130(130, "开立商户资金账户"),
    DESCRIBE_140(140, "商户绑卡"),
    DESCRIBE_160(160, "资匹分配资方ID"),
    DESCRIBE_170(170, "资匹生成还款计划"),
    DESCRIBE_180(180, "调用资匹取消"),
    DESCRIBE_190(190, "风控准入"),
    DESCRIBE_200(200, "用户绑卡"),
    DESCRIBE_210(210, "恢复信用额度"),
    DESCRIBE_220(220, "下单立减信用额度"),
    DESCRIBE_230(230, "资匹回调"),
    DESCRIBE_240(240, "查询商户信用账户信息"),
    DESCRIBE_250(250, "风控审核回调"),
    DESCRIBE_260(260, "获取通讯录"),
    DESCRIBE_270(270, "还款完成回调"),
    DESCRIBE_280(280, "商户订单核账"),
    DESCRIBE_290(290, "商户账户提现"),
    DESCRIBE_300(300, "商户资金账户查询"),
    DESCRIBE_310(310, "商户账户提现回调");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    ForeignInterfaceServiceType(int code, String message) {
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
