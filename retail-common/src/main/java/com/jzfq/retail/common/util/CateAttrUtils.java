package com.jzfq.retail.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author MaoLixia
 * @Date 2018/8/24 14:19
 * 品类属性工具类
 */
public class CateAttrUtils {
    /**
     * @param param  颜色:白色,黑色,红色|尺码:S,M,L,XL|容量:128G,64G,32G
     * 获取参数的笛卡尔积
     * 首先将数据转换成数组：[[颜色:白色, 颜色:黑色, 颜色:红色], [尺码:S, 尺码:M, 尺码:L, 尺码:XL], [容量:128G, 容量:64G, 容量:32G]]
     * a=[颜色:白色, 颜色:黑色, 颜色:红色]
     * b=[尺码:S, 尺码:M, 尺码:L, 尺码:XL]
     * c=[容量:128G, 容量:64G, 容量:32G]
     * 其大小分别为a_length=3,    b_length=4  c_length=3
     * 笛卡尔积list的总大小为：totalSize=3*4*3=36
     * 对每个子集a,b,c进行循环次数=总记录数/(元素个数*后续集合的笛卡尔积个数)
     * 对a中的每个元素进行循环=总记录数/(元素个数*后续集合的笛卡尔积个数)=36/3*12=1
     * 对b中的每个元素进行循环=总记录数/(元素个数*后续集合的笛卡尔积个数)=36/4*3=3
     * 对b中的每个元素进行循环=总记录数/(元素个数*后续集合的笛卡尔积个数)=36/3*1=12
     */
    public static List<String> getAttrList(String param){
        String[] list = param.split("\\|");
        List<List> strs = new ArrayList<>();
        for(int i = 0; i < list.length; i++){
            List attrValue = new ArrayList();
            String[] attrList = list[i].split(":");
            String attrKey = attrList[0];
            String[] attrValues = attrList[1].split(",");
            for(int j = 0; j < attrValues.length; j++){
                attrValue.add(attrKey+","+attrValues[j]);
            }
            strs.add(attrValue);
        }
//        System.out.println(strs);
        int total = 1;
        for(int i = 0; i < strs.size(); i++){
            total*=strs.get(i).size();
        }
        String[] myresult = new String[total];
        int now = 1;
        //每个元素每次循环打印个数
        int itemLoopNum = 1;
        //每个元素循环的总次数
        int loopPerItem = 1;
        for(int i = 0; i < strs.size(); i++){
            List temp = strs.get(i);
            now = now * temp.size();
            //目标数组的索引值
            int index = 0;
            int currentSize = temp.size();
            itemLoopNum = total/now;
            loopPerItem = total/(itemLoopNum*currentSize);
            int myindex = 0;
            for(int j = 0; j < temp.size(); j++){
                for(int k = 0; k < loopPerItem; k++){
                    if(myindex == temp.size()) myindex = 0;
                    for(int m = 0; m < itemLoopNum; m++){
                        myresult[index]=(myresult[index]==null?"":myresult[index]+";")+temp.get(myindex);
                        index++;
                    }
                    myindex++;
                }
            }
        }
        return Arrays.asList(myresult);
    }

    public static JSONArray getNormName(String param){
        param = param.replaceAll("，", ","); //传入的字符串处理
        String[] list = param.split("\\|");
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < list.length; i++){
            String[] attrs = list[i].split(":");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(attrs[0], Arrays.asList(attrs[1].split(",")));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
    public static void main(String[] args){
        String param = "颜色:白色，黑色,红色|尺码:S,M,L,XL|容量:128G,64G,32G";
//        System.out.println(getAttrList(param));
        System.out.println(getNormName(param));

    }
}
