package com.jzfq.retail.bean.vo.crm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: RiskInfo
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月18日 16:21
 * @Description: 风控审核信息
 */
@Getter
@Setter
@ToString
public class RiskInfo implements Serializable {
    /**
     * 首月授信额度
     */
    @NotNull(message ="[firstMonthQuota]首月授信额度不可为空" )
    private BigDecimal firstMonthQuota;
    /**
     * 每月授信额度
     */
    @NotNull(message ="[monthQuota]每月授信额度不可为空" )
    private BigDecimal monthQuota;
    /**
     * 授信总额度
     */
    @NotNull(message ="[totalQuota]授信总额度不可为空" )
    private BigDecimal totalQuota;
    /**
     * 审核通过时间
     */
    @NotNull(message ="[approvedDate]审核通过时间不可为空" )
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approvedDate;
}
