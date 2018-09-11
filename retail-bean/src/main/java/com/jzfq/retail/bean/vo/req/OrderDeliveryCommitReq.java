package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Title: OrderDeliveryCommitReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月28日 18:32
 * @Description:
 */
@Getter
@Setter
@ToString
public class OrderDeliveryCommitReq implements Serializable {
    /**
     * 订单id
     */
    @NotNull(message = "订单id不可为空")
    private Integer orderId;
    /**
     * 订单图片
     */
    private List<OrderImageReq> orderImages;

}
