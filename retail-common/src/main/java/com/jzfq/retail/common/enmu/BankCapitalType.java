package com.jzfq.retail.common.enmu;

/**
 * response 返回状态码
 * @author luyankun
 * @version V1.0
 * @date 2016年12月06日 9:17
 */
public enum BankCapitalType {

    CAPITAL_TYPE_SC("SC","商城"),
    CAPITAL_TYPE_BT("BT","白条"),
    CAPITAL_TYPE_DF("DF", "店付")
    ;


    private String retCode;
    private String retMsg;

    BankCapitalType(String retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public String getRetCode() {
        return retCode;
    }

    public String getRetMsg(Object... params){
        return String.format(retMsg, params);
    }

}
