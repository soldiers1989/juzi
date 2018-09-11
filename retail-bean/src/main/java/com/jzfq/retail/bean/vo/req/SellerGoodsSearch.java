package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: SellerGoodsSearch
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月28日 14:55
 * @Description:
 */
@Getter
@Setter
@ToString
public class SellerGoodsSearch implements Serializable {
    /**
     * 商户id
     */
    @NotNull(message = "商户编号不可为空")
    private Integer sellerId;
    /**
     * 商品状态 0：未上架 1：已上架
     */
    @NotNull(message = "商品状态不可为空")
    private Integer state;
}
