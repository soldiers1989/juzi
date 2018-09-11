package com.jzfq.retail.common.enmu;

/**
 * @Title: OrderImageType
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月22日 17:32
 * @Description:
 */
public enum OrderImageType {
    _1(1, "扫码店创建订单");

    /**
     * 类型code
     */
    private int type;

    /**
     * 类型描述
     */
    private String typeDesc;

    OrderImageType(int type, String typeDesc) {
        this.type = type;
        this.typeDesc = typeDesc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }


}
