package com.jzfq.retail.bean.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: ProductCateSearchReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author liuxueliang@juzifenqi.com
 * @Date 2018年07月12日 21:27
 * @Description: 商品筛选入参
 */
@Getter
@Setter
@ToString
public class ProductSearchReq implements Serializable {
    private String productCateName;
    private String productBrandName;
    private String productName;
    private String productStatus;
    private String sellerName;
}