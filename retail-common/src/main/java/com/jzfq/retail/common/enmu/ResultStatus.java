package com.jzfq.retail.common.enmu;

/**
 * 自定义请求状态码
 * @author liuwei
 */
public enum ResultStatus {
    SUCCESS(100, "成功"),
    USERNAME_OR_PASSWORD_ERROR(110, "用户名或密码错误"),
    USER_NOT_FOUND(120, "用户不存在"),
    USER_ALREADY_EXIST(130, "用户已存在"),
    USER_NOT_LOGIN(140, "用户未登录"),
    USERNAME_NOT_NULL(150, "用户名不能为空"),
    PASSWORD_NOT_NULL(160, "密码名不能为空"),
    CHECKCODE_NOT_NULL(170, "图形验证码不能为空"),
    CHECKCODE_IS_WRONG(180, "图形验证码输入错误"),
    USER_FROZEN(190, "该用户已被冻结"),
    ORDER_IS_EMPTY(220, "订单为空"),
    UNOPEN_SINGLE_CREDIT(230, "此商户未设置是否开启单笔授信"),
    CRM_ADDRESS_ERROR(240, "CRM入驻地址错误"),
    POSITION_ERROR(250, "店员下单位置距离店铺大于2KM"),
    NOT_ACTIVE(260,"用户状态未激活");

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;

    ResultStatus(int code, String message) {
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
