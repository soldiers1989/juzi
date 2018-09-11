package com.jzfq.retail.common.enmu;

/**
 * 请求失败重试类型状态码  error_retry_task  type
 * @author liuwei
 */
public enum ErrorRetryTaskStatus {
    TYPE_1(100, "风控准入"),
    TYPE_2(110, "风控进件"),
    TYPE_3(120, "开立商户额度账户"),
    TYPE_4(130, "开立商户资金账户"),
    TYPE_5(140, "商户绑卡"),
    TYPE_6(150, "信用支付"),
    TYPE_7(160, "资匹分配资方ID"),
    TYPE_8(170, "资匹还款计划"),
    TYPE_9(180, "调用资匹取消"),
    TYPE_10(190, "恢复信用额度"),
    TYPE_11(200, "获取通讯录"),

    RETRY_RESULT_SUCCESS(300, "成功"),
    RETRY_RESULT_ERROR(310, "失败"),

    STATUS_SUCCESS(500, "已成功，不需要重试"),
    STATUS_ERROR(510, "失败，需要重试"),
    STATUS_EXECUTING(520, "执行中，不允许拉取数据"),

    RETRY_COUNT(5, "预定重试次数");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    ErrorRetryTaskStatus(int code, String message) {
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
