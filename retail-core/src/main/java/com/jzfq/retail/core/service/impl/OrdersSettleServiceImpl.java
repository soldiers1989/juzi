package com.jzfq.retail.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.vo.req.SellerSearchReq;
import com.jzfq.retail.bean.vo.req.WithdrawCashCallbackReq;
import com.jzfq.retail.bean.vo.req.WithdrawCashReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.OrdersSettleRes;
import com.jzfq.retail.bean.vo.req.OrdersSettleSearch;
import com.jzfq.retail.common.enmu.ForeignInterfaceServiceType;
import com.jzfq.retail.common.enmu.ForeignInterfaceStatus;
import com.jzfq.retail.common.enmu.ForeignInterfaceType;
import com.jzfq.retail.common.enmu.OrdersSettleState;
import com.jzfq.retail.common.util.CodeGenerateUtils;
import com.jzfq.retail.common.util.POIHandler;
import com.jzfq.retail.common.util.date.DateUtil;
import com.jzfq.retail.core.api.service.OrdersSettleService;
import com.jzfq.retail.core.api.service.SellerService;
import com.jzfq.retail.core.api.service.SysUserService;
import com.jzfq.retail.core.call.domain.WithdrawCash;
import com.jzfq.retail.core.call.service.MerchantCapitalAccountService;
import com.jzfq.retail.core.call.service.WithdrawAccountService;
import com.jzfq.retail.core.dao.OrdersBaseMapper;
import com.jzfq.retail.core.dao.OrdersSettleMapper;
import com.jzfq.retail.core.dao.WithdrawRecordMapper;
import com.jzfq.retail.core.dao.manual.OrdersSettleManualMapper;
import com.jzfq.retail.core.service.CallAccountsLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Title: OrdersSettleServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年08月16日 15:53
 * @Description:
 */
@Slf4j
@Service
public class OrdersSettleServiceImpl implements OrdersSettleService {

    @Autowired
    private OrdersSettleManualMapper ordersSettleManualMapper;

    @Autowired
    private OrdersSettleMapper ordersSettleMapper;

    @Autowired
    POIHandler poiHandler;

    @Autowired
    MerchantCapitalAccountService merchantCapitalAccountService;

    @Autowired
    OrdersBaseMapper ordersBaseMapper;

    @Autowired
    SellerService sellerService;

    @Autowired
    WithdrawAccountService withdrawAccountService;

    @Autowired
    WithdrawRecordMapper withdrawRecordMapper;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    private CallAccountsLog allAccountsLog;

    @Value("${accounts.merchant_account.application}")
    private String APPLICATION;

    @Override
    public ListResultRes<OrdersSettleRes> getList(Integer page, Integer pageSize, OrdersSettleSearch search) {
        PageHelper.startPage(page, pageSize);
        Page<OrdersSettleRes> res = ordersSettleManualMapper.findList(search);
        return ListResultRes.newListResult(res.getResult(),res.getTotal(),res.getPageNum(),res.getPageSize());
    }

    @Override
    public List<OrdersSettleRes> getListAll(OrdersSettleSearch search) {
        return ordersSettleManualMapper.findListAll(search);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Integer orderId, String orderSn, BigDecimal productCost,BigDecimal settleMoney) {
        OrdersSettle settle = new OrdersSettle();
        settle.setOrderId(orderId);
        settle.setOrderSn(orderSn);
        settle.setProductCost(productCost);
        settle.setSettleMoney(settleMoney);
        settle.setState(OrdersSettleState._1.getCode());
        settle.setUpdateTime(new Date());
        settle.setSettlePlanTime(DateUtil.addDays(new Date(),14));
        ordersSettleMapper.insert(settle);
        log.info("订单结算添加数据：{}",JSON.toJSON(settle));
    }

    @Override
    public void modifyState(String orderSn, Integer state) {
        OrdersSettleQuery query = new OrdersSettleQuery();
        OrdersSettleQuery.Criteria criteria = query.createCriteria();
        criteria.andOrderSnEqualTo(orderSn);
        List<OrdersSettle> ordersSettleList = ordersSettleMapper.selectByExample(query);
        if(!CollectionUtils.isEmpty(ordersSettleList)){
            OrdersSettle ordersSettle = ordersSettleList.get(0);
            ordersSettle.setState(state);
            ordersSettleMapper.updateByPrimaryKey(ordersSettle);
        }
    }


    @Override
    public void balanceAccount(String orderSn) {
        // 订单结算状态变更为待放款
        modifyState(orderSn, OrdersSettleState._30.getCode());
    }

    @Override
    public void pendingMoney(String orderSn) {
        // 调用核账接口
        withdrawAccountService.checkConfirm(orderSn);
        // 订单结算状态变更为
        modifyState(orderSn, OrdersSettleState._40.getCode());
    }

    @Override
    public ListResultRes<OrdersSettleRes> getAccountList(Integer page, Integer pageSize, String sellerName) {
        SellerSearchReq req = new SellerSearchReq();
        req.setSellerName(sellerName);
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> mapPage = sellerService.getList(page, pageSize, req);
        List<Map<String, Object>> result = mapPage.getResult();
        String businessIds = "";
        for(int i=0;i<result.size();i++){
            Map<String, Object> map = result.get(i);
            String sellerId = (String) map.get("sellerCode");
            businessIds += sellerId;
            if(i<result.size()-1){
                businessIds += ",";
            }
        }
        Map<String, Object> merchantAccountMap = withdrawAccountService.getMerchantAccount(businessIds);
        for(int i=0;i<result.size();i++){
            Map<String, Object> map = result.get(i);
            String sellerCode = (String) map.get("sellerCode");
            Double merchantAccount = (Double) merchantAccountMap.get(sellerCode);
            map.put("merchantAccount", merchantAccount);
        }
        return ListResultRes.newListResult(mapPage.getResult(),mapPage.getTotal(),mapPage.getPageNum(),mapPage.getPageSize());
    }

    @Override
    public void withdrawCash(WithdrawCashReq req, String username) {
        //获取登录用户
        SysUser byNickName = sysUserService.getByNickName(username);
        if(byNickName == null){
            throw new RuntimeException("用户为空");
        }
        WithdrawCash withdrawCash = new WithdrawCash();
        withdrawCash.setAmount(Double.parseDouble(req.getAmount()));
        withdrawCash.setBusinessId(req.getSellerCode());
        withdrawCash.setMerchant(req.getMerchant());
        withdrawCash.setWithdrawId(createWithdrawId());
        withdrawAccountService.withdrawCash(withdrawCash);
        // 记录到表中
        WithdrawRecord record = new WithdrawRecord();
        record.setAmount(Double.parseDouble(req.getAmount()));
        record.setApplication(APPLICATION);
        record.setCreateTime(new Date());
        record.setMerchant(req.getMerchant());
        record.setWithdrawId(withdrawCash.getWithdrawId());
        record.setSellerCode(req.getSellerCode());
        record.setCreateUserId(byNickName.getId());
        record.setCreateUserName(byNickName.getRealName());
        withdrawRecordMapper.insert(record);
    }

    @Override
    public void withdrawCaseCallback(WithdrawCashCallbackReq req) {
        //添加日志
        allAccountsLog.addLog(req.getWithdrawId(), ForeignInterfaceType.REQ.getCode(), ForeignInterfaceServiceType.DESCRIBE_310.toString(), "/merchantAccount/withdrawCashReturn", JSON.toJSONString(req), "", ForeignInterfaceStatus.SUCCESS.getCode(), "提现回调请求报文记录");
        WithdrawRecordQuery query = new WithdrawRecordQuery();
        query.createCriteria().andWithdrawIdEqualTo(req.getWithdrawId());
        List<WithdrawRecord> withdrawRecords = withdrawRecordMapper.selectByExample(query);
        if(!CollectionUtils.isEmpty(withdrawRecords)){
            WithdrawRecord record = withdrawRecords.get(0);
            record.setState(req.getState());
            record.setFailReason(req.getFailReason());
            record.setTransNum(req.getTransNum());
            record.setFinishTime(new Date());
            withdrawRecordMapper.updateByPrimaryKey(record);
        }
    }


    /**
     * 创建提现业务流水号(年月日时分秒毫秒+三位随机数)
     * @return
     */
    private String createWithdrawId() {
        return CodeGenerateUtils.getTimeRandomCode();
    }

}
