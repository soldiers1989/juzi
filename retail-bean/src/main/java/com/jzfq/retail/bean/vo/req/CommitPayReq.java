package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class CommitPayReq implements Serializable {

    //订单号
    @NotBlank(message = "订单号不可为空")
    private String orderSn;

    //交易密码
    @NotBlank(message = "交易密码不可为空")
    private String tradePassword;

    //经度
    @NotNull(message = "经度不可为空")
    private Double longitude;

    //纬度
    @NotNull(message = "纬度不可为空")
    private Double latitude;

}