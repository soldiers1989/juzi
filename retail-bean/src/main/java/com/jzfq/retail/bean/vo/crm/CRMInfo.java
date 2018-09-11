package com.jzfq.retail.bean.vo.crm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: CRMInfo
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月18日 16:22
 * @Description: CRM信息
 */
@Getter
@Setter
@ToString
public class CRMInfo implements Serializable {
    /**
     * 签约开始日期
     */
    @NotNull(message ="[signDateStart]签约开始日期不可为空" )
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signDateStart;
    /**
     * 签约结束日期
     */
    @NotNull(message ="[signDateEnd]签约结束日期不可为空" )
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signDateEnd;
    /**
     * 流水号
     */
    @NotBlank(message ="[serialNumber]流水号不可为空" )
    private String serialNumber;
}
