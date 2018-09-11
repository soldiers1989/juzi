package com.jzfq.retail.bean.vo.crm;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Title: CRMSystemReq
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月17日 20:09
 * @Description: CRM商户入住请求入参
 */

@Getter
@Setter
@ToString
public class CRMSystemReq implements Serializable {
    /**
     * 图像信息
     */
    private List<ImageInfo> images;
    /**
     * 公司信息
     */
    @Valid
    private CompanyInfo company;
    /**
     * 门店信息
     */
    @Valid
    private ShopInfo shop;
    /**
     * 风控审核信息
     */
    @Valid
    private RiskInfo risk;
    /**
     * crm信息
     */
    @Valid
    private CRMInfo crm;
    /**
     * 银行卡信息
     */
    @Valid
    private BankcardInfo bankcard;
}
