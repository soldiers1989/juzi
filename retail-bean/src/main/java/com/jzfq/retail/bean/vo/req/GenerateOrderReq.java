package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description: II期便利店客户扫码创建订单入参
 *
 * @author liuwei
 * @version V1.0
 * 2018年7月5日上午
 */
@ToString
@Getter
@Setter
public class GenerateOrderReq implements Serializable {

    /**
     * 商户id
     */
    @NotNull(message = "商户编号不可为空")
    private Integer sellerId;

    /**
     * 商品id
     */
    @NotNull(message = "商品编号不可为空")
    private Integer productId;

    /**
     * 商品库存id
     */
    @NotNull(message = "商品库存id不可为空")
    private Integer goodsStockId;

    /**
     * 经度 lng
     */
    @NotNull(message = "经度不可为空")
    private Double lng;

    /**
     * 纬度 lat
     */
    @NotNull(message = "纬度不可为空")
    private Double lat;

    //微信code
    @NotBlank(message = "微信Code不可为空")
    private String wxCode;

    //订单金额
    @NotNull(message = "订单金额不可为空")
    private BigDecimal price;

    /**
     * 备注 remark
     */
    private String remark;
}
