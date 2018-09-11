package com.jzfq.retail.bean.valid.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotInlUtil {

    public static boolean valid(String params, Object obj) {
        com.alibaba.dubbo.common.utils.Assert.notNull(params, "params不能为空");
        Integer status = (Integer) obj;
        String[] split = params.split(",");
        List<String> paramList = Arrays.asList(split);
        for(String param : paramList){
            if(param.equals(status+"")){
                return true;
            }
        }
        return false;
    }
}