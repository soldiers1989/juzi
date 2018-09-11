package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
public class ProductWithBLOBsReq implements Serializable {
    private String name2;
    @NotBlank(message = "商品描述信息不能为空", groups = {ProductWithBLOBsReq.UpdateMethod.class})
    private String description;
    private String packing;
    private String afterSales;
    private String specPacking;
    @NotNull(message = "ID不能为空", groups = {ProductWithBLOBsReq.UpdateMethod.class})
    private Integer id;
    @NotNull(message = "分类ID不能为空", groups = {ProductWithBLOBsReq.UpdateMethod.class, ProductWithBLOBsReq.CreateMethod.class})
    private Integer productCateId;
    private String productCatePath;
    @NotBlank(message = "商品名称不能为空", groups = {ProductWithBLOBsReq.UpdateMethod.class, ProductWithBLOBsReq.CreateMethod.class})
    private String name1;
    private String keyword;
    @NotNull(message = "品牌ID不能为空", groups = {ProductWithBLOBsReq.UpdateMethod.class, ProductWithBLOBsReq.CreateMethod.class})
    private Integer productBrandId;
//    @NotNull(message = "1、自营；2、商家不能为空", groups = {ProductWithBLOBsReq.UpdateMethod.class, ProductWithBLOBsReq.CreateMethod.class})
    private Integer isSelf;
//    @NotNull(message = "成本价不能为空", groups = {ProductWithBLOBsReq.UpdateMethod.class, ProductWithBLOBsReq.CreateMethod.class})
    private BigDecimal costPrice;
//    @NotNull(message = "保护价不能为空", groups = {ProductWithBLOBsReq.UpdateMethod.class, ProductWithBLOBsReq.CreateMethod.class})
    private BigDecimal protectedPrice;
    private BigDecimal marketPrice;
    private BigDecimal mallPcPrice;
    private BigDecimal malMobilePrice;
    private Integer virtualSales;
    private Integer actualSales;
    private Integer productStock;
    private Integer isNorm;
    private String normIds;
    private String normName;
    private Integer state;
    private Integer isTop;
    private Date upTime;
    private Integer sellerId;
    private Integer createId;
    private Integer upUserId;
    private Date createTime;
    private Date updateTime;
//    @NotNull(message = "商家分类ID不能为空", groups = {ProductWithBLOBsReq.UpdateMethod.class, ProductWithBLOBsReq.CreateMethod.class})
    private Integer sellerCateId;
    private Integer sellerIsTop;
    private Integer sellerState;
    private Integer commentsNumber;
    private Integer productCateState;
    private Integer isInventedProduct;
    private Integer transportType;
    private Integer transportId;
//    @NotBlank(message = "主图不能为空", groups = {ProductWithBLOBsReq.UpdateMethod.class, ProductWithBLOBsReq.CreateMethod.class})
    private String masterImg;
//    @NotBlank(message = "商品编码", groups = {ProductWithBLOBsReq.UpdateMethod.class, ProductWithBLOBsReq.CreateMethod.class})
    private String productCode;
    private String shortName;
    private Integer weights;
    private BigDecimal settlementPrice;
    private Date offTime;
    private Integer mFloorDataId;
    private String masterMiddleImg;
    private String masterLittleImg;
    private String tagFall;
    private String tagFree;
    private String sourceDescription;
    private Integer isFullPayment;
    private Integer isByStages;
    private Integer isSearch;
    private Integer isPickself;
    private String remark4;
    private String remark5;
    private String remark6;
    private String remark7;
    private Integer isCategory;
    private Integer limits;
    private String deliveryChannel;
    @NotNull(message = "商品标识不能为空", groups = {ProductWithBLOBsReq.UpdateMethod.class, ProductWithBLOBsReq.CreateMethod.class})
    private Integer identification;
    private String huohao;

    public interface CreateMethod{}
    public interface UpdateMethod{}
}