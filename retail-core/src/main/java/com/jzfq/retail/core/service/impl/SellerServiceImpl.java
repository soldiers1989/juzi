package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.vo.req.OptionsSearchReq;
import com.jzfq.retail.bean.vo.req.SellerCateBrandRelSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.enmu.SellerStoreTypeEnum;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.dao.SellerImageMapper;
import com.jzfq.retail.core.dao.manual.SellerStoreManualMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.jzfq.retail.core.api.service.SellerAddressService;
import com.jzfq.retail.core.dao.*;
import com.jzfq.retail.core.dao.manual.SellerCateBrandRelManualMapper;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.vo.req.SellerSearchReq;
import com.jzfq.retail.common.enmu.OrderStatus;
import com.jzfq.retail.common.enmu.SellerStatus;
import com.jzfq.retail.core.api.service.OrdersBaseService;
import com.jzfq.retail.core.api.service.SellerService;
import com.jzfq.retail.core.call.service.AssetPlatformService;
import com.jzfq.retail.core.call.service.PersonalCreditAccountService;
import com.jzfq.retail.core.dao.manual.SellerManualMapper;
import com.jzfq.retail.core.service.SystemLogSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhangjianwei@juzifenqi.com
 * @Date 2018年06月29日 17:46
 * @Description: 商户接口实现
 */
@SuppressWarnings("ALL")
@Slf4j
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SystemLogSupport systemLogSupport;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private SellerManualMapper sellerManualMapper;

    @Autowired
    private PersonalCreditAccountService personalCreditAccountService;

    @Autowired
    private AssetPlatformService assetPlatformService;

    @Autowired
    private OrdersBaseService ordersBaseService;

    @Autowired
    private SellerImageMapper sellerImageMapper;

    @Autowired
    private SellerStoreManualMapper sellerStoreManualMapper;

    @Autowired
    private SellerAccountMapper sellerAccountMapper;

    @Autowired
    private SellerAddressMapper sellerAddressMapper;

    @Autowired
    private SellerApprovalMapper sellerApprovalMapper;

    @Autowired
    private SellerCollectAddressMapper sellerCollectAddressMapper;

    @Autowired
    private SellerCompanyMapper sellerCompanyMapper;

    @Autowired
    private SellerLoginPermissionMapper sellerLoginPermissionMapper;

    @Autowired
    private SellerSettlePointMapper sellerSettlePointMapper;

    @Autowired
    private SellerSingleCreditMapper sellerSingleCreditMapper;

    @Autowired
    private SellerStoreMapper sellerStoreMapper;

    @Autowired
    private SellerTermMapper sellerTermMapper;

    @Autowired
    private SellerTypeMapper sellerTypeMapper;

    @Autowired
    private SellerUserMapper sellerUserMapper;

    @Autowired
    private SellerCateBrandRelManualMapper sellerCateBrandRelManualMapper;

    @Override
    public List<Map<String, Object>>  getAllList(SellerSearchReq search) {
        List<Map<String, Object>>  result = sellerManualMapper.findList(search);
        return result;
    }

    @Override
    public Page<Map<String, Object>> getList(Integer page, Integer pageSize, SellerSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = sellerManualMapper.findList(search);
        return listPage;
    }

    @Override
    public ListResultRes<Map<String, Object>> getList2(Integer page, Integer pageSize, SellerSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> pages = sellerManualMapper.findList(search);
        return ListResultRes.newListResult(pages.getResult(), pages.getTotal(), pages.getPageNum(), pages.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Seller seller) {
        sellerMapper.insert(seller);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Seller seller) {
        Seller seller1 = sellerMapper.selectByPrimaryKey(seller.getId());
        if (seller1 == null) {
            throw new RuntimeException("商户不存在");
        }

        sellerMapper.updateByPrimaryKey(seller);
    }

    @Override
    public void delete(Integer id) {
        sellerMapper.deleteByPrimaryKey(id);
    }


    @Override
    public Seller getSellerById(Integer id) {
        return sellerMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean sellerIsFrozen(Integer id) {
        Seller seller = sellerMapper.selectByPrimaryKey(id);
        if (seller == null) {
            throw new RuntimeException("商户不存在");
        }
        if (seller.getAuditStatus() == SellerStatus.ACCOUNT_FROZEN.getCode()) {
            return true;
        }
        return false;
    }


    @Override
    public void updateAuditStatus(Integer id, Integer auditStatus) {
        Seller seller = sellerMapper.selectByPrimaryKey(id);
        if (seller == null) {
            throw new RuntimeException("商户不存在，请确认输入的商户ID是否正确。");
        }
        boolean statusInside = SellerStatus.checkStatus(auditStatus);

        if (!statusInside) {
            throw new RuntimeException("状态码不正确，请确认状态码auditStatus = " + auditStatus + "是否正确。");
        }
        seller.setAuditStatus(auditStatus);
        sellerMapper.updateByPrimaryKey(seller);
    }

    @Override
    public void updateAuditStatus(String code, Integer auditStatus) {
        SellerQuery sellerQuery = new SellerQuery();
        sellerQuery.or().andSellerCodeEqualTo(code);
        Seller seller = sellerMapper.selectByExample(sellerQuery).get(0);
        if (seller == null) {
            throw new RuntimeException("商户不存在，请确认输入的商户ID是否正确。");
        }
        boolean statusInside = SellerStatus.checkStatus(auditStatus);

        if (!statusInside) {
            throw new RuntimeException("状态码不正确，请确认状态码auditStatus = " + auditStatus + "是否正确。");
        }
        seller.setAuditStatus(auditStatus);
        sellerMapper.updateByPrimaryKey(seller);
    }

    @Override
    public void freeze(Integer sellerId, String freezeType) {
        if("freeze".equals(freezeType)){    // 冻结
            sellerFrozen(sellerId);
        }
        if("unfreeze".equals(freezeType)){
            Seller seller = sellerMapper.selectByPrimaryKey(sellerId);
            if(seller == null){
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0049);
            }
            seller.setAuditStatus(2);
            sellerMapper.updateByPrimaryKey(seller);
        }
    }

    @Override
    public List<Map<String, Object>> getOptions(OptionsSearchReq search) {
        return sellerManualMapper.getOptions(search);
    }

    @Override
    public Map<String, Object> getSellerDetailInfo(Integer sellerId) {
        Map<String, Object> map = new HashMap<>();
        // 商户表
        Seller seller = getSellerById(sellerId);
        map.put("seller", seller);
        // 商户店铺地址
        SellerAddress sellerAddress = new SellerAddress();
        SellerAddressQuery sellerAddressQuery = new SellerAddressQuery();
        sellerAddressQuery.createCriteria().andSellerIdEqualTo(sellerId);
        List<SellerAddress> sellerAddressList = sellerAddressMapper.selectByExample(sellerAddressQuery);
        if(!CollectionUtils.isEmpty(sellerAddressList)){
            sellerAddress = sellerAddressList.get(0);
        }
        map.put("sellerAddress", sellerAddress);
        // 商户审批表
        SellerApproval sellerApproval = new SellerApproval();
        SellerApprovalQuery sellerApprovalQuery = new SellerApprovalQuery();
        sellerApprovalQuery.createCriteria().andSellerIdEqualTo(sellerId);
        List<SellerApproval> sellerApprovalList = sellerApprovalMapper.selectByExample(sellerApprovalQuery);
        if(!CollectionUtils.isEmpty(sellerApprovalList)){
            sellerApproval = sellerApprovalList.get(0);
        }
        map.put("sellerApproval", sellerApproval);
        // 商户品类品牌关联表
        SellerCateBrandRelSearchReq sellerCateBrandRelSearchReq = new SellerCateBrandRelSearchReq();
        List<Map<String, Object>> sellerCateBrandList = sellerCateBrandRelManualMapper.getSellerCateBrandRelListBySellerId(sellerId);
        map.put("sellerCateBrandList", sellerCateBrandList);
        // 商家申请表-商户公司信息
        SellerCompany sellerCompany = new SellerCompany();
        SellerCompanyQuery sellerCompanyQuery = new SellerCompanyQuery();
        sellerCompanyQuery.createCriteria().andSellerIdEqualTo(sellerId);
        List<SellerCompany> sellerCompanyList = sellerCompanyMapper.selectByExample(sellerCompanyQuery);
        if(!CollectionUtils.isEmpty(sellerCompanyList)){
            sellerCompany = sellerCompanyList.get(0);
        }
        map.put("sellerCompany", sellerCompany);
        // 商户影像资料表
        SellerImageQuery sellerImageQuery = new SellerImageQuery();
        sellerImageQuery.createCriteria().andSellerIdEqualTo(sellerId);
        List<SellerImage> sellerImageList = sellerImageMapper.selectByExample(sellerImageQuery);
        map.put("sellerImageList", sellerImageList);
        // 商户结算扣点设定表
        SellerSettlePoint sellerSettlePoint = new SellerSettlePoint();
        SellerSettlePointQuery sellerSettlePointQuery = new SellerSettlePointQuery();
        sellerSettlePointQuery.createCriteria().andSellerIdEqualTo(sellerId);
        List<SellerSettlePoint> sellerSettlePointList = sellerSettlePointMapper.selectByExample(sellerSettlePointQuery);
        if(!CollectionUtils.isEmpty(sellerSettlePointList)){
            sellerSettlePoint = sellerSettlePointList.get(0);
        }
        map.put("sellerSettlePoint", sellerSettlePoint);
        // 商户单笔授信设定表
        SellerSingleCredit sellerSingleCredit = new SellerSingleCredit();
        SellerSingleCreditQuery sellerSingleCreditQuery = new SellerSingleCreditQuery();
        sellerSingleCreditQuery.createCriteria().andSellerIdEqualTo(sellerId);
        List<SellerSingleCredit> sellerSingleCreditList = sellerSingleCreditMapper.selectByExample(sellerSingleCreditQuery);
        if(!CollectionUtils.isEmpty(sellerSingleCreditList)){
            sellerSingleCredit = sellerSingleCreditList.get(0);
        }
        map.put("sellerSingleCredit", sellerSingleCredit);
        // 商户门店经营基本信息表
        SellerStore sellerStore = new SellerStore();
        SellerStoreQuery sellerStoreQuery = new SellerStoreQuery();
        sellerStoreQuery.createCriteria().andSellerIdEqualTo(sellerId);
        List<SellerStore> sellerStoreList = sellerStoreMapper.selectByExample(sellerStoreQuery);
        if(!CollectionUtils.isEmpty(sellerStoreList)){
            sellerStore = sellerStoreList.get(0);
        }
        map.put("sellerStore", sellerStore);
        // 商户分期设定表
        SellerTerm sellerTerm = new SellerTerm();
        SellerTermQuery sellerTermQuery = new SellerTermQuery();
        sellerTermQuery.createCriteria().andSellerIdEqualTo(sellerId);
        List<SellerTerm> sellerTermList = sellerTermMapper.selectByExample(sellerTermQuery);
        if(!CollectionUtils.isEmpty(sellerTermList)){
            sellerTerm = sellerTermList.get(0);
        }
        map.put("sellerTerm", sellerTerm);
        return map;
    }

    /**
     * 商户冻结
     * @param id
     */
    private void sellerFrozen(Integer id){
        Seller seller = sellerMapper.selectByPrimaryKey(id);
        if (seller == null) {
            throw new RuntimeException("商户不存在，请确认输入的商户ID是否正确。");
        }
        /**冻结后需要将在途订单处理掉
         *1、查询商户所有订单  改成 200已取消
         *2、处理以下状态的订单 100待确认；110用户已确认，待支付；120待支付；130支付成功，交易复核中；140交易复核通过，待资匹；150交易复核失败；155资匹成功，待交货
         *3、 110 -> 200,110 -> 200,120 -> 200,130 -> 200(调用恢复信用额度接口),140 -> 200（调用资匹回退接口、恢复信用额度接口）,150 -> 200（调用恢复信用额度接口）,155 -> 200（调用资匹回退接口、调用恢复信用额度接口）
         */
        //查询商户所有订单
        OrdersBase order = new OrdersBase();
        order.setSellerId(id);
        List<Map<String, Object>> list = ordersBaseService.findOrderAndUserList(order);
        //订单改成200
        ordersBaseService.modifyBySellerId(id);
        for(Map<String, Object> orders : list){
            Object orderState = orders.get("order_state");
            if(orderState != null){
                //订单状态
                Integer orderState_ = Integer.parseInt(orderState.toString());
                //订单用户的身份证
                String certNo = orders.get("id_card") != null ? orders.get("id_card").toString() : "";
                //订单总金额
                BigDecimal moneyOrder = orders.get("money_order") != null ? new BigDecimal(orders.get("money_order").toString()) : new BigDecimal("0");
                //订单号
                String orderSn = orders.get("order_sn") != null ? orders.get("order_sn").toString() : "";
                String memberId = orders.get("member_id") != null ? orders.get("member_id").toString() : "";
                if(orderState_ == OrderStatus.ORDER_STATE_130.getCode() || orderState_ == OrderStatus.ORDER_STATE_150.getCode()){
                    //恢复信用额度接口
                    personalCreditAccountService.recoverByCertNo(memberId, "008", moneyOrder, orderSn);

                } else if(orderState_ == OrderStatus.ORDER_STATE_140.getCode() || orderState_ == OrderStatus.ORDER_STATE_155.getCode()){
                    //恢复信用额度接口
                    personalCreditAccountService.recoverByCertNo(memberId, "008", moneyOrder, orderSn);
                    //调用资匹回退接口
                    assetPlatformService.closeOrder(orderSn);
                }
                if(orderState_ > OrderStatus.ORDER_STATE_110.getCode()){
                    //判断此订单是否有冻结库存，如果有，则释放库存

                }
                //添加订单操作日志
                systemLogSupport.orderLogSave(null, null, Integer.parseInt(orders.get("id").toString()), orderSn, OrderStatus.getMsgByCode(order.getOrderState()), 200);
            }
        }
        seller.setAuditStatus(SellerStatus.ACCOUNT_FROZEN.getCode());
        sellerMapper.updateByPrimaryKey(seller);
    }

    @Transactional
    @Override
    public void addSellerImage(Integer sellerId, String[] urlList, String proName) {
        Seller seller = sellerMapper.selectByPrimaryKey(sellerId);
        SellerStore sellerStore = sellerStoreManualMapper.selectBySellerId(sellerId);
        if(seller == null || sellerStore == null){
            throw new RuntimeException("未查询到商户信息");
        }
        String storeType = sellerStore.getStoreType();
        if(StringUtils.isBlank(storeType)){
            throw new RuntimeException("店铺类型错误");
        }

        Seller newSeller = new Seller();
        newSeller.setId(seller.getId());
        newSeller.setProName(proName);
        sellerMapper.updateByPrimaryKeySelective(newSeller);
        String sellerStoreType = "";
        if(SellerStoreTypeEnum.STORE_TYPE_5.getCode().equals(storeType)){
            sellerStoreType = SellerStoreTypeEnum.STORE_TYPE_5.getMessage() + "店铺装修";
        }else{
            sellerStoreType = SellerStoreTypeEnum.STORE_TYPE_6.getMessage() + "店铺装修";
        }
        for(String url : urlList){
            SellerImage sellerImage = new SellerImage();
            sellerImage.setSellerId(sellerId);
            sellerImage.setSellerCode(seller.getSellerCode());
            sellerImage.setUrl(url);
            sellerImage.setImageName("店铺装修");
            sellerImage.setDelFlag(0);
            sellerImage.setCreateTime(new Date());
            sellerImage.setImageType(sellerStoreType);
            sellerImageMapper.insert(sellerImage);
        }
    }
}
