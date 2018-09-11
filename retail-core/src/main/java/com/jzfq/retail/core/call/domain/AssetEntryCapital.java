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
public class AssetEntryCapital {

    private BigDecimal amount;//借款金额必填
    private BigDecimal couponAmount;//分期订单或者全款订单都必传 无优惠券 传0；
    private String application;//SC商城、BT白条，DF店付必填
    private List<Integer> authList;//认证项必填 项目类型:1_身份认证2_基本信息3_联系人4_face5_白条6_芝麻7_运营商8_京东9_单位信息
    private String bankCardId;//用户中心订单绑定银行卡的主键ID暂时非必填
    private String bankCardNumber;//代扣卡号必填
    private String bankCustomer;//银行卡绑定姓名必填
    private String bankId;//银行代码简写必填
    private String bankName;//银行名称必填
    private String bankPhone;//银行预留手机号必填
    private Integer behead;//是否砍头息必填 1是、0否
    private String callbackUrl;//回调地址必填
    private Integer capitalCode;//资金方必填
    private String categoryCode;//三级类目非必传
    private Integer deliveryCount;//历史发货次数非必填
    private BigDecimal diffPrice;//差价非必传，默认为0
    private String downpaymentAmount;//首付金额非必填
    private String fCode;//fCode码非必填
    private String firstLevelCategory;//一级类目非必传
    private String firstPayOrder;//首付流水号非必填
    private Integer freeInterest;//是否免息 1是 0 否
    private String goodsAddress;//收货详细地址goodsAddress不能为空
    private String goodsArea;//收货区
    private String goodsAreaCode;//收货区编码
    private String goodsCity;//收货市
    private String goodsCityCode;//收货市编码
    private String goodsName;//收货人
    private String goodsPhone;//收货人手机号
    private String goodsPost;//收货地址编码
    private String goodsProvince;//收货省
    private String goodsProvinceCode;//收货省编码
    private String loanPurpose;//借款用途婚宴------> 婚庆喜宴 WEDDING_BANQUET 旅游 ------>旅行远足 HIKING 教育------> 进修学习 FURTHER_STUDY 家电 ------>日常购物 DAILY_SHOPPING
    private Integer num;//商品个数必填
    private String orderId;//订单编码必填
    private String orderProvince;//下单区域非必填
    private Integer orderType;//必填 0：线上订单 1：线下体验店
    private Double paymentRatio;//首付比例必填
    private Integer period;//期数必填
    private Integer periodType;//分期单位非必填 1 月，2 天
    private BigDecimal price;//商品价格必填
    private String productCode;//场景产品线类型必填 产品类型01现金贷、02上线分期（商城）、03线下分期（白条），04车险
    private String productId;//商品编码必填
    private BigDecimal productRate;//借款标准利率必填
    private Double rate;//费率必填
    private String repayDate;//首期还款日必填
    private String secondLevelCategory;//二级类目非必传
    private String serviceFee;//服务费非必填
    private Integer storeCount;//下单次数非必填
    private String storeOrderTime;//下单时间必填
    private String title;//商品名称必填

    private List<Contacts> contacts;//联系人 非必填

    private EntryMerchant entryMerchant;//商户信息 白条必填，其他非必填

    private EntryPerson entryPerson;//用户信息 必填

    private List<EntryTelbooks> entryTelbooks;//通讯录

    //内部类 联系人
    @Setter
    @Getter
    @ToString
    public static class Contacts {
        private String contractsName;//联系人姓名
        private String contractsPhone;//联系人手机号
    }

    //内部类 商户信息
    @Setter
    @Getter
    @ToString
    public static class EntryMerchant {
        private String address;//商户详细地址白条必填，其他非必填
        private String area;//商户所在区白条必填，其他非必填
        private String city;//商户所在市白条必填，其他非必填
        private String cox;//经度白条必填，其他非必填
        private String coy;//纬度白条必填，其他非必填
        private String idNo;//商家身份证号白条必填，其他非必填
        private String major;//商户主营业务白条必填，其他非必填
        private String merchantId;//商户编码白条必填，其他非必填
        private String mobile;//商户手机号白条必填，其他非必填
        private String name;//商户名称白条必填，其他非必填
        private String province;//商户所在省白条必填，其他非必填
        private String realName;//商户名称白条必填，其他非必填


    }

    //内部类 用户信息
    @Setter
    @Getter
    @ToString
    public static class EntryPerson {
        private Integer age;//年龄number必填
        private String cardWithHeadImageUrl;// 手持身份证照片非必填
        private String certAddress;// 身份证地址必填
        private String certBackImageUrl;// 身份证反面照片必填
        private String certFrontImageUrl;// 身份证正面照片url地址必填
        private String certNo;// 身份证号必填
        private String certValidEnd;// 身份证有效期结束时间只传结束时间yyyy-MM-dd
        private String certValidStart;// 身份证有效期开始时间只传开始时间yyyy-MM-dd
        private String certValidTime;// 身份证有效期全格式
        private String companyAddress;// 公司地址非必填
        private String companyDepartment;// 公司部门非必填
        private String companyDuty;// 公司职位非必填 TYPE_1("1", "一般员工"), TYPE_2("2", "基层管理"), TYPE_3("3", "中层管理"), TYPE_4("4", "高层管理法人"), TYPE_5("5", "技术员工"), TYPE_6("6", "其他");
        private String companyEntryTime;// 入职时间非必填
        private String companyName;// 公司名称非必填
        private String companyPhone;// 公司手机号非必填
        private String companyWorkArea;// 工作地址区非必填
        private String companyWorkAreaCode;// 工作地址区代码非必填
        private String companyWorkCity;// 工作地址市非必填
        private String companyWorkCityCode;// 工作地址市代码非必填
        private String companyWorkPro;// 工作地址省非必填
        private String companyWorkProCode;// 工作地址省代码非必填
        private Integer customerId;// 用户id必填
        private String customerName;// 用户姓名必填
        private String customerPhone;// 用户电话必填
        private Integer degree;// 学位必填
        private String drivingLicense;// 车牌号非必填
        private String education;// 学历非必填
        private String ethnicity;// 民族最好必填
        private String frameInteger;// 车架号非必填
        private Integer juziScore;// 同盾分必填
        private String liveAddress;// 居住详细地址
        private String liveAreaCode;// 居住地址区编码
        private String liveAreaName;// 居住地址区名称
        private String liveCityCode;// 居住地址市编码
        private String liveCityName;// 居住地址市名称
        private String liveProCode;// 居住地址省编码
        private String liveProName;// 居住地址省名称
        private String livingImage;// 活体照片
        private String plateLicense;// 行驶证非必填
        private String salary;// 月收入
        private String sesameCredit;// 芝麻信用分
        private Integer sex;// 性别性别 0女 1男
    }

    //内部类 通讯录
    @Setter
    @Getter
    @ToString
    public static class EntryTelbooks {
        private String contactsName;//名称
        private String contactsPhone;//手机号
    }
}
