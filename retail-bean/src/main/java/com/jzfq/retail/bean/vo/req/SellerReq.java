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
public class SellerReq implements Serializable {
    @NotNull(message = "ID不能为空", groups = {SellerReq.UpdateMethod.class})
    private Integer id;
    @NotNull(message = "账户ID不能为空", groups = {SellerReq.CreateMethod.class})
    private Integer accountId;
    private Integer memberId;
    @NotBlank(message = "商家唯一标识不能为空", groups = {SellerReq.CreateMethod.class})
    private String sellerCode;
    @NotBlank(message = "商户名称不能为空", groups = {SellerReq.CreateMethod.class})
    private String name;
    @NotBlank(message = "店铺名称不能为空", groups = {SellerReq.CreateMethod.class})
    private String sellerName;
    private String sellerTitle;
    private String sellerLogo;
    @NotBlank(message = "商户名称不能为空", groups = {SellerReq.CreateMethod.class})
    private String sellerBgimg;
    private Integer sellerGrade;
    @NotBlank(message = "店铺类型不能为空", groups = {SellerReq.CreateMethod.class})
    private Integer sellerStyle;
    private String scoreService;
    private String scoreDeliverGoods;
    private String scoreDescription;
    private Integer productNumber;
    private Integer collectionNumber;
    private Date createTime;
    private BigDecimal saleMoney;
    private Integer orderCount;
    private Integer orderCountOver;
    private String sellerKeyword;
    private String sellerDes;
    private Integer auditStage;
    private Integer auditStatus;
    private String judgeStage;
    private String failReason;
    @NotNull(message = "商户主营业务不能为空", groups = {SellerReq.CreateMethod.class})
    private Integer mainBusiness;
    private Integer isExclusiveCooperation;
    private Integer coordinationStrength;
    private Integer shopNum;
    private Date renewalStartTime;
    private Date renewalEndTime;
    private String storeSlide;

    public interface CreateMethod{}
    public interface UpdateMethod{}
}