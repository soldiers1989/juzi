package com.jzfq.retail.common.enmu;

public enum IdentificationCode {

    FIRST_GOODS(1, "关闭"),
    SECOND_GOODS(2, "打开");

    private Integer status;

    private String description;

    IdentificationCode(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public static IdentificationCode getEnum(Integer status) {
        for (IdentificationCode identificationCode : IdentificationCode.values()) {
            if (identificationCode.getStatus() == status) {
                return identificationCode;
            }
        }
        return null;
    }
}
