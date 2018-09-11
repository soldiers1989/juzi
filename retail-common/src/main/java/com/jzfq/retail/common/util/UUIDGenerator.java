package com.jzfq.retail.common.util;

import java.util.UUID;

/**
 * @author lagon
 * @time 2017/3/6 11:24
 * @description UUID生成工具
 */
public class UUIDGenerator {

    public UUIDGenerator() {

    }

    /**
     * 获得一个UUID
     * @return String UUID
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        String ruuid=uuid.replaceAll("-","");
        return ruuid;
    }

    /**
     * 获得指定数目的UUID
     * @param number int 需要获得的UUID数量
     * @return String[] UUID数组
     */

    public static String[] getUUID(int number){
        if(number < 1){
            return null;
        }
        String[] ss = new String[number];
        for(int i=0;i<number;i++){
            ss[i] = getUUID();
        }
        return ss;
    }

    public static void main(String[] args){
//        String[] ss = getUUID(11);
//        for(int i=0;i<ss.length;i++){
//            System.out.println(ss[i]);
//        }
        System.out.println(getUUID());

    }

}
