package com.jzfq.retail.core.dao.manual;

import java.util.Map;

public interface OpenidRecordManualMapper {

    /**
     * 统计未认证、认证成功、认证失败用户的数量及对应比例
     * @return
     */
    Map<String, Object> getAuthenticationStateOfCountAndRatio();
}