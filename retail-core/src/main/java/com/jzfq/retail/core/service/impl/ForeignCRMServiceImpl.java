package com.jzfq.retail.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.jzfq.retail.common.util.JsonMapper;
import com.jzfq.retail.core.api.service.SellerAddressService;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.vo.crm.*;
import com.jzfq.retail.common.enmu.*;
import com.jzfq.retail.core.api.service.ForeignCRMService;
import com.jzfq.retail.core.dao.*;
import com.jzfq.retail.core.dao.manual.SellerManualMapper;
import com.jzfq.retail.core.service.SellerCallAccounts;
import com.jzfq.retail.core.service.SystemLogSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Title: ForeignCRMServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月02日 14:48
 * @Description:
 */
@Slf4j
@Service
public class ForeignCRMServiceImpl implements ForeignCRMService {


    @Autowired
    private SellerManualMapper sellerManualMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private SellerStoreMapper sellerStoreMapper;

    @Autowired
    private SellerAddressMapper sellerAddressMapper;

    @Autowired
    private SellerApprovalMapper sellerApprovalMapper;

    @Autowired
    private SellerCompanyMapper sellerCompanyMapper;

    @Autowired
    private SellerImageMapper sellerImageMapper;

    @Autowired
    private SystemLogSupport systemLogSupport;

    @Autowired
    private SellerCallAccounts sellerCallAccounts;

    @Autowired
    private SellerAddressService sellerAddressService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sellerInfo(CRMSystemReq info) throws Exception {
        if (info == null) {
            throw new RuntimeException("CRM调用BD系统添加商户信息：缺少参数");
        }
        log.info("CRM调用BD系统添加商户信息，入参：{}", new JsonMapper().toJson(info));
        //对象信息
        List<ImageInfo> images = info.getImages();
        CompanyInfo company = info.getCompany();
        ShopInfo shop = info.getShop();
        RiskInfo risk = info.getRisk();
        CRMInfo crm = info.getCrm();
        BankcardInfo bankcard = info.getBankcard();
        if (company == null) {
            throw new RuntimeException("CRM调用BD系统添加商户信息：缺少公司信息");
        }
        if (shop == null) {
            throw new RuntimeException("CRM调用BD系统添加商户信息：缺少门店信息");
        }
        if (risk == null) {
            throw new RuntimeException("CRM调用BD系统添加商户信息：缺少风控审核信息");
        }
        if (crm == null) {
            throw new RuntimeException("CRM调用BD系统添加商户信息：缺少CRM信息");
        }
        if (bankcard == null) {
            throw new RuntimeException("CRM调用BD系统添加商户信息：缺少银行卡信息");
        }
        //首先处理seller对象表
        boolean isUpdate = false;
        Seller seller = sellerManualMapper.findBySellerCode(company.getMerchantCode());
        if (seller != null && seller.getId() != null) {
            //throw new RuntimeException("CRM调用BD系统添加商户信息：该商户已经存在BD系统");
            isUpdate = true;
        }else{
            seller = new Seller();
            seller.setCreateTime(new Date());
        }
        log.info("商户CRM入住，isUpdate：{}", isUpdate);
        //添加
        seller.setSellerCode(company.getMerchantCode());
        seller.setSellerName(shop.getShopName());
        seller.setSellerTitle(shop.getShopSimpleName());
        seller.setMainBusiness(shop.getMainBusiness());

        //--默认状态
        seller.setAuditStatus(SellerStatus.OPEN_ACCOUNT_SUCCESS.getCode());

        //商户店铺
        SellerStore sellerStore = new SellerStore();
        if (isUpdate) {
            SellerStoreQuery SSQ = new SellerStoreQuery();
            SSQ.createCriteria().andSellerIdEqualTo(seller.getId());
            List<SellerStore> sellerStores = sellerStoreMapper.selectByExample(SSQ);
            if (sellerStores != null && sellerStores.size() == 1) {
                sellerStore = sellerStores.get(0);
            }
            sellerStore.setUpdateTime(new Date());
        }else {
            sellerStore.setCreateTime(new Date());
        }
        sellerStore.setSellerCode(company.getMerchantCode());
        sellerStore.setStoreType(shop.getType());
        sellerStore.setOpeningTime(shop.getOpenningDate());
        sellerStore.setStoreContacts(shop.getContactName());
        sellerStore.setStoreContactsPosition(shop.getContactJob());
        sellerStore.setStoreContactsMobile(shop.getContactMobile());
        sellerStore.setStoreOwnership(shop.getOwnerShip());
        sellerStore.setOperatingArea(shop.getArea());
        sellerStore.setEmployeesTotalNumber(shop.getWorkers());
        sellerStore.setMonthlySaleMoney(shop.getAverageMonthSale());
        sellerStore.setStartBusinessTime(shop.getWorkStartTime());
        sellerStore.setEndBusinessTime(shop.getWorkEndTime());
        //签约时间
        sellerStore.setStartBusinessDate(crm.getSignDateStart());
        sellerStore.setEndBusinessDate(crm.getSignDateEnd());

        //商户地址信息
        SellerAddress sellerAddress = new SellerAddress();
        if (isUpdate) {
            sellerAddress = sellerAddressService.getBySellerId(seller.getId());
            sellerAddress.setUpdateTime(new Date());
        }else{
            sellerAddress.setCreateTime(new Date());
        }
        sellerAddress.setLongitude(shop.getShopLongitude());
        sellerAddress.setLatitude(shop.getShopLatitude());
        sellerAddress.setProvinceCode(shop.getProvinceCode());
        sellerAddress.setProvinceName(shop.getProvince());
        sellerAddress.setCityCode(shop.getCityCode());
        sellerAddress.setCityName(shop.getCity());
        sellerAddress.setAreaCode(shop.getDistrictCode());
        sellerAddress.setAreaName(shop.getDistrict());
        sellerAddress.setAddressInfo(shop.getAddress());
        sellerAddress.setAddAll(shop.getProvince() + shop.getCity() + shop.getArea());
        sellerAddress.setCreateTime(new Date());


        //商户公司信息
        SellerCompany sellerCompany = new SellerCompany();
        if (isUpdate) {
            SellerCompanyQuery SCQ = new SellerCompanyQuery();
            SCQ.createCriteria().andSellerIdEqualTo(seller.getId());
            List<SellerCompany> sellerCompanies = sellerCompanyMapper.selectByExample(SCQ);
            if (sellerCompanies != null && sellerCompanies.size() == 1) {
                sellerCompany = sellerCompanies.get(0);
            }
            sellerCompany.setUpdateTime(new Date());
        }else{
            sellerCompany.setCreateTime(new Date());
        }
        sellerCompany.setCompany(company.getMerchantName());
        sellerCompany.setMode(company.getMode());
        sellerCompany.setBusinessContactName(company.getBusinessName());
        sellerCompany.setBusinessContactPosition(company.getBusinessJob());
        sellerCompany.setBusinessContactMobile(company.getBusinessMobile());
        sellerCompany.setFinancialContactName(company.getFinanceName());
        sellerCompany.setFinancialContactMobile(company.getFinanceMobile());
        sellerCompany.setLegalPerson(company.getLegalName());
        sellerCompany.setLegalPersonCard(company.getLegalIdCard());
        sellerCompany.setRegistProvinceCode(company.getRegistProvinceCode());
        sellerCompany.setRegistProvince(company.getRegistProvince());
        sellerCompany.setRegistCityCode(company.getRegistCityCode());
        sellerCompany.setRegistCity(company.getRegistCity());
        sellerCompany.setRegistDistrictCode(company.getRegistDistrictCode());
        sellerCompany.setRegistDistrict(company.getRegistDistrict());
        sellerCompany.setRegistAddress(company.getRegistAddress());
        sellerCompany.setBussinessLicense(company.getRegistNo());
        sellerCompany.setRegisteredCapital(company.getRegistCapital());
        sellerCompany.setCompanyStartTime(company.getStartDate());
        sellerCompany.setCompanyEndTime(company.getEndDate());
        sellerCompany.setRange(company.getRange());
        //负责人信息
        sellerCompany.setResponsiblePerson(company.getResponsiblePerson());
        sellerCompany.setResponsiblePersonIdCard(company.getResponsiblePersonIdCard());
        sellerCompany.setResponsiblePersonMobile(company.getResponsiblePersonMobile());
        //银行卡信息
        sellerCompany.setBankUser(bankcard.getName());
        sellerCompany.setBankName(bankcard.getBank());
        sellerCompany.setBankId(bankcard.getBankCode());
        sellerCompany.setBankCode(bankcard.getBankNo());
        sellerCompany.setPersonalCard(bankcard.getCertNo());
        sellerCompany.setAccountType(Integer.valueOf(bankcard.getIsPublic()));
        sellerCompany.setPersonalBankPhone(bankcard.getBankPhone());
        sellerCompany.setBankProvinceCode(bankcard.getBankProvinceCode());
        sellerCompany.setBankProvince(bankcard.getBankProvince());
        sellerCompany.setBankCityCode(bankcard.getBankCityCode());
        sellerCompany.setBankCity(bankcard.getBankCity());
        sellerCompany.setBankNameBranch(bankcard.getPayeeBankFullName());
        sellerCompany.setBrandNameCode(bankcard.getPayeeBankAssociatedCode());

        //--默认值需要协商
        sellerCompany.setState(SellerCompayStatus.UNDER_REVIEW.getCode());
        sellerCompany.setType(SellerCompanyType.MERCHANT_ENTRY.getCode());
        sellerCompany.setAccountType(CompanyAccountType.TO_PRIVATE.getCode());
        //商户审核信息
        SellerApproval sellerApproval = new SellerApproval();
        if (isUpdate) {
            SellerApprovalQuery SALQ = new SellerApprovalQuery();
            SALQ.createCriteria().andSellerIdEqualTo(seller.getId());
            List<SellerApproval> sellerApprovals = sellerApprovalMapper.selectByExample(SALQ);
            if (sellerApprovals != null && sellerApprovals.size() == 1) {
                sellerApproval = sellerApprovals.get(0);
            }
            sellerApproval.setUpdateTime(new Date());
        }else{
            sellerApproval.setCreateTime(new Date());
        }
        sellerApproval.setSellerCode(company.getMerchantCode());
        sellerApproval.setQuota(risk.getTotalQuota());
        sellerApproval.setLimitQuota(risk.getMonthQuota());
        sellerApproval.setFirstLimitQuota(risk.getFirstMonthQuota());
        sellerApproval.setApprovalTime(risk.getApprovedDate());
        sellerApproval.setSerialNumber(crm.getSerialNumber());
        if (isUpdate) {
            //修改
            sellerMapper.updateByPrimaryKey(seller);
            sellerStoreMapper.updateByPrimaryKey(sellerStore);
            sellerAddressMapper.updateByPrimaryKey(sellerAddress);
            sellerApprovalMapper.updateByPrimaryKey(sellerApproval);
            sellerCompanyMapper.updateByPrimaryKey(sellerCompany);

            //图片处理
            imagesH(isUpdate, info.getImages(), seller.getId(), seller.getSellerCode());

            log.info("商户入住修改提交成功[{}]成功", company.getMerchantCode());
            //添加商户入住信息
            systemLogSupport.sellerEnterLogSave(seller.getSellerCode(), ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_100.toString(), null, JSON.toJSONString(info), null, "商户入住修改提交成功");

        } else {
            //保存
            sellerMapper.insert(seller);
            sellerStore.setSellerId(seller.getId());
            sellerStoreMapper.insert(sellerStore);
            sellerAddress.setSellerId(seller.getId());
            sellerAddressMapper.insert(sellerAddress);
            sellerApproval.setSellerId(seller.getId());
            sellerApprovalMapper.insert(sellerApproval);
            sellerCompany.setSellerId(seller.getId());
            sellerCompanyMapper.insert(sellerCompany);

            //图片处理
            imagesH(isUpdate, info.getImages(), seller.getId(), seller.getSellerCode());

            log.info("商户入住提交成功[{}]成功", company.getMerchantCode());
            //添加商户入住信息
            systemLogSupport.sellerEnterLogSave(seller.getSellerCode(), ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_100.toString(), null, JSON.toJSONString(info), null, "商户入住提交成功");

        }
        //商户入住调用账务系统
        sellerCallAccounts.callAccounts(info, seller.getId());
    }


    private void imagesH(boolean isUpdate, List<ImageInfo> images, Integer sellerId, String sellerCode) {
        if (isUpdate) {
            //删除原来图片
            SellerImageQuery SIQ = new SellerImageQuery();
            SIQ.createCriteria().andSellerIdEqualTo(sellerId);
            sellerImageMapper.deleteByExample(SIQ);
        }
        //图片处理
        for (ImageInfo image : images) {
            SellerImage sellerImage = new SellerImage();
            sellerImage.setSellerId(sellerId);
            sellerImage.setSellerCode(sellerCode);
            sellerImage.setImageType(image.getType());
            sellerImage.setUrl(image.getUrl());
            sellerImage.setDelFlag(0);
            sellerImage.setImageName(image.getName());
            sellerImageMapper.insert(sellerImage);
        }
    }
}
