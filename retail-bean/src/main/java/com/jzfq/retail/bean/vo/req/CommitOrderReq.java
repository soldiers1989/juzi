package com.jzfq.retail.bean.vo.req;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Title: CommitOrderReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月19日 13:33
 * @Description: 小程序-客户-提交订单接口入参
 */
@Getter
@Setter
@ToString
public class CommitOrderReq implements Serializable {

    //微信code
    @NotBlank(message = "微信CODE不可为空")
    private String wxCode;

    //订单号
    @NotBlank(message = "订单号不可为空")
    private String orderSn;

    //首付比例  0,10,30,50
    @NotNull(message = "首付比例不可为空")
    @Min(value = 0, message = "首付比例不能小于0")
    private Double firstPayRatio;

    //月利率 0.2, 0.6
    @NotNull(message = "月利率不可为空")
    @Min(value = 0, message = "月利率不能小于0")
    private Double ratio;

    //月供本金
    @NotNull(message = "月供本金不可为空")
    @Min(value = 0, message = "月供本金不能小于0")
    private BigDecimal monthPrincipal;

    //服务费
    @NotNull(message = "服务费不可为空")
    @Min(value = 0, message = "服务费不能小于0")
    private BigDecimal monthService;

    //期数
    @NotNull(message = "期数不可为空")
    @Min(value = 0, message = "期数不能小于0")
    private Integer term;

}
