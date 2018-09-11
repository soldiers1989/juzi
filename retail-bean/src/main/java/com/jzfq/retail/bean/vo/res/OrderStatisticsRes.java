package com.jzfq.retail.bean.vo.res;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Company: 北京桔子分期电子商务有限公司
 * @Author caishijian@juzifenqi.com
 * @Date 2018年08月15日 17:25
 * @Description: 订单统计响应对象
 */
@Setter
@Getter
@ToString
public class OrderStatisticsRes implements Serializable {

    /**
     * 进件笔数
     */
    private Integer entryCount;

    /**
     * 客单价
     */
    private BigDecimal perCusTran;

    /**
     * 总额
     */
    private BigDecimal totalAmount;
}
