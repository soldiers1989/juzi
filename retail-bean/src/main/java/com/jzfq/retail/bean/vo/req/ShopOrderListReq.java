package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ShopOrderListReq implements Serializable {
    @NotNull(message = "页码不能为空")
    private Integer page;
    @NotNull(message = "每页数量不能为空")
    private Integer pageSize;
    @NotNull(message = "微信code不能为空")
    private String wxcode;
    //订单状态标识 0.全部（不包含待确认），1.待付款，2.待收货，3.已收货
    private Integer orderState;
}
