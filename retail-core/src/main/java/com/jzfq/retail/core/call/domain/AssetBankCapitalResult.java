package com.jzfq.retail.core.call.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Title: AssetBankCapitalResult
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月04日 14:20
 * @Description: 资产平台 第一次调用资金路由-绑卡 返回参数
 * 第二次进件需要该参数
 */
@Setter
@Getter
@ToString
public class AssetBankCapitalResult {

    private String capital;//资金方-最优资金方
    private String isSupportMultiple;//是否可以绑多张卡-是否支持绑定多张卡。1是。0否
    private String needBindCard;//是否需要绑卡-是否需要绑卡，1是，0否

}
