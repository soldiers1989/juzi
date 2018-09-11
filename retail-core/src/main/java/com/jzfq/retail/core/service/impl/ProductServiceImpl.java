package com.jzfq.retail.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.vo.req.*;
import com.jzfq.retail.common.enmu.ProductStatus;
import com.jzfq.retail.common.enmu.SellerStoreTypeEnum;
import com.jzfq.retail.common.util.CateAttrUtils;
import com.jzfq.retail.core.dao.*;
import com.jzfq.retail.core.dao.manual.ProductGoodsManualMapper;
import com.jzfq.retail.core.dao.manual.ProductImageManualMapper;
import com.jzfq.retail.core.dao.manual.SellerStoreManualMapper;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.vo.res.ImportExcelResult;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.exception.BadRequestException;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.util.POIHandler;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.api.service.ProductBrandService;
import com.jzfq.retail.core.api.service.ProductCateService;
import com.jzfq.retail.core.api.service.ProductService;
import com.jzfq.retail.core.dao.manual.ProductManualMapper;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年07月02日 15:02
 * @Description: 商品接口实现
 */
@SuppressWarnings("ALL")
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductManualMapper productManualMapper;

    @Autowired
    private POIHandler poiHandler;

    @Autowired
    private ProductCateService productCateService;

    @Autowired
    private ProductBrandService productBrandService;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private SellerStoreManualMapper sellerStoreManualMapper;

    @Autowired
    private ProductGoodsManualMapper productGoodsManualMapper;

    @Autowired
    private ProductGoodsMapper productGoodsMapper;

    @Autowired
    private ProductImageMapper productImageMapper;

    @Autowired
    private ProductImageManualMapper productImageManualMapper;

    @Override
    public List<Map<String, Object>> getAllList(ProductSearchReq search) {
        List<Map<String, Object>> result = productManualMapper.findList(search);
        return result;
    }

    @Override
    public ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, ProductSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = productManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ProductWithBLOBs product) {
        productMapper.insert(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ProductWithBLOBs product) {
        productMapper.updateByPrimaryKeyWithBLOBs(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public ProductWithBLOBs getProductById(Integer id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public Map<String, List<ImportExcelResult>> importExcel(MultipartFile file) {
//        productValid.importExcelValid(file);
        Map<String, List<ImportExcelResult>> result = new HashedMap();
        List<Map<Integer, String>> rows = poiHandler.readExcel(file);
        List<ImportExcelResult> successList = new ArrayList<>();
        List<ImportExcelResult> failList = new ArrayList<>();
        if(rows.size() == 0){
            throw new BadRequestException("操作失败，原因：Excel无数据");
        }
        for(int i=0;i<rows.size();i++){
            ImportExcelResult importExcelResponse = null;//productValid.importExcelRowValid(i + 2, rows.get(i));
            if("操作失败".equals(importExcelResponse.getRes())){
                failList.add(importExcelResponse);
            } else {
                Map<Integer, String> map = rows.get(i);
                try {
                    ProductWithBLOBs product = buildProduct(map);
                    create(product);
                    importExcelResponse.setRes("操作成功");
                    importExcelResponse.setMessage("操作成功");
                    successList.add(importExcelResponse);
                }catch (Exception e) {
                    ImportExcelResult importExcelValidResponse = new ImportExcelResult();
                    importExcelValidResponse.setRowNo(i + 2);
                    importExcelValidResponse.setData(map);
                    importExcelValidResponse.setMessage(e.getMessage());
                    failList.add(importExcelResponse);
                }
            }
        }
        result.put("successList", successList);
        result.put("failList", failList);
        return result;
    }

    public ProductWithBLOBs buildProduct(Map<Integer, String> map){
        ProductWithBLOBs product = new ProductWithBLOBs();
        // `product_cate_id` int(11) NOT NULL COMMENT '分类ID',
        if(StringUtils.isNotBlank(map.get(0))){
            product.setProductCateId(Integer.parseInt(map.get(0)));
            ProductCate productCate = productCateService.getEntityById(product.getProductCateId());
            if(productCate == null){
                throw new BusinessException("分类ID不正确");
            }
        }
        //  `product_cate_path` varchar(50) NOT NULL COMMENT '商品所属分类路径（只包含父路径）',
        product.setProductCatePath(map.get(1));
        // `name1` varchar(200) NOT NULL DEFAULT '' COMMENT '商品名称建议50个字符',
        product.setName1(map.get(2));
        // `name2` text NOT NULL COMMENT '商品促销信息（建议100个字符）',
        product.setName2(map.get(3));
        //  `keyword` varchar(200) NOT NULL COMMENT '商品关键字，用于检索商品，用逗号分隔',
        product.setKeyword(map.get(4));
        //  `product_brand_id` int(11) NOT NULL COMMENT '品牌ID',
        if(StringUtils.isNotBlank(map.get(5))){
            product.setProductBrandId(Integer.parseInt(map.get(5)));
            ProductBrand productBrand = productBrandService.getEntityById(product.getProductBrandId());
            if(productBrand == null){
                throw new BusinessException("品牌ID不正确");
            }
        }
        //  `is_self` int(4) NOT NULL COMMENT '1、自营；2、商家',
        if(StringUtils.isNotBlank(map.get(6))){
            product.setIsSelf(Integer.parseInt(map.get(6)));
        }
        //  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
        if(StringUtils.isNotBlank(map.get(7))){
            product.setCostPrice(new BigDecimal(map.get(7)));
        }
        //  `protected_price` decimal(10,2) NOT NULL COMMENT '保护价，最低价格不能低于',
        if(StringUtils.isNotBlank(map.get(8))){
            product.setProtectedPrice(new BigDecimal(map.get(8)));
        }
        //  `market_price` decimal(10,2) DEFAULT NULL COMMENT '市场价',
        if(StringUtils.isNotBlank(map.get(9))){
            product.setMarketPrice(new BigDecimal(map.get(9)));
        }
        //  `mall_pc_price` decimal(10,2) DEFAULT NULL COMMENT '商城价',
        if(StringUtils.isNotBlank(map.get(10))){
            product.setMallPcPrice(new BigDecimal(map.get(10)));
        }
        //  `mal_mobile_price` decimal(10,2) NOT NULL COMMENT '商城价Mobile',
        if(StringUtils.isNotBlank(map.get(11))){
            product.setMalMobilePrice(new BigDecimal(map.get(11)));
        }
        //  `virtual_sales` int(11) NOT NULL COMMENT '虚拟销量',
        if(StringUtils.isNotBlank(map.get(12))){
            product.setVirtualSales(Integer.parseInt(map.get(12)));
        }
        //  `actual_sales` int(11) NOT NULL COMMENT '实际销量',
        if(StringUtils.isNotBlank(map.get(13))){
            product.setActualSales(Integer.parseInt(map.get(13)));
        }
        //  `product_stock` int(11) NOT NULL COMMENT '商品库存',
        if(StringUtils.isNotBlank(map.get(14))){
            product.setProductStock(Integer.parseInt(map.get(14)));
        }
        //  `is_norm` int(4) NOT NULL COMMENT '1、没有启用规格；2、启用规格',
        if(StringUtils.isNotBlank(map.get(15))){
            product.setIsNorm(Integer.parseInt(map.get(15)));
        }
        //  `norm_ids` varchar(255) NOT NULL COMMENT '规格ID集合',
        product.setNormIds(map.get(16));
        //  `norm_name` varchar(255) NOT NULL DEFAULT '' COMMENT '规格属性值集合 空',
        product.setNormName(map.get(17));
        //  `state` int(3) NOT NULL DEFAULT '0' COMMENT '1、刚创建；2、提交审核；3、审核通过；4、申请驳回；5、商品删除；6、上架；7、下架',
        if(StringUtils.isNotBlank(map.get(18))){
            product.setState(Integer.parseInt(map.get(18)));
        }
        //  `is_top` int(4) DEFAULT NULL COMMENT '1、不推荐；2、推荐',
        if(StringUtils.isNotBlank(map.get(19))){
            product.setIsTop(Integer.parseInt(map.get(19)));
        }
        //  `up_time` datetime DEFAULT NULL COMMENT '商品上架时间',
        if(StringUtils.isNotBlank(map.get(20))){
            Date upTime = DateUtil.formatting(map.get(20), DateUtil.FORMATTING_DATETIME);
            product.setUpTime(upTime);
        }
        //  `description` text NOT NULL COMMENT '商品描述信息',
        product.setDescription(map.get(21));
        //  `packing` text NOT NULL COMMENT '包装清单',
        product.setPacking(map.get(22));
        //  `seller_id` int(11) NOT NULL COMMENT '商家ID',
        if(StringUtils.isNotBlank(map.get(23))){
            product.setSellerId(Integer.parseInt(map.get(23)));
        }
        //  `create_id` int(11) NOT NULL COMMENT '创建人',
        if(StringUtils.isNotBlank(map.get(24))){
            product.setCreateId(Integer.parseInt(map.get(24)));
        }
        //  `up_user_id` int(11) DEFAULT NULL COMMENT '上架人id',
        if(StringUtils.isNotBlank(map.get(25))){
            product.setUpUserId(Integer.parseInt(map.get(25)));
        }
        //  `create_time` datetime NOT NULL COMMENT '创建时间',
        if(StringUtils.isNotBlank(map.get(26))){
            Date createTime = DateUtil.formatting(map.get(26), DateUtil.FORMATTING_DATETIME);
            product.setCreateTime(createTime);
        }
        //  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
        if(StringUtils.isNotBlank(map.get(27))){
            Date updateTime = DateUtil.formatting(map.get(27), DateUtil.FORMATTING_DATETIME);
            product.setCreateTime(updateTime);
        }
        //  `seller_cate_id` int(11) NOT NULL COMMENT '商家分类ID',
        if(StringUtils.isNotBlank(map.get(28))){
            product.setSellerCateId(Integer.parseInt(map.get(28)));
        }
        //  `seller_is_top` int(4) DEFAULT NULL COMMENT '商品推荐，1、不推荐；2、推荐',
        if(StringUtils.isNotBlank(map.get(29))){
            product.setSellerIsTop(Integer.parseInt(map.get(29)));
        }
        //  `seller_state` int(4) NOT NULL COMMENT '店铺状态：1、店铺正常；2、店铺关闭 默认1',
        if(StringUtils.isNotBlank(map.get(30))){
            product.setSellerState(Integer.parseInt(map.get(30)));
        }
        //  `comments_number` int(11) NOT NULL DEFAULT '0' COMMENT '评价总数',
        if(StringUtils.isNotBlank(map.get(31))){
            product.setCommentsNumber(Integer.parseInt(map.get(31)));
        }
        //  `product_cate_state` int(4) NOT NULL COMMENT '平台商品分类状态：1、分类正常；2、分类关闭 默认1',
        if(StringUtils.isNotBlank(map.get(32))){
            product.setProductCateState(Integer.parseInt(map.get(32)));
        }
        //  `is_invented_product` int(4) NOT NULL COMMENT '是否是虚拟商品：1、实物商品；2、虚拟商品',
        if(StringUtils.isNotBlank(map.get(33))){
            product.setIsInventedProduct(Integer.parseInt(map.get(33)));
        }
        //  `transport_type` int(1) DEFAULT NULL COMMENT '运费计算类型：1、按件，2、按重量，3、按体积',
        if(StringUtils.isNotBlank(map.get(34))){
            product.setTransportType(Integer.parseInt(map.get(34)));
        }
        //  `transport_id` int(11) DEFAULT NULL COMMENT '运费模板id',
        if(StringUtils.isNotBlank(map.get(35))){
            product.setTransportId(Integer.parseInt(map.get(35)));
        }
        //  `master_img` varchar(200) NOT NULL DEFAULT '' COMMENT '主图',
        product.setMasterImg(map.get(36));
        //  `product_code` varchar(50) NOT NULL DEFAULT '' COMMENT '商品编码',
        product.setProductCode(map.get(37));
        //  `short_name` varchar(200) DEFAULT NULL COMMENT '商品短标题',
        product.setShortName(map.get(38));
        //  `weights` int(11) NOT NULL COMMENT '权重,默认为1,权重越大越靠前',
        if(StringUtils.isNotBlank(map.get(39))){
            product.setWeights(Integer.parseInt(map.get(39)));
        }
        //  `settlement_price` decimal(10,2) NOT NULL COMMENT '结算价',
        if(StringUtils.isNotBlank(map.get(40))){
            product.setSettlementPrice(new BigDecimal(map.get(40)));
        }
        //  `off_time` datetime DEFAULT NULL COMMENT '商品下架时间',
        if(StringUtils.isNotBlank(map.get(41))){
            Date offTime = DateUtil.formatting(map.get(41), DateUtil.FORMATTING_DATETIME);
            product.setOffTime(offTime);
        }
        //  `after_sales` text NOT NULL COMMENT '售后保障',
        product.setAfterSales(map.get(42));
        //  `m_floor_data_id` int(11) DEFAULT NULL COMMENT '关联新增活动楼层数据外键',
        if(StringUtils.isNotBlank(map.get(43))){
            product.setmFloorDataId(Integer.parseInt(map.get(43)));
        }
        //  `master_middle_img` varchar(200) NOT NULL DEFAULT '' COMMENT '主图-中图',
        product.setMasterMiddleImg(map.get(44));
        //  `master_little_img` varchar(200) NOT NULL DEFAULT '' COMMENT '主图-小图',
        product.setMasterLittleImg(map.get(45));
        //  `tag_fall` varchar(100) DEFAULT NULL COMMENT '直降标签',
        product.setTagFall(map.get(46));
        //  `tag_free` varchar(100) DEFAULT NULL COMMENT '免息标签',
        product.setTagFree(map.get(47));
        //  `source_description` varchar(255) DEFAULT NULL COMMENT '货源描述',
        product.setSourceDescription(map.get(48));
        //  `is_full_payment` int(4) DEFAULT NULL COMMENT '是否可全款 0-可以 1-不可以',
        if(StringUtils.isNotBlank(map.get(49))){
            product.setIsFullPayment(Integer.parseInt(map.get(49)));
        }
        //  `is_by_stages` int(4) DEFAULT NULL COMMENT '是否可分期 0-可以 1-不可以',
        if(StringUtils.isNotBlank(map.get(50))){
            product.setIsByStages(Integer.parseInt(map.get(50)));
        }
        //  `is_search` int(4) DEFAULT NULL COMMENT '是否可搜索 0-可以 1-不可以',
        if(StringUtils.isNotBlank(map.get(51))){
            product.setIsSearch(Integer.parseInt(map.get(51)));
        }
        //  `is_pickself` int(11) DEFAULT NULL COMMENT '是否自提  0-可以自提  1-不可以自提',
        if(StringUtils.isNotBlank(map.get(52))){
            product.setIsPickself(Integer.parseInt(map.get(52)));
        }
        //  `remark4` varchar(255) DEFAULT NULL COMMENT '备用字段4',
        product.setRemark4(map.get(53));
        //  `remark5` varchar(255) DEFAULT NULL COMMENT '备用字段5',
        product.setRemark5(map.get(54));
        //  `remark6` varchar(255) DEFAULT NULL COMMENT '备用字段6',
        product.setRemark6(map.get(55));
        //  `remark7` varchar(255) DEFAULT NULL COMMENT '备用字段7',
        product.setRemark7(map.get(56));
        //  `spec_packing` text COMMENT '旧数据 规格包装数据',
        product.setSpecPacking(map.get(57));
        //  `is_category` int(4) DEFAULT NULL COMMENT '是否可分类  0-可以 1-不可以',
        if(StringUtils.isNotBlank(map.get(58))){
            product.setIsCategory(Integer.parseInt(map.get(58)));
        }
        //  `limits` int(11) DEFAULT NULL COMMENT '限制购买数量',
        if(StringUtils.isNotBlank(map.get(59))){
            product.setLimits(Integer.parseInt(map.get(59)));
        }
        //  `delivery_channel` varchar(1) DEFAULT NULL COMMENT '发货渠道：1自营 2考拉 3京东',
        product.setDeliveryChannel(map.get(60));
        //  `identification` int(11) DEFAULT NULL COMMENT '商品标识：0普通商品，1企业购商品',
        if(StringUtils.isNotBlank(map.get(61))){
            product.setIdentification(Integer.parseInt(map.get(61)));
        }
        //  `huohao` varchar(20) DEFAULT NULL COMMENT '货号',
        product.setHuohao(map.get(62));
        return product;
    }

    @Override
    public ListResultRes<Map<String, Object>> getList(Integer page, Integer pageSize, Integer sellerId,Integer[] states) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = productManualMapper.findListBySellerId(sellerId,states);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    public JSONArray getProductNames(Integer sellerId) {
        Seller seller = sellerMapper.selectByPrimaryKey(sellerId);
        SellerStore sellerStore = sellerStoreManualMapper.selectBySellerId(sellerId);
        if(seller == null || sellerStore == null){
            throw new RuntimeException("未查询到此店铺信息");
        }
        String storeType = sellerStore.getStoreType();
        if(StringUtils.isBlank(storeType)){
            throw new RuntimeException("此店铺不可进行店铺装修");
        }
        JSONArray jsonArray = new JSONArray();
        if(SellerStoreTypeEnum.STORE_TYPE_5.getCode().equals(storeType)){//扫码店
            List<Map<String,Object>> list = productManualMapper.findListBySellerIdAndIdentification(sellerId, 0, 6);
            for(Map<String,Object> product1 : list){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", product1.get("id"));
                jsonObject.put("name", product1.get("name"));
                jsonArray.add(jsonObject);
            }
        }else if(SellerStoreTypeEnum.STORE_TYPE_6.getCode().equals(storeType)){//店中店
            List<ProductGoods> list = productGoodsManualMapper.selectBySellerId(sellerId);
            for(ProductGoods productGoods : list){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", productGoods.getId());
                jsonObject.put("name", productGoods.getSkuName());
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer productOnShelf(ProductOnShelfReq productOnShelfReq) {
        Integer result = 0;
        Seller seller = sellerMapper.selectByPrimaryKey(productOnShelfReq.getSellerId());
        SellerStore sellerStore = sellerStoreManualMapper.selectBySellerId(productOnShelfReq.getSellerId());
        if(seller == null || sellerStore == null){
            throw new RuntimeException("未查询到此店铺信息");
        }
        String storeType = sellerStore.getStoreType();
        if(StringUtils.isBlank(storeType)){
            throw new RuntimeException("未查询到此店铺信息");
        }
        if(productOnShelfReq.getOperationType() != 1 && productOnShelfReq.getOperationType() != 2){
            throw new RuntimeException("operationType is error");
        }
        Product product = productMapper.selectByPrimaryKey(productOnShelfReq.getProductId());
        if(product == null || productOnShelfReq.getSellerId().intValue() != product.getSellerId().intValue()){
            throw new RuntimeException("商户ID和商品ID不匹配");
        }else if(ProductStatus.AUDITING_SUCCESS.getCode() != product.getState()){
            throw new RuntimeException("该商品不是待上架的状态");
        }
        String normName = "";
        try {
            JSONArray jsonArray = CateAttrUtils.getNormName(productOnShelfReq.getAttrParams());
            if(jsonArray.size() > 8){
                throw new RuntimeException("Goods should not be more than 8 when they are on shelves.");
            }
            normName = JSON.toJSONString(jsonArray);
        } catch (Exception e){
            throw new RuntimeException("参数传入错误");
        }
        ProductWithBLOBs productWithBLOBs = new ProductWithBLOBs();
        productWithBLOBs.setId(productOnShelfReq.getProductId());
        if(productOnShelfReq.getOperationType() == 2){
            productWithBLOBs.setState(ProductStatus.EFFECTIVE.getCode());//上架
        }
        productWithBLOBs.setNormName(normName);
        productWithBLOBs.setProLabel(productOnShelfReq.getProLabel());
        productWithBLOBs.setRemark4(productOnShelfReq.getRemark());
        try {
            Date upDate = DateUtil.parseTime(productOnShelfReq.getUpTime());
            Date offDate = DateUtil.parseTime(productOnShelfReq.getOffTime());
            productWithBLOBs.setUpTime(upDate);
            productWithBLOBs.setOffTime(offDate);
        } catch (ParseException e) {
            throw new RuntimeException("data param is error");
        }
        productWithBLOBs.setUpdateTime(new Date());
        result = productMapper.updateByPrimaryKeySelective(productWithBLOBs);
        if(result == 1){
            productImageManualMapper.deleteByProductId(productOnShelfReq.getProductId());
            List<ProductOnShelfReq.ProductImageList> list = productOnShelfReq.getImageList();
            for(ProductOnShelfReq.ProductImageList ProductImage : list){
                ProductImage productImage = new ProductImage();
                productImage.setProductId(productOnShelfReq.getProductId());
                productImage.setType(1);
                productImage.setUrl(ProductImage.getUrl());
                productImage.setSort(ProductImage.getSort());
                productImage.setRemark(ProductImage.getRemark());
                productImage.setStatus(0);
                productImage.setCreateTime(new Date());
                productImageMapper.insertSelective(productImage);
            }
        }
        return result;
    }
}
