package com.jzfq.retail.common.enmu;

/**
 * GPS校验规则范围
 * @author caishijian
 * @version V1.0
 * @date 2018年07月30日 12:57
 */
public enum GpsScopeFlag {

    GPS_NO_GLOBAL(0, "非全局"),
    GPS_YES_GLOBAL(1, "全局")
    ;

    private Integer flag;

    private String description;

    GpsScopeFlag(Integer flag, String description) {
        this.flag = flag;
        this.description = description;
    }

    public Integer getFlag() {
        return flag;
    }

    public String getDescription() {
        return description;
    }

    public static GpsScopeFlag getEnum(Integer flag){
        for (GpsScopeFlag gpsScopeFlag : GpsScopeFlag.values()) {
            if (gpsScopeFlag.getFlag() == flag) {
                return gpsScopeFlag;
            }
        }
        return null;
    }
}
