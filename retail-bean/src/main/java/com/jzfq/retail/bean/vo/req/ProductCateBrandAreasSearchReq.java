package com.jzfq.retail.bean.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: ProductCateBrandAreasSearchReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月09日 14:51
 * @Description: 分类规则筛选 入参
 */
@Setter
@Getter
@ToString
public class ProductCateBrandAreasSearchReq implements Serializable {
    @ApiModelProperty(value = "品类名称")
    private String cateName;
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    @ApiModelProperty(value = "区域名称")
    private String areaName;
    @ApiModelProperty(value = "风控审核状态 0、默认；1、提交审核；2、审核通过；3、审核失败；4、停用'")
    private Integer status;
    @ApiModelProperty(value = "风控审核人")
    private String approvalUser;
    @ApiModelProperty(value = "创建人")
    private String createUser;

}
