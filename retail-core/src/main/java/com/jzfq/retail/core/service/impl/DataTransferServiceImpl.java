package com.jzfq.retail.core.service.impl;

import com.juzifenqi.core.StringUtil;
import com.jzfq.retail.bean.TermRule;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.vo.crm.*;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.api.service.DataTransferService;
import com.jzfq.retail.core.dao.*;
import com.jzfq.retail.core.service.SellerCallAccounts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Title: DataTransferServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author caishijian@juzifenqi.com
 * @Date 2018年07月24日 9:53
 * @Description: 新零售上线初始化数据导入
 */
@Slf4j
@Service
public class DataTransferServiceImpl implements DataTransferService {

    @Autowired
    private SellerMapper sellerMapper;
    @Autowired
    private SellerSettlePointMapper sellerSettlePointMapper;
    @Autowired
    private SellerAddressMapper sellerAddressMapper;
    @Autowired
    private SellerCompanyMapper sellerCompanyMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductCateMapper productCateMapper;
    @Autowired
    private ProductBrandMapper productBrandMapper;
    @Autowired
    private AreasMapper areasMapper;
    @Autowired
    private ProductCateBrandAreasMapper productCateBrandAreasMapper;
    @Autowired
    private SellerTermMapper sellerTermMapper;
    @Autowired
    private SellerLoginPermissionMapper sellerLoginPermissionMapper;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private SellerCallAccounts sellerCallAccounts;




    private static Map<String, List<TermRule>> ruleMap = new HashMap<String, List<TermRule>>();

    private static Map<String,String> bankIdMap = new HashMap<String,String>();

    static {
        ruleMap.put("3/6/9/12期免息不开全款", new ArrayList<TermRule>(){{
            add(new TermRule(3,0.015,BigDecimal.ZERO,BigDecimal.ZERO,10d));
            add(new TermRule(6,0.014,BigDecimal.ZERO,BigDecimal.ZERO,10d));
            add(new TermRule(9,0.013,BigDecimal.ZERO,BigDecimal.ZERO,10d));
            add(new TermRule(12,0.012,BigDecimal.ZERO,BigDecimal.ZERO,10d));
        }});
        ruleMap.put("3/6/9/12期不免息不开全款", new ArrayList<TermRule>(){{
            add(new TermRule(3,0.015,new BigDecimal("100"),new BigDecimal("0.015"),0d));
            add(new TermRule(6,0.014,new BigDecimal("100"),new BigDecimal("0.014"),0d));
            add(new TermRule(9,0.013,new BigDecimal("100"),new BigDecimal("0.013"),0d));
            add(new TermRule(12,0.012,new BigDecimal("100"),new BigDecimal("0.012"),0d));
        }});
        ruleMap.put("3期免息6/9/12不免息不开全款", new ArrayList<TermRule>(){{
            add(new TermRule(3,0.015,new BigDecimal("0"),new BigDecimal("0"),5d));
            add(new TermRule(6,0.014,new BigDecimal("100"),new BigDecimal("0.014"),5d));
            add(new TermRule(9,0.013,new BigDecimal("100"),new BigDecimal("0.013"),5d));
            add(new TermRule(12,0.012,new BigDecimal("100"),new BigDecimal("0.012"),5d));
        }});

        bankIdMap.put("ABC","23");
        bankIdMap.put("CMB","25");
        bankIdMap.put("ICBC","20");
        bankIdMap.put("CCB","21");
        bankIdMap.put("BOC","22");
        bankIdMap.put("BCM","24");
        bankIdMap.put("CEB", "26");
        bankIdMap.put("CMBC","27");
        bankIdMap.put("BOB","28");
        bankIdMap.put("CGB","29");
        bankIdMap.put("SPDB","31");
        bankIdMap.put("HSBC","33");
        bankIdMap.put("CITIC","34");
        bankIdMap.put("HXB","35");
        bankIdMap.put("CIB","36");
        bankIdMap.put("BJRCB","38");
        bankIdMap.put("PSBC","40");
        bankIdMap.put("PAB","41");
        bankIdMap.put("BOS","42");
        bankIdMap.put("JSB","71");
        bankIdMap.put("NJCB","72");

        /*bankIdMap.put("BC","22");
        bankIdMap.put("CBG","29");
        bankIdMap.put("HB","35");
        bankIdMap.put("CNCB","34");*/

        // 生产时放开，注掉上面四行
        bankIdMap.put("BOC","22");
        bankIdMap.put("CGB","29");
        bankIdMap.put("HXB","35");
        bankIdMap.put("CITIC","34");


    }


    @Override
    public void initDataImport(List<Map<Integer, String>> sellers,List<Map<Integer, String>> products,
                               List<Map<Integer, String>> productCateBrandAreases,List<Map<Integer, String>> sellerTerms,
                               List<Map<Integer, String>> sellerLoginPermissions,
                               List<Map<Integer, String>> productCates,List<Map<Integer, String>> productBrands) {

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                saveInitData(sellers, products, productCateBrandAreases, sellerTerms, sellerLoginPermissions, productCates, productBrands);
            }
        });

    }

    @Override
    public void batchCallAccounts(List<Map<Integer, String>> sellers) {
        for(int i = 0; i < sellers.size(); i++) {

            // 行数据
            Map<Integer, String> sellerMap = sellers.get(i);

            // 构造请求能数
            CRMSystemReq req = new CRMSystemReq();
            CompanyInfo companyInfo = new CompanyInfo();
            RiskInfo riskInfo = new RiskInfo();
            CRMInfo crmInfo = new CRMInfo();
            ShopInfo shop = new ShopInfo();
            BankcardInfo bankcardInfo = new BankcardInfo();
            req.setCompany(companyInfo);
            req.setBankcard(bankcardInfo);
            req.setShop(shop);
            req.setCrm(crmInfo);
            req.setRisk(riskInfo);

            // 商户编码
            companyInfo.setMerchantCode(sellerMap.get(22).trim());
            log.info("==============businessId:================:{}",companyInfo.getMerchantCode());
            try {
                // 风控审核通过时间
                riskInfo.setApprovedDate(DateUtil.parseDate(sellerMap.get(3).trim(), DateUtil.FORMATTING_YMD));
                // 签约开始时间
                crmInfo.setSignDateStart(DateUtil.parseDate(sellerMap.get(2).trim(), DateUtil.FORMATTING_YMD));
            } catch (Exception e) {
                log.error("时间解析异常：{}",e);
            }
            // 首月授信额度
            riskInfo.setFirstMonthQuota(BigDecimal.ZERO);
            // 每月授信额度
            riskInfo.setMonthQuota(new BigDecimal("300000"));
            // 授信总额度
            riskInfo.setTotalQuota(new BigDecimal("300000"));
            // 商户名称
            shop.setShopName(sellerMap.get(1).trim());
            // 银行卡信息
            String accountName = sellerMap.get(11).trim();
            if(StringUtil.isEmpty(accountName)) {
                // 对公
                bankcardInfo.setIsPublic("1");
                // 银行卡号，对公使用企业帐号
                bankcardInfo.setBankNo(sellerMap.get(16).trim());
                // 姓名，对公使用店铺名称
                bankcardInfo.setName(sellerMap.get(1).trim());
            } else {
                // 对私
                bankcardInfo.setIsPublic("2");
                // 银行卡号
                bankcardInfo.setBankNo(sellerMap.get(14).trim());
                // 姓名
                bankcardInfo.setName(sellerMap.get(11).trim());
            }

            // 银行编码
            bankcardInfo.setBankCode(Integer.parseInt(bankIdMap.get(sellerMap.get(21).trim())));

            // 银行预留手机号
            bankcardInfo.setBankPhone(sellerMap.get(20).trim());
            // 身份证号
            bankcardInfo.setCertNo(sellerMap.get(19).trim());
            // 商户号
            sellerCallAccounts.initDataCallAccounts(req);

        }
    }



    @Override
    @Transactional
    public void saveInitData(List<Map<Integer, String>> sellers, List<Map<Integer, String>> products, List<Map<Integer, String>> productCateBrandAreases, List<Map<Integer, String>> sellerTerms, List<Map<Integer, String>> sellerLoginPermissions, List<Map<Integer, String>> productCates, List<Map<Integer, String>> productBrands) {
        // 保存分类、品牌
        converseAndSaveProductCatesAndProductBrands(productCates,productBrands);

        // 保存健身商户信息
        converseAndSaveSellers(sellers);

        // 保存商品信息
        converseAndSaveProducts(products);

        // 保存区间价
       converseAndSaveProductCateBrandAreases(productCateBrandAreases);

        // 保存分期信息
        converseAndSaveSellerTerm(sellerTerms);

        // 保存商户帐号密码
        converseAndSaveSellerLoginPermission(sellerLoginPermissions);
    }


    private void converseAndSaveProductCatesAndProductBrands(List<Map<Integer, String>> productCates,List<Map<Integer, String>> productBrands) {
        for(int i = 0; i< productCates.size(); i++) {
            // 品类行数据
            Map<Integer, String> productCateMap = productCates.get(i);
            // 保存品类信息
            ProductCate cate = saveProductCate(productCateMap);
            // 保存与该品类相关联的品牌
            saveProductBrand(productBrands, cate);
        }
    }

    private void saveProductBrand(List<Map<Integer, String>> productBrands, ProductCate cate) {
        // 遍历品牌数据
        for(int i = 0; i < productBrands.size(); i++) {
            // 品牌行数据
            Map<Integer, String> productBrandMap = productBrands.get(i);
            // 如果该行数据(品牌)对应的品类与刚入库的品类数据名称相同，则关联上品类主键，
            if(cate.getName().equals(productBrandMap.get(0).trim())) {
                ProductBrand brand = new ProductBrand();
                // 品牌名称
                brand.setName(productBrandMap.get(1));
                // 分类关联id
                brand.setProductCateId(cate.getId());
                // 非空字段暂且赋值如下
                brand.setCreateId(0); // 创建人id
                brand.setUpdateId(0);// 更新人id
                brand.setState(0); // 【0、默认；1、提交审核；2、显示中；3、审核失败；4、删除】
                brand.setCreateTime(DateUtil.getDate());
                brand.setNameFirst("");// 品牌首字母
                productBrandMapper.insert(brand);
            }
        }
    }

    private ProductCate saveProductCate(Map<Integer, String> productCateMap) {
        // 查询品类是否已经入库
        ProductCateQuery cateQuery = new ProductCateQuery();
        cateQuery.or().andNameEqualTo(productCateMap.get(0).trim());
        List<ProductCate> productCates =  productCateMapper.selectByExample(cateQuery);
        if(!CollectionUtils.isEmpty(productCates) && productCates.size() == 1) {
            return productCates.get(0);
        }
        if(!CollectionUtils.isEmpty(productCates) && productCates.size() > 1) {
            throw new BusinessException("品类数据存在冗余记录，品类名：" + productCateMap.get(0).trim());
        }
        // 入库实体
        ProductCate cate = new ProductCate();
        // 品类名称
        cate.setName(productCateMap.get(0).trim());
        // 以下非空字段，暂时赋值如下：
        cate.setCreateId(0);
        cate.setUpdateId(0);
        cate.setCreateTime(DateUtil.getDate());
        cate.setUpdateTime(DateUtil.getDate());
        cate.setVisible(1);
        productCateMapper.insert(cate);
        return cate;
    }

    private void converseAndSaveSellerTerm(List<Map<Integer, String>> sellerTerms) {
        for(int i = 0; i < sellerTerms.size(); i++) {
            // 行数据
            Map<Integer, String> sellerTermMap = sellerTerms.get(i);
            saveSellerTerm(sellerTermMap);
        }
    }

    private void saveSellerTerm(Map<Integer, String> sellerTermMap) {
        // 店铺ID 0
        // 店铺名称 1
        // 入库实体
        SellerTerm term = new SellerTerm();
        if(!StringUtil.isEmpty(sellerTermMap.get(1))) {
            String sellerName = sellerTermMap.get(1).trim();
            List<Seller> sellers = getDbSellers(sellerName);
            if(!CollectionUtils.isEmpty(sellers) && sellers.size() == 1) {
                term.setSellerId(sellers.get(0).getId());
            }else {
                throw new BusinessException("根据分类名称查询店铺信息异常，描述：，" + sellers == null ? "为空":("记录数：" + sellers.size()));
            }
        } else {
            throw new BusinessException("分类名称不能为空！");
        }
        //  扣点费率 2  已关联保存到 seller_settle_point

        // 分期信息规则 3
        if(!StringUtil.isEmpty(sellerTermMap.get(3))) {
            List<TermRule> list = ruleMap.get(sellerTermMap.get(3).trim());
            for(TermRule rule: list) {
               // 期数
               term.setTerm(rule.getTerm());
               // 月利率
               term.setMonthRate(rule.getDefaultRate());
               // 折后费率
               term.setAfterDiscountRate(rule.getAfterDiscountRate());
               // 折扣百分比
               term.setDiscount(rule.getDiscount());

               // 不为空的字段
               term.setSort(0);
               term.setStatus(1); // 【1-正常 2-冻结 3-删除】
               sellerTermMapper.insert(term);
            }
        }
    }

    private void converseAndSaveSellers(List<Map<Integer, String>> sellers) {
        for(int i = 0; i < sellers.size(); i++) {
            // 行数据
            Map<Integer, String> sellerMap = sellers.get(i);
            // 保存商户信息
            Seller seller = saveSeller(sellerMap);
            // 保存商户结算扣点
            saveSellerSettlePoint(sellerMap, seller);
            // 保存商户公司信息
            saveSellerCompany(sellerMap, seller);
            // 保存商户店铺地址
            saveSellerAddress(sellerMap, seller);
        }

    }

    private void saveSellerAddress(Map<Integer, String> sellerMap, Seller seller) {
        // 商户店铺地址 入库实体对象
        SellerAddress sellerAddress = new SellerAddress();
        // 业务员 7
        // 是否入驻 8
        // 城市 9
        if(!StringUtil.isEmpty(sellerMap.get(9))) {
            sellerAddress.setCityName(sellerMap.get(9).trim());
        }
        // 门店地址 10
        if(!StringUtil.isEmpty(sellerMap.get(10))) {
            String[] addressArray = sellerMap.get(10).trim().split(",");
            sellerAddress.setProvinceName(addressArray[0]);
            sellerAddress.setCityName(addressArray[1]);
            sellerAddress.setAreaName(addressArray[2]);
            sellerAddress.setAddressInfo(addressArray[3]);

            // 省市区组合
            sellerAddress.setAddAll(addressArray[0] + "," + addressArray[1] + "," + addressArray[2]);
            sellerAddress.setCreateTime(DateUtil.getDate());

            // 查询省市区代码
            Areas provinceAreas = getProvinceAreas(addressArray[0]);
            sellerAddress.setProvinceCode(provinceAreas.getAreaId());

            AreasQuery areasQuery2 = new AreasQuery();
            areasQuery2.or().andNameEqualTo(addressArray[1]).andMergerNameLike('%' + addressArray[0] + "," + addressArray[1] + "%");
            Areas provinceAreas2 = areasMapper.selectByExample(areasQuery2).get(0);
            sellerAddress.setCityCode(provinceAreas2.getAreaId());

            AreasQuery areasQuery3 = new AreasQuery();
            areasQuery3.or().andNameEqualTo(addressArray[2]).andMergerNameLike('%' + addressArray[0] + "," + addressArray[1] + "," + addressArray[2] + "%");
            Areas provinceAreas3 = areasMapper.selectByExample(areasQuery3).get(0);
            sellerAddress.setAreaCode(provinceAreas3.getAreaId());
        }

        // 经度 17
        if(!StringUtil.isEmpty(sellerMap.get(17))) {
            sellerAddress.setLatitude(Double.parseDouble(sellerMap.get(17).trim()));
        }
        // 纬度 18
        if(!StringUtil.isEmpty(sellerMap.get(18))) {
            sellerAddress.setLongitude(Double.parseDouble(sellerMap.get(18).trim()));
        }
        // 保存商户地址信息
        sellerAddress.setSellerId(seller.getId());
        sellerAddressMapper.insert(sellerAddress);
    }

    private void saveSellerCompany(Map<Integer, String> sellerMap, Seller seller) {
        // 商家申请表-商户公司信息 入库实体对象
        SellerCompany sellerCompany = new SellerCompany();
        // 帐期 5
        if(!StringUtil.isEmpty(sellerMap.get(5))) {
           sellerCompany.setAccountPeriod(Integer.parseInt(sellerMap.get(5).trim()));
        }
        // 帐户姓名 11
        if(!StringUtil.isEmpty(sellerMap.get(11))) {
            // 【开户行账户名称】
            sellerCompany.setBankUser(sellerMap.get(11).trim());
        }
        // 开户行 12
        if(!StringUtil.isEmpty(sellerMap.get(12))) {
            sellerCompany.setBankName(sellerMap.get(12).trim());
        }
        // 所属支行 13
        if(!StringUtil.isEmpty(sellerMap.get(13))) {
            sellerCompany.setBankNameBranch(sellerMap.get(13).trim());
        }
        // 银行卡号 14
        if(!StringUtil.isEmpty(sellerMap.get(14))) {
            sellerCompany.setBankCode(sellerMap.get(14).trim());
        }
        // 纳税人识别号 15    【存在异义】 税务登记号
        if(!StringUtil.isEmpty(sellerMap.get(15))) {
            sellerCompany.setTaxLicense(sellerMap.get(15).trim());
        }
        // 企业帐号 16      【存在异义】 营业执照注册号
        if(!StringUtil.isEmpty(sellerMap.get(16))) {
            sellerCompany.setBussinessLicense(sellerMap.get(16).trim());
        }
        // 身份证号 19
        if(!StringUtil.isEmpty((sellerMap.get(19)))) {
            sellerCompany.setResponsiblePersonIdCard(sellerMap.get(19).trim());
        }
        // 手机号 20
        if(!StringUtil.isEmpty((sellerMap.get(20)))) {
            sellerCompany.setResponsiblePersonMobile(sellerMap.get(20).trim());
        }
        // 非空数据暂存如下
        sellerCompany.setSellerId(seller.getId());
        sellerCompany.setState(2); // 不能为空，先初始化为2，再行确认  【1、提交申请；2、审核通过；3、缴纳保证金；4、审核失败】
        sellerCompany.setType(2); // 不能为空，先初始化为2，再行确认  【1、平台自营；2、商家入驻】
        sellerCompany.setAccountType(2);// 【不能为空，先初始化为2，再行确认   1、对公账户；2、个人账户】
        sellerCompanyMapper.insert(sellerCompany);
    }

    private void saveSellerSettlePoint(Map<Integer, String> sellerMap, Seller seller) {
        // 商户结算扣点 入库实体对象
        SellerSettlePoint sellerSettlePoint = new SellerSettlePoint();
        // 签约开始日期 2
        // 风控审核通过时间 3
        // 状态 4
        if(!StringUtil.isEmpty(sellerMap.get(4))) {
            if(sellerMap.get(4).trim().equals("关店")) {
                sellerSettlePoint.setStatus(5);// 审核失败
            } else if(sellerMap.get(4).trim().equals("冻结")){
                sellerSettlePoint.setStatus(3);// 冻结
            } else if(sellerMap.get(4).trim().equals("在线")) {
                sellerSettlePoint.setStatus(2);// 审核通过
            }
        }
        // 扣点费率 5
        if(!StringUtil.isEmpty(sellerMap.get(6))) {
            // 设置扣点比例，去除%
            sellerSettlePoint.setSettlePoint(Double.parseDouble(sellerMap.get(6).trim().replace("%","")));
        }
        // 保存扣点信息
        sellerSettlePoint.setSellerId(seller.getId());
        sellerSettlePoint.setSort(0);
        sellerSettlePointMapper.insert(sellerSettlePoint);
    }

    private Seller saveSeller(Map<Integer, String> sellerMap) {
        // 商户 入库实体对象
        Seller seller = new Seller();
        // 店铺名称 1
        if (!StringUtil.isEmpty(sellerMap.get(1))) {
            seller.setSellerName(sellerMap.get(1).trim());
        }
        // 保存商户信息
        seller.setSellerCode(sellerMap.get(22).trim()); // SellerCode不能为空，未确认从哪里获取，暂时存为UUID字符串
        seller.setAuditStatus(1); // 不能为空，先初始化为1，再行确认
        seller.setAuditStage(1); // 不能为空，先初始化为1，再行确认
        sellerMapper.insert(seller);
        return seller;
    }

    private void converseAndSaveProducts(List<Map<Integer, String>> products) {
        for(int i = 0; i < products.size(); i++) {
            // 产品行数据
            Map<Integer, String> productMap = products.get(i);
            saveProduct(productMap);
        }
    }

    private void saveProduct(Map<Integer, String> productMap) {
        // 入库实体
        ProductWithBLOBs product = new ProductWithBLOBs();
        // 店铺名称 0，这里需要店铺名称查询店铺id
        if(!StringUtil.isEmpty(productMap.get(0))) {
            String sellerName = productMap.get(0).trim();
            // 查询店铺名称查询店铺
            List<Seller> sellers = getDbSellers(sellerName);
            if(!CollectionUtils.isEmpty(sellers) && sellers.size() == 1) {
                product.setSellerId(sellers.get(0).getId());
            }else {
                throw new BusinessException("根据分类名称查询店铺信息异常，描述：，" + sellers == null ? "为空":("记录数：" + sellers.size()));
            }
        } else {
            throw new BusinessException("分类名称不能为空！");
        }
        // 商品名称 1
        if(!StringUtil.isEmpty(productMap.get(1))) {
            product.setName1(productMap.get(1).trim());
        }
        // 分类名称 3，根据分类名反查ID
        List<ProductCate> productCates = null;
        if(!StringUtil.isEmpty(productMap.get(3))) {
            String cateName = productMap.get(3).trim();
            // 根据品类名称查询品类
            productCates = getDbProductCates(cateName);
            if(!CollectionUtils.isEmpty(productCates) && productCates.size() == 1) {
                product.setProductCateId(productCates.get(0).getId());
            }else {
                throw new BusinessException("根据分类名称查询店铺信息异常，描述：，" + productCates == null ? "为空":("记录数：" + productCates.size()));
            }
        } else {
            throw new BusinessException("分类名称不能为空！");
        }

        // 商品品牌 2，根据商品品牌名反查ID
        if(!StringUtil.isEmpty(productMap.get(2))) {
            String brandName = productMap.get(2).trim();
            Integer cateId = productCates.get(0).getId();
            List<ProductBrand> productBrands = getDbProductBrands(brandName, cateId);
            if(!CollectionUtils.isEmpty(productBrands) && productBrands.size() == 1) {
                product.setProductBrandId(productBrands.get(0).getId());
            }else {
                throw new BusinessException("根据品牌名称查询店铺信息异常，描述：，" + productBrands == null ? "为空":("记录数：" + productBrands.size()));
            }
        } else {
            throw new BusinessException("品牌名称不能为空！");
        }
        // 以下是数据库不允许为空的字段，暂赋值如下
        product.setName2("");// 商品促销信息
        product.setKeyword(productMap.get(1)); // 商品关键
        product.setIsSelf(2); // 1、自营；2、商家
        product.setProtectedPrice(BigDecimal.ZERO); // 保护价
        product.setMalMobilePrice(BigDecimal.ZERO); // 成本价
        product.setVirtualSales(0); // 虚拟销量
        product.setActualSales(0);  // 实际销量
        product.setProductStock(0); // 商品库存
        product.setIsNorm(1);   // 1、没有启用规格；2、启用规格
        product.setNormIds(""); // 规格ID集合
        product.setNormName(""); // 规格属性值集合
        product.setState(6);  // 【1、刚创建；2、提交审核；3、审核通过；4、申请驳回；5、商品删除；6、上架；7、下架】
        product.setCreateId(0);
        product.setCreateTime(new Date());
        product.setSellerCateId(0); // 商家分类ID
        product.setSellerState(1);// 【1、店铺正常；2、店铺关闭 默认1】
        product.setCommentsNumber(0); // 评价总数
        product.setProductCateState(1); // 【1、分类正常；2、分类关闭】
        product.setIsInventedProduct(2);// 【1、实物商品；2、虚拟商品】
        product.setMasterImg(""); // 主图
        product.setProductCode("");// 商品编码
        product.setWeights(0); // 权重
        product.setSettlementPrice(BigDecimal.ZERO);// 结算价
        product.setAfterSales(""); // 售后保障
        product.setMasterMiddleImg("");// 主图-中图
        product.setMasterLittleImg("");// 主图-小图
        product.setProductCatePath("");// 商品所属分类路径
        product.setDescription("");// 商品描述信息
        product.setPacking("");// 包装清单
        product.setIdentification(0);// 0一期健身
        productMapper.insert(product);
    }

    private List<ProductBrand> getDbProductBrands(String brandName, Integer cateId) {
        ProductBrandQuery productBrandQuery = new ProductBrandQuery();
        productBrandQuery.or().andNameEqualTo(brandName).andProductCateIdEqualTo(cateId);
        return productBrandMapper.selectByExample(productBrandQuery);
    }

    private List<ProductCate> getDbProductCates(String cateName) {
        List<ProductCate> productCates;
        ProductCateQuery productCateQuery = new ProductCateQuery();
        productCateQuery.or().andNameEqualTo(cateName);
        productCates = productCateMapper.selectByExample(productCateQuery);
        return productCates;
    }

    private List<Seller> getDbSellers(String sellerName) {
        SellerQuery sellerQuery = new SellerQuery();
        sellerQuery.or().andSellerNameEqualTo(sellerName);
        return sellerMapper.selectByExample(sellerQuery);
    }

    private void converseAndSaveProductCateBrandAreases(List<Map<Integer, String>> productCateBrandAreases) {
        for(int i = 0; i < productCateBrandAreases.size(); i++) {
            // 行数据
            Map<Integer, String> productCateBrandAreasMap = productCateBrandAreases.get(i);
            // 保存
            saveProductCateBrandAreas(productCateBrandAreasMap);
        }
    }

    private void saveProductCateBrandAreas(Map<Integer, String> productCateBrandAreasMap) {
        // 入库实体
        ProductCateBrandAreas productCateBrandAreas = new ProductCateBrandAreas();

        // 分类名称 0，根据名称反查id
        List<ProductCate> productCates = null;
        if(!StringUtil.isEmpty(productCateBrandAreasMap.get(0))){
            String cateName = productCateBrandAreasMap.get(0).trim();
            productCates = getDbProductCates(cateName);
            if(!CollectionUtils.isEmpty(productCates) && productCates.size() == 1) {
                productCateBrandAreas.setCateId(productCates.get(0).getId());
            } else {
                throw new BusinessException("根据分类名称查询分类ID异常，描述：，" + productCates == null ? "为空":("记录数：" + productCates.size()));
            }
        } else {
            throw new BusinessException("分类名称不能为空！");
        }

        // 品牌名称 0，根据名称反查id
        if(!StringUtil.isEmpty(productCateBrandAreasMap.get(1))){
            Integer cateId = productCates.get(0).getId();
            String brandName = productCateBrandAreasMap.get(1).trim();
            List<ProductBrand> productBrand = getDbProductBrands(brandName, cateId);
            if(!CollectionUtils.isEmpty(productBrand) && productBrand.size() == 1) {
                productCateBrandAreas.setBrandId(productBrand.get(0).getId());
            } else {
                throw new BusinessException("根据品牌名称查询分类ID异常，描述：，" + productBrand == null ? "为空":("记录数：" + productBrand.size()));
            }
        } else {
            throw new BusinessException("品牌名称不能为空！");
        }

        // 城市， 根据城市名称返查area_id
        if(!StringUtil.isEmpty(productCateBrandAreasMap.get(2))) {
            String cityName = productCateBrandAreasMap.get(2).trim();
            Areas provinceAreas = getProvinceAreas(cityName);
            productCateBrandAreas.setAreaId(provinceAreas.getAreaId());
        }
        // key
        // 区间低价 5
        if(!StringUtil.isEmpty(productCateBrandAreasMap.get(7))) {
            productCateBrandAreas.setIntervalPriceMin(new BigDecimal(productCateBrandAreasMap.get(4).trim()));
        }
        // 区间高价 6
        if(!StringUtil.isEmpty(productCateBrandAreasMap.get(8))) {
            productCateBrandAreas.setIntervalPriceMax(new BigDecimal(productCateBrandAreasMap.get(5).trim()));
        }
        productCateBrandAreasMapper.insert(productCateBrandAreas);
    }

    private Areas getProvinceAreas(String cityName) {
        AreasQuery areasQuery = new AreasQuery();
        areasQuery.or().andNameEqualTo(cityName);
        return areasMapper.selectByExample(areasQuery).get(0);
    }

    private void converseAndSaveSellerLoginPermission(List<Map<Integer, String>> sellerLoginPermissions) {
        for(int i = 0; i < sellerLoginPermissions.size(); i++) {
            // 行数据
            Map<Integer, String> sellerLoginPermissionMap = sellerLoginPermissions.get(i);
            saveSellerLoginPermission(sellerLoginPermissionMap);
        }
    }

    private void saveSellerLoginPermission(Map<Integer, String> sellerLoginPermissionMap) {
        // 入库实体
        SellerLoginPermission sellerLoginPermission = new SellerLoginPermission();
        // 店铺名称 1
        if(!StringUtil.isEmpty(sellerLoginPermissionMap.get(1))) {
            String sellerName = sellerLoginPermissionMap.get(1).trim();
            List<Seller> sellers = getDbSellers(sellerName);
            if(!CollectionUtils.isEmpty(sellers) && sellers.size() == 1) {
                sellerLoginPermission.setSellerId(sellers.get(0).getId());
                sellerLoginPermission.setSellerName(sellers.get(0).getSellerName());
            }else {
                throw new BusinessException("根据店铺名称查询店铺信息异常，描述：，" + sellers == null ? "为空":("记录数：" + sellers.size()));
            }
        } else {
            throw new BusinessException("店铺名称不能为空！");
        }

        // 在线 2
        // 城市 3

        // 手机号 4
        if(!StringUtil.isEmpty(sellerLoginPermissionMap.get(4))) {
            sellerLoginPermission.setSellerMobile(sellerLoginPermissionMap.get(4).trim());
        }
        // 店铺帐户 5
        if(!StringUtil.isEmpty(sellerLoginPermissionMap.get(5))) {
            sellerLoginPermission.setSellerLogin(sellerLoginPermissionMap.get(5).trim());
        }
        // 店铺帐户 6
        if(!StringUtil.isEmpty(sellerLoginPermissionMap.get(6))) {
            sellerLoginPermission.setSellerPassword(sellerLoginPermissionMap.get(6).trim());
        }
        sellerLoginPermissionMapper.insert(sellerLoginPermission);
    }
}
