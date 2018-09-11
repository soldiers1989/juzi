package com.jzfq.retail.common.util;

import com.jzfq.retail.common.util.date.DateUtil;

import java.util.Date;
import java.util.UUID;

/**
 * @author liuwei
 * @version 1.0
 **/
public class CodeGenerateUtils {
    public static String getRandomCode(){
        return UUID.randomUUID().toString();
    }

    /**
     * 年月日时分秒毫秒
     * @return
     */
    public static String getTimeCode(){
        return DateUtil.formatDate(new Date(), "yyyyMMddHHmmssSSS");
    }

    /**
     * 年月日时分秒毫秒+3位随机数
     * @return
     */
    public static String getTimeRandomCode(){
        String random = RandomUtil.r9(3);
        return getTimeCode() + random;
    }

//    public static void main(String[] args) {
//        System.out.println(getTimeRandomCode());
//    }

}
