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
 * @Date 2018年08月06日 14:55
 * @Description: 分期表筛选 入参
 */
@Setter
@Getter
@ToString
public class SellerTermSearchReq implements Serializable {
    @ApiModelProperty(value = "店铺名称")
    private String sellerName;
}