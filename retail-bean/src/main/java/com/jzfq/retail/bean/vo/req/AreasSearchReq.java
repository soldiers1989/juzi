package com.jzfq.retail.bean.vo.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: ProductBrandSearchReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月09日 19:36
 * @Description: 商品品牌筛选入参
 */
@Setter
@Getter
@ToString
public class AreasSearchReq implements Serializable {
   private String areaId;
   private String areaName;
   private String cityCode;
   private String zipCode;
   private String createUser;
   private String parentName;
   private String levelType;
}
