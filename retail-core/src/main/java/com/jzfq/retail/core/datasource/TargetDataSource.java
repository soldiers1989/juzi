package com.jzfq.retail.core.datasource;

/**
 * @Author: liuwei
 * @Date: 2017/7/18 11:13
 * @Description: 在方法上使用，用于指定使用哪个数据源
 * @Version: 1.0
 */

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String name();
}