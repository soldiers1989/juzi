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
 * @Author Liuwei@juzifenqi.com
 * @Date 2018年07月18日 17:35
 * @Description: 小程序店中店扫码获取商品详情
 */
@Setter
@Getter
@ToString
public class ScanProductReq implements Serializable {

    //商户id
    @NotNull(message = "商户ID不可为空")
    private Integer sellerId;

    //商品id
    @NotNull(message = "商品id不可为空")
    private Integer productId;

    //微信code
//    @NotNull(message = "微信Code不可为空")
    private String wxCode;

    /**
     * 经度 lng  8.21日修改，此日之前小程序端没有传经纬度，接收时非必填校验，但是后期需要小程序端加上，等待下次商城小程序有需求调整时添加
     */
    private Double lng;

    /**
     * 纬度 lat
     */
    private Double lat;

}
