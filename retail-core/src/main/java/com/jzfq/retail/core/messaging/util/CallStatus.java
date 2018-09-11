package com.jzfq.retail.core.messaging.util;

/**
 * @author lagon
 * @time 2017/10/25 17:58
 * @description 调用状态枚举
 */
public enum CallStatus {

    PROCESSING(0,"处理中"),
    SUCCESS(1, "成功"),
    ERROR(2, "失败");

    private Integer code;
    private String desc;

    CallStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CallStatus getEnum(Integer status){
        for (CallStatus callStatus : CallStatus.values()) {
            if (callStatus.getCode().equals(status)) {
                return callStatus;
            }
        }
        return null;
    }
}
