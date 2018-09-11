package com.jzfq.retail.bean.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: SellerSearchReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author liuxueliang@juzifenqi.com
 * @Date 2018年07月15日 12:18
 * @Description: 商户表筛选 入参
 */
@Setter
@Getter
@ToString
public class SellerSearchReq implements Serializable {
    @ApiModelProperty(value = "ID")
    private Integer id;
    @ApiModelProperty(value = "账户id")
    private Integer accountId;
    @ApiModelProperty(value = "用户ID")
    private Integer memberId;
    @ApiModelProperty(value = "uuid生成的商家唯一标识-CRM传递唯一标识")
    private String sellerCode;
    @ApiModelProperty(value = "用户名")
    private String name;
    @ApiModelProperty(value = "店铺名称")
    private String sellerName;
    @ApiModelProperty(value = "店铺类型 1：自营，10：自营海外购，11：自营线下店，30：第三方，31：第三方海外购")
    private Integer sellerStyle;
    @ApiModelProperty(value = "审核资料填写状态 1、完成 11、销售信息 21、企业信息 31、结算信息")
    private Integer auditStage;
    @ApiModelProperty(value = "审核状态 1、待审核；2、审核通过；3、冻结；4、创建；5、审核失败；501、销售，企业，结算全失败；502；503；504；505；506；507；508，111全是1，全通过；。")
    private Integer auditStatus;
    @ApiModelProperty(value = "审核阶段 a、销售信息已审核；b、企业信息已审核；c、财务信息已审核")
    private String judgeStage;
    @ApiModelProperty(value = "crm商户入驻传递商户id")
    private Integer merchantId;
    @ApiModelProperty(value = "店铺副标题")
    private String sellerTitle;

}