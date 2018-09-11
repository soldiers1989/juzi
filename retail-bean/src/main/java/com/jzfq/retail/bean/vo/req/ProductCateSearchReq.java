package com.jzfq.retail.bean.vo.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: ProductCateSearchReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月09日 17:42
 * @Description: 商品分类筛选入参
 */
@Getter
@Setter
@ToString
public class ProductCateSearchReq implements Serializable {
    /**
     * 编号
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 是否显示 1 显示 2不显示
     */
    private Integer visible;

}
