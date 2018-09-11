package com.jzfq.retail.common.enmu;

/**
 * GPS校验规则开关
 *
 * @author caishijian
 * @version V1.0
 * @date 2018年07月30日 12:57
 */
public enum GpsSwitchStatus {

    GPS_RULE_CLOSE(0, "关闭"),
    GPS_RULE_OPEN(1, "打开");

    private Integer status;

    private String description;

    GpsSwitchStatus(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public static GpsSwitchStatus getEnum(Integer status) {
        for (GpsSwitchStatus gpsSwitchStatus : GpsSwitchStatus.values()) {
            if (gpsSwitchStatus.getStatus() == status) {
                return gpsSwitchStatus;
            }
        }
        return null;
    }
}
