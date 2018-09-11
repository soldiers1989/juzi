package com.jzfq.retail.core.datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: liuwei
 * @Date: 2017/7/18 11:12
 * @Description:
 * @Version: 1.0
 */
public class DynamicDataSourceContextHolder {

    /**
     * 默认主库
     */
    public static final String DEFAULT_DATA_SOURCE = "master";

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static List<String> dataSourceIds = new ArrayList<>();

    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public static String getDataSourceType() {
        return contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }

    /**
     * @Author: liuwei
     * @Description: 判断指定DataSource当前是否存在
     * @Version: 1.0
     * @Date: 2017/7/18 11:20
     */
    public static boolean containsDataSource(String dataSourceId){
        return dataSourceIds.contains(dataSourceId);
    }
}
