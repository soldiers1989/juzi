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
public class ProductBrandSearchReq implements Serializable {
    /**
     * 品牌名称
     */
    private String name;
    /**
     * 品牌英文名称
     */
    private String englishName;

    /**
     * 分类ID
     */
    private Integer cateId;

    /**
     * 是否已删除
     */
    private Integer state;


}
