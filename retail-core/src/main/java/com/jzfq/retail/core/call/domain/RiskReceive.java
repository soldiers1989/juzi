package com.jzfq.retail.core.call.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @title: OrderCheck
 * @description:
 * @company: 北京桔子分期电子商务有限公司  风控准入入参
 * @author: Liu Wei
 * @date: 2018/7/10 16:21
 */
@Setter
@Getter
@ToString
public class RiskReceive {
    private Integer orderId;
    private String orderSn;
    private List<Contacts> contactsList = new ArrayList<Contacts>(); //联系人
    private List<Images> imagesList = new ArrayList<Images>(); //图片
    private Insurance insurance; //车险信息
    private Loan loan;//贷款信息
    private Person person;//个人信息
    private Product product;//产品
    private Profession profession;//职业信息
    private Task task;//任务表
    private String token;//

    @Setter
    @Getter
    @ToString
    public static class Contacts {
        String name;// 姓名
        String phone;// 电话
        String relation;// 关系
    }

    @Setter
    @Getter
    @ToString
    public static class Images {
        String smallUrl;// 压缩影像地址
        String type;// 图片类型
        String url;// 影像地址
    }

    @Setter
    @Getter
    @ToString
    public static class Insurance {
        String adValoremAmount ;// 	价税合计
        String carType ;// 	车辆类型
        String commercialInsuranceAmount ;// 	商业险金额
        String commercialInsuranceCompany ;// 	商业险保险公司
        String commercialInsuranceNo ;// 	商业险单号
        String drivingIssueDate ;// 	行驶证签发日期
        String drivingRegistDate ;// 	行驶证注册日期
        String engineNo ;// 	发动机号
        String frameNo ;// 	车架号
        String invoiceCode ;// 	发票代码
        String invoiceNo ;// 	发票号码
        String merchantType ;// 	商户类型
        String plateNo ;// 	车牌号
        String registInvoiceDate ;// 	开发票日期
        String shopCompanyName ;// 	销货单位名称
        String strongInsuranceAmount ;// 	交强险金额
        String strongInsuranceCompany ;// 	交强险公司
        String strongInsuranceNo ;// 	交强险保单号
        String totalAmount ;// 	保单总金额
        String valueAddedTaxAmount ;// 	增值税合计
    }

    @Setter
    @Getter
    @ToString
    public static class Loan {
        BigDecimal accrual; // 利息
        BigDecimal contractAmount;//签约金额
        BigDecimal firstPay;// 首付
        Double firstPayRatio;// 首付比例
        BigDecimal loanAmount;// 贷款金额
        Integer loanPeriod;// 贷款期限
        Integer loanType;// 贷款类型
        BigDecimal monthPay;// 月供
        String productColor;// 产品颜色
        String productName;// 产品名称
        Integer productNum;// 商品数量
        String productUrl;// 产品链接地址
        String productVersion;// 版本
        String pyCertify;// 鹏远认证结果
        Double rate;// 期限利率
        String registCode;// 注册推荐码
        String remark;// 申请备注
        String tdScore;// 同盾评分
        Integer unrepay_amount;// 
        String zmxyScore;// 芝麻信用分
    }

    @Setter
    @Getter
    @ToString
    public static class Person {
        String	admissionDate;// 入学时间
        String	alipay;// 支付宝
        String	bankBranch;// 银行支行
        String	bankNo;// 常用银行卡号
        String	birthDate;// 生日
        String	borrowingPurposes;// 借款用途
        String	certCardNo;// 身份证号
        Integer	certType;// 证件类型
        Integer	commodityNum;// 商品数量
        String	country;// 国籍
        String	debitBank;// 开户行
        Integer	degree	;// 	学位
        Integer	distributionWay	;// 	配送方式
        Integer	education	;// 	教育程度（学历）
        String	effectBeginDate	;// 	有效日期起
        String	effectEndDate	;// 	有效日期止
        String	email	;// 	电子邮箱
        String	graduateDate	;// 	毕业时间
        Integer	haveCar	;// 	是否有车
        String	homeAddress	;// 	居住地址
        String	homeCity	;// 	居住城市
        String	homeCityCode	;// 	现居城市code
        String	homeDistrict	;// 	现居城市街道
        String	homeDistrictCode	;// 	现居城市街道code
        String	homeProvince	;// 	现居省份
        String	homeProvinceCode	;// 	现居省code
        String  jxlToken	;//
        String	major	;// 	专业
        Integer	marriage	;// 	婚姻状况
        String	merchantAddress	;// 	商户-地址
        String	merchantCity	;// 	商户-市
        String	merchantDistrict	;// 	商户-地区
        String	merchantGrade	;// 	商户级别
        String	merchantId	;// 	商户id
        String	merchantName	;// 	商户名称
        String	merchantNo	;// 	商户编号
        String	merchantProvince	;// 	商户-省
        String	minzu	;// 	民族
        String	mobile	;// 	手机号
        String	mobileOther	;// 	手机号1
        Integer	monthIncome	;// 	每月工作收入
        String	phone	;// 	住宅电话
        String	qq	;// 	QQ
        String	receiptAddress	;// 	收货地址
        String	receiptCity	;// 	收货城市
        String	receiptCityCode	;// 	收货城市code
        String	receiptDistrict	;// 	收货街道
        String	receiptDistrictCode	;//
        String	receiptName	;// 	收件人姓名
        String	receiptPhone	;// 	收件手机
        String	receiptProvince	;// 	收件省份
        String	receiptProvinceCode	;// 	收件省code
        String	registAddress	;// 	户籍-地址
        String	registCity	;// 	户籍-市
        String	registDistrict	;// 	户籍地址
        String	registPostcode	;// 	户籍-邮编
        String	registProvince	;// 	户籍-市
        String	school	;// 	毕业院校
        Integer	schoolLevel	;// 	教育程度
        Integer	sex	;// 	性别
        String	wechat	;// 	微信
        String	wifiMac	;// 	WIFI的MAC地址
        String	withdrawBankNo	;// 	提现银行卡
        String	withdrawDebitBank	;// 	提现银行开户行
        String	withdrawPhoneNo	;// 	提现银行卡预留手机号
    }

    @Setter
    @Getter
    @ToString
    public static class Product {
        Integer	channelId	;// 	渠道
        Integer	clientType	;// 	设备类型
        Integer	commodityType	;// 	商品品类
        Integer	customerType	;// 	客户类型
        Integer	financialProductId	;// 	金融产品
        Integer	merchantType	;// 	商户类型
        Integer	operationSubType	;// 	提额类型
        Integer	operationType	;// 	操作类型
        String	tacticskqid	;// 	客群id
    }

    @Setter
    @Getter
    @ToString
    public static class Profession {
        String	companyAddress	;// 	公司地址
        String	companyCity	;// 	公司城市
        String	companyCityCode	;// 	
        String	companyDistrict	;// 	公司街道
        String	companyDistrictCode	;// 	
        String	companyName	;// 	公司名称
        String	companyPhone	;// 	公司电话
        String	companyPostcode	;// 	单位邮编
        String	companyProvince	;// 	公司省份
        String	companyProvinceCode	;// 	
        String	department	;// 	部门
        String	job	;// 	职位
        Integer	nature	;// 	单位性质
        String	profession	;// 	职业
        String	salaryDay	;// 	发薪日
        Integer	salaryMethod	;//
        String	title	;// 	职称
        String	workBeginDate	;// 	开始工作日期
        String	workYears	;// 	工作年限
    }

    @Setter
    @Getter
    @ToString
    public static class Task {
        Integer	addQuotaType	;// 	提额类型
        String	applyTime	;// 	申请时间
        String	certCardNo	;// 	身份证号	
        String	channel	;// 	渠道	
        String	company	;// 	工作单位	
        String	createTime	;// 	创建时间	
        Integer	customerLevel	;// 	用户现金贷评级结果 （A 类 或 B类）	
        String	customerName	;// 	姓名	
        Integer	customerType	;// 	客户类型	
        String	dataAuditApproveName	;// 	资审人名称	
        String	dataAuditApproveTime	;// 	终审时间	
        Integer	dataAuditApproveUser	;// 	终审人	
        String	finalApproveName	;// 	终审人名称	
        String	finalApproveTime	;// 	终审时间	
        Integer	finalApproveUser	;// 	终审人	
        String	firstApproveName	;// 	初审人名称	
        String	firstApproveTime	;// 	初审时间	
        Integer	firstApproveUser	;// 	初审人	
        String	gpsAddress	;// 	gps地址	
        String	gpsCity	;// 	gps城市	
        String	gpsCityCode	;// 	gps城市code	
        String	gpsDistrict	;// 	gps街道	
        String	gpsDistrictCode	;// 	gps街道code	
        String	gpsProvince	;// 	gps省份	
        String	gpsProvinceCode	;// 	gps省code	
        Integer	homeCity	;// 	居住-市	
        String	homeProvince	;// 	居住-省	
        Integer	is3c	;// 	是否轻奢	
        Integer	is3cFirst	;// 	是否首单3C	
        Integer	is3cOther	;// 	是否其它3C	
        String	isJxl	;// 	聚信立认证	
        Integer	isNoApprove	;// 	是否免签	
        Integer	isNoSign	;// 	是否免签	
        Integer	isVip	;// 	用于普通初审看不到Vip初审的任务列表	
        Double	lat	;// 	纬度
        Double	lng	;// 	经度
        Integer	loanType	;// 	产品大类	
        String	orderNo	;// 	订单号	
        Integer	productCategory	;// 	出资方
        String	productType	;// 	产品线（贷款类型）	
        String	productTypeName	;// 		
        String	realName	;// 	真实姓名	
        String	registCode	;// 	F码	
        Integer	riskCount	;// 	数量	
        String	riskTime	;// 	时间	
        Integer	roleCode	;// 	角色码	
        String	signFlag	;// 	是否校验免签免审	
        Integer	singularHandling	;// 	办理人数	
        String	source	;// 	商品来源	
        Integer	status	;// 	状态	
        String	statusName	;// 		
        Integer	tdFlag	;// 	闭单标识	
        Integer	tdScore	;// 	同盾分	
        String	userName	;// 	人员	
        Integer	uuid	;// 	客户id
    }
}
