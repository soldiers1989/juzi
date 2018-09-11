package com.jzfq.retail.core.messaging.util;

/**
 * @author lagon
 * @time 2017/6/28 9:43
 * @description 服务提供方枚举
 */
public enum ProviderRealm {

    CORE_ZP("CORE_ZP","核心-资匹")
    ;


    private String mark;
    private String name;

    ProviderRealm(String mark, String name) {
        this.mark=mark;
        this.name=name;
    }

    public String getMark() {
        return mark;
    }

    public String getName() {
        return name;
    }

    public static ProviderRealm getEnum(String mark){
        for (ProviderRealm providerRealm : ProviderRealm.values()) {
            if (providerRealm.getMark().equals(mark)) {
                return providerRealm;
            }
        }
        return null;
    }

}
