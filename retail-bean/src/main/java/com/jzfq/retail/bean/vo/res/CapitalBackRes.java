package com.jzfq.retail.bean.vo.res;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年06月29日 18:25
 * @Description: 返回接口格式
 */
public class CapitalBackRes implements Serializable {

    public static Map<String, Object> success(final String message) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("code", "100000");
        map.put("msg", message);
        map.put("success", true);
        return map;
    }

    public static Map<String, Object> error(final String message) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("code", "200000");
        map.put("msg", message);
        map.put("success", false);
        return map;
    }

}
