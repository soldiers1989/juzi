package com.jzfq.retail.core.call.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Title: AssetBankCapital
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月04日 10:01
 * @Description: 资产平台 第一次调用资金路由-绑卡 参数模型
 */
@Setter
@Getter
@ToString
public class AssetBankCapital {

    private BigDecimal amount;//借款金额	必传、
    private String application;//渠道	必传、描述：商城：SC、白条：BT 店付:DF
    private List<String> authList;//认证项	必传、传数组 项目类型:1_身份认证2_基本信息3_联系人4_face5_白条6_芝麻7_运营商8_京东9_单位信息
    private Integer bairongScore;//百融分	非必传
    private String categoryCode;//三级类目	非必传
    private String certNo;//身份证	必传、
    private Integer customerId;//用户id	必传、
    private String fCode;//	f码	非必传、
    private String firstLevelCategory;//一级类目	非必传
    private String goodsProvince;//	收货地址省
    private String goodsProvinceCode;//	收货地址省编码
    private String orderId;//订单编码	必传、
    private String orderProvince;//	区域	必传、
    private Integer orderType;//	必传 类别 0：线上订单 1：线下体验店
    private Integer period;//分期期数	必传、
    private Integer periodType;//分期单位	非必传 1 月，2 天
    private String productCode;//产品类型	必传、描述：01现金贷、02上线分期（商城）、03线下分期（白条）
    private String secondLevelCategory;//二级类目	非必传
    private Integer tdScore;//同盾分	非必传


}
