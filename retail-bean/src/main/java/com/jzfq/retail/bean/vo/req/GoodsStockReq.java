package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: CheckRegisterReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Liuwei@juzifenqi.com
 * @Date 2018年07月18日 17:35
 * @Description: 小程序店中店扫码获取商品详情
 */
@Setter
@Getter
@ToString
public class GoodsStockReq implements Serializable {

    //goodsStockId
    @NotNull(message = "商品库存id不可为空")
    private Integer goodsStockId;

}
