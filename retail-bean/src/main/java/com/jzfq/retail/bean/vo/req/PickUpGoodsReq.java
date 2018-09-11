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
public class PickUpGoodsReq implements Serializable {

    @NotBlank(message = "orderSn不能为空")
    private String orderSn;

    @NotNull(message = "seqNum不能为空")
    private String seqNum;
}