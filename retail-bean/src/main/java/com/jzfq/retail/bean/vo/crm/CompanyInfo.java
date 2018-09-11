package com.jzfq.retail.bean.vo.crm;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: CompanyInfo
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月18日 16:20
 * @Description: 公司信息
 */
@Getter
@Setter
@ToString
public class CompanyInfo implements Serializable {
    /**
     * 商户id
     */
    @NotBlank(message = "[merchantCode]商户编号不可以为空")
    private String merchantCode;
    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 营业模式 1：自营 2：加盟 0：其它
     */
    private Integer mode;
    /**
     * 业务联系人姓名
     */
    private String businessName;
    /**
     * 业务联系人职位名称
     */
    private String businessJob;
    /**
     * 业务联系人手机号
     */
    private String businessMobile;
    /**
     * 财务联系人姓名
     */
    private String financeName;
    /**
     * 财务联系人手机号
     */
    private String financeMobile;
    /**
     * 法定代表人
     */
    private String legalName;
    /**
     * 法定代表人身份证号
     */
    private String legalIdCard;
    /**
     * 实际负责人
     */
    private String responsiblePerson;
    /**
     * 实际负责人身份证号
     */
    private String responsiblePersonIdCard;
    /**
     * 实际负责人联系方式
     */
    private String responsiblePersonMobile;
    /**
     * 营业执照注册号
     */
    private String registNo;
    /**
     * 注册资本 单位：万元
     */
    private BigDecimal registCapital;
    /**
     * 经营范围
     */
    private String range;
    /**
     * 注册日期
     */
    private Date startDate;
    /**
     * 营业期限至
     */
    private Date endDate;
    /**
     * 注册地址-省code
     */
    private String registProvinceCode;
    /**
     * 注册省地址
     */
    private String registProvince;
    /**
     * 注册市地址code
     */
    private String registCityCode;
    /**
     * 注册市地址
     */
    private String registCity;
    /**
     * 注册区县地址code
     */
    private String registDistrictCode;
    /**
     * 注册区县地址
     */
    private String registDistrict;
    /**
     * 注册地址
     */
    private String registAddress;

}
