package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 商户创建订单入参
 *
 * @author liuwei
 * @version V1.0
 * 2018年7月5日上午
 */
@ToString
@Getter
@Setter
public class CreateOrderReq implements Serializable {

    /**
     * 商户id
     */
    @NotNull(message = "商户编号不可为空")
    private Integer sellerId;
    /**
     * 商户名称
     */
    @NotBlank(message = "商户名称不可为空")
    private String sellerName;
    /**
     * 品类id
     */
    @NotNull(message = "商品分类编号不可为空")
    private Integer cateId;
    /**
     * 品类名称
     */
    @NotBlank(message = "商品分类名称不可为空")
    private String cateName;
    /**
     * 品牌id
     */
    @NotNull(message = "商品品牌编号不可为空")
    private Integer brandId;
    /**
     * 品牌名称
     */
    @NotBlank(message = "商品品牌名称不可为空")
    private String brandName;
    /**
     * 商品id
     */
    @NotNull(message = "商品编号不可为空")
    private Integer productId;
    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不可为空")
    private String productName;
    /**
     * 客户姓名
     */
    @NotBlank(message = "客户姓名不可为空")
    private String customerName;
    /**
     * 商品单价
     */
    @NotNull(message = "商品单价不可为空")
    @Min(value = 1, message = "商品单价必须大于1元")
    private BigDecimal price;

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

    /**
     * 订单规格信息 如：颜色,褐色;容量,64G
     */
    //@NotNull(message = " 订单规格信息不可为空")
    private String specInfo;
    /**
     * 订单图片地址
     */
    //@NotNull(message = "订单图片地址不可为空")
    private List<OrderImageReq> orderImages;
    /**
     * 订单商品品名
     */
    private String proLabel;

    /**
     * 备注 remark
     */
    private String remark;
}
