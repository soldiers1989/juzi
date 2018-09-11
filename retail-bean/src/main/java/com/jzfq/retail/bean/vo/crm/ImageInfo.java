package com.jzfq.retail.bean.vo.crm;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: ImageInfo
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月18日 16:19
 * @Description: 图像信息
 */
@Getter
@Setter
@ToString
public class ImageInfo implements Serializable {
    /**
     * 图像分类 base:基本、store:门店、other:其他
     */
    @NotBlank(message = "[type]图像类型不可为空")
    private String type;
    /**
     * 图像地址
     */
    @NotBlank(message = "[url]图像地址不可为空")
    private String url;
    /**
     * 图像名称
     * 营业执照
     * 法定代表人或实际负责人身份证（正面）
     * 法定代表人或实际负责人身份证（反面）
     * 组织机构代码证
     * 银行开户许可证
     * 股东会决议
     * 税务登记证
     * 特许经营行业需提供特许经营许可证
     * 其他
     * 店铺室内照片1
     * 店铺室内照片2
     * 店铺室内照片3
     * 店铺室外照片-门头照
     * BD连同签约人手持合同照片
     * 营业场地房产证或房屋租赁合同
     * 委托人代收声明函
     * 线下商户合作协议
     * 商户补充协议
     * 商户补充协议
     * 商户补充协议
     * 授权委托书
     * 声明函
     */
    @NotBlank(message = "[name]图像名称不可为空")
    private String name;
}
