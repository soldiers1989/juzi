package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class SellerTermReq implements Serializable {
    @NotNull(message = "店铺名称不能为空")
    private Integer sellerId;

    @NotNull(message = "期数不能为空")
    private Integer term;

    @NotNull(message = "月利率不能为空")
    private Double monthRate;

    @NotNull(message = "折扣百分比不能为空")
    private BigDecimal afterDiscountRate;

    @NotNull(message = "折后费率不能为空")
    private BigDecimal discount;
}