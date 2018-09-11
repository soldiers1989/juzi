package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.vo.crm.CRMSystemReq;

/**
 * @Title: ForeignCRMService
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月02日 14:47
 * @Description: CRM操作系统对接操作接口
 */
public interface ForeignCRMService {

    /**
     * 商户入住
     * @param info crm入住信息
     */
    void sellerInfo(CRMSystemReq info) throws Exception;
}
