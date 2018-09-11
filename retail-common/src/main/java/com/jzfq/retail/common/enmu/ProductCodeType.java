package com.jzfq.retail.common.enmu;

/**
 * response 返回状态码
 * @author luyankun
 * @version V1.0
 * @date 2016年12月06日 9:17
 */
public enum ProductCodeType {

    PRODUCT_CODE_TYPE_01("01","现金贷"),
    PRODUCT_CODE_TYPE_02("02","线上分期（商城）"),
    PRODUCT_CODE_TYPE_03("03", "线下分期（白条）");


    private String retCode;
    private String retMsg;

    ProductCodeType(String retCode, String retMsg) {
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
