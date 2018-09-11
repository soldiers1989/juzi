package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: OrderImageReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月28日 17:44
 * @Description:
 */
@Setter
@Getter
@ToString
public class OrderImageReq implements Serializable {

    @NotNull(message = "订单图片地址不可为空")
    private String url;

    private String remark;

}
