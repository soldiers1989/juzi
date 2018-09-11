package com.jzfq.retail.bean.vo.crm;

import com.jzfq.retail.bean.valid.annotation.NotBlank;
import com.jzfq.retail.bean.valid.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Title: BankcardInfo
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月18日 16:22
 * @Description: 银行卡信息
 */

@Getter
@Setter
@ToString
public class BankcardInfo implements Serializable {

    /**
     * 银行卡号
     */
    @NotBlank(message = "[bankNo]银行卡号不可为空")
    private String bankNo;

    /**
     * 持卡人姓名
     */
    @NotBlank(message = "[name]持卡人姓名不可为空")
    private String name;

    /**
     * 开户行
     */
    @NotBlank(message = "[bank]开户行不可为空")
    private String bank;

    /**
     * 对公or对私 1：对公 2：对私
     */
    @NotBlank(message = "[isPublic]银行卡对公or对私不可为空")
    private String isPublic;

    /**
     * 开户行省code
     */
    @NotBlank(message = "[bankProvinceCode]开户行省code不可为空")
    private String bankProvinceCode;

    /**
     * 开户行省地址
     */
    @NotBlank(message = "[bankProvince]开户行省地址不可为空")
    private String bankProvince;

    /**
     * 开户行市code
     */
    @NotBlank(message = "[bankCityCode]开户行市code不可为空")
    private String bankCityCode;

    /**
     * 开户行市地址
     */
    @NotBlank(message = "[bankCity]开户行市地址不可为空")
    private String bankCity;


    //####对公必传
    /**
     * 银行编码
     */
    private Integer bankCode;
    /**
     * 支行联行号
     */
    private String payeeBankAssociatedCode;
    /**
     * 支行名称
     */
    private String payeeBankFullName;

    //#对私必传
    /**
     * 身份证号
     */
    private String certNo;
    /**
     * 预留手机号
     */
    private String bankPhone;


}
