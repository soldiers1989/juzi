package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;

@Setter
@Getter
@ToString
public class RiskCallbackReq implements Serializable {

    //状态
    @NotBlank(message = "状态不能为空")
    private String status;

    //订单号
    @NotBlank(message = "订单号不能为空")
    private String orderSn;

    //备注
    private String remark;

    //拒绝code
    private String closeCode;

    //拒绝原因
    private String closeResult;
}