package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: CheckRegisterReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月18日 17:35
 * @Description: 验证用户是否登录注册入参
 */
@Setter
@Getter
@ToString
public class CheckRegisterReq implements Serializable {

    //微信code
    @NotBlank(message = "微信Code不可为空")
    private String wxCode;

    //订单号
//    @NotBlank(message = "订单号不可为空")
    private String orderSn;

    // 纬度
    @NotNull(message = "纬度不可为空")
    private Double latitude;

    // 纬度
    @NotNull(message = "经度不可为空")
    private Double longitude;
}
