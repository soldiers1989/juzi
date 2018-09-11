package com.jzfq.retail.bean.vo.res;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Company: 北京桔子分期电子商务有限公司
 * @Author caishijian@juzifenqi.com
 * @Date 2018年08月03日 10:34
 * @Description: 二维码响应
 */
@Setter
@Getter
@ToString
public class TwoDimensionRes implements Serializable {

    /**
     * 订单编号
     */
    private String productName;

    /**
     * 客户姓名
     */
    private String productDesc;

    /**
     * 二维码地址
     */
    private String url;

}
