package com.jzfq.retail.common.enmu;

/**
 * @Title: SellerImageName
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月20日 16:44
 * @Description: 商户图片名称
 */
public enum SellerImageName {

    IMAGE_NAME_0001("营业执照"),
    IMAGE_NAME_0002("法定代表人或实际负责人身份证（正面）"),
    IMAGE_NAME_0003("法定代表人或实际负责人身份证（反面）"),
    IMAGE_NAME_0004("组织机构代码证"),
    IMAGE_NAME_0005("银行开户许可证"),
    IMAGE_NAME_0006("股东会决议"),
    IMAGE_NAME_0007("税务登记证"),
    IMAGE_NAME_0008("特许经营行业需提供特许经营许可证"),
    IMAGE_NAME_0009("其他"),
    IMAGE_NAME_0010("店铺室内照片1"),
    IMAGE_NAME_0011("店铺室内照片2"),
    IMAGE_NAME_0012("店铺室内照片3"),
    IMAGE_NAME_0013("店铺室外照片-门头照"),
    IMAGE_NAME_0014("BD连同签约人手持合同照片"),
    IMAGE_NAME_0015("营业场地房产证或房屋租赁合同"),
    IMAGE_NAME_0016("委托人代收声明函"),
    IMAGE_NAME_0017("线下商户合作协议"),
    IMAGE_NAME_0018("商户补充协议"),
    IMAGE_NAME_0019("商户补充协议"),
    IMAGE_NAME_0020("商户补充协议"),
    IMAGE_NAME_0021("授权委托书"),
    IMAGE_NAME_0022("声明函");

    private String name;

    private SellerImageName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
