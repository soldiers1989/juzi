package com.jzfq.retail.core.service.impl;

import com.juzifenqi.usercenter.entity.member.MemberInfo;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.TelUtil;
import com.jzfq.retail.core.api.service.OpenidRecordService;
import com.jzfq.retail.bean.vo.res.OrderStatisticsRes;
import com.jzfq.retail.core.api.service.ProblemOrderService;
import com.jzfq.retail.core.call.domain.PersonAuthenticationResult;
import com.jzfq.retail.core.call.service.PersonCreditAccountService;
import com.jzfq.retail.core.config.MemberCenterHandler;
import com.jzfq.retail.core.dao.OpenidRecordMapper;
import com.jzfq.retail.core.dao.OrdersBaseMapper;
import com.jzfq.retail.core.dao.manual.ProblemOrderManualMapper;
import com.jzfq.retail.core.dao.manual.OpenidRecordManualMapper;
import com.jzfq.retail.core.dao.manual.OrdersBaseManualMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Map;

import java.util.List;

import java.math.BigDecimal;

/**
 * @Author Liuwei@juzifenqi.com
 * @Date 2018年07月02日 15:02
 * @Description: 订单接口实现
 */
@Slf4j
@Service
public class ProblemOrderServiceImpl implements ProblemOrderService {

    @Autowired
    private OrdersBaseMapper ordersBaseMapper;

    @Autowired
    private ProblemOrderManualMapper problemOrderManualMapper;
    @Autowired
    private OpenidRecordManualMapper openidRecordManualMapper;

    @Autowired
    private OrdersBaseManualMapper ordersBaseManualMapper;

    @Autowired
    private MemberCenterHandler memberCenterHandler;

    @Autowired
    private PersonCreditAccountService personCreditAccountService;


    /**
     * 统计认证失败又认证成功的数量及转化率
     */
    @Override
    public List<Map> statisticsAuthFailSuccessRate(){


        return null;
    }

    @Override
    public Map<String, Object> getMap1() {
        return problemOrderManualMapper.getMap1();
    }

    @Override
    public List<Map<String,Object>> statisticsSellerRebate() {
        return problemOrderManualMapper.statisticsSellerRebate();
    }

    @Override
    public String queryCustomerAuth(String value) {
        //简单根据长度判断是手机号、身份证、姓名，暂不考虑名字超长的情况，比如老外
        String result = "请输入正确的手机号、身份证、姓名进行查询";
        if (value.isEmpty())
            return result;

        MemberInfo memberInfo = null;
        int length = value.length();
        if (length == 11) {
            if (TelUtil.validPhone(value)) {
                memberInfo = memberCenterHandler.getMemberByMobile(value);
            }
        } else if (length == 15 || length == 18) {
            memberInfo = memberCenterHandler.getMemberByIdCard(value);
        } else if (length <=10 ) {
            List<MemberInfo> memberList = memberCenterHandler.getMemberByName(value);
            if (memberList != null && memberList.size()>=1) {
                memberInfo = memberList.get(0);
            }
        } else {
            return result;
        }

        if (memberInfo == null) {
            return result;
        }

        //调用核心是否认证
        String memberId = String.valueOf(memberInfo.getId());
        PersonAuthenticationResult jsob = personCreditAccountService.checkPersonAuthentication(memberId);
        if (jsob == null) {
            // 保存扫码记录
            return TouchApiCode.TOUCH_API_CODE_0006.getMsg();
        }
        log.info("调用核心是否认证结果：{}，memberID：{}", jsob.getCode(), memberId);
        switch (jsob.getCode()) {//返回状态码 0未认证，1认证审核中，2，认证审核成功，3认证审核失败
            case 0:
                return TouchApiCode.TOUCH_API_CODE_0003.getMsg();
            case 1:
                return TouchApiCode.TOUCH_API_CODE_0017.getMsg();
            case 2:
                return TouchApiCode.TOUCH_API_CODE_0008.getMsg();
            case 3:
                return TouchApiCode.TOUCH_API_CODE_0018.getMsg();
            default:
                return TouchApiCode.TOUCH_API_CODE_0006.getMsg();
        }
    }

    @Override
    public OrderStatisticsRes countUpOrderBase() {
        // 统计当日进件数
        Integer entryCount = ordersBaseManualMapper.countEntryOrder();
        // 当日支付成功的用户数
        Integer memberCount = ordersBaseManualMapper.countMember();
        // 当日支付成功总金额
        BigDecimal totalMoney = ordersBaseManualMapper.sumOrderMoney();
        if(totalMoney == null) {
            throw new RuntimeException("未统计到订单总金额，请确认当日是有满足条件的订单");
        }
        BigDecimal mc = new BigDecimal(memberCount);
        // 计算客单价
        BigDecimal perCusTran = totalMoney.divide(mc);

        // 统计总额
        OrderStatisticsRes res = new OrderStatisticsRes();
        res.setEntryCount(entryCount);
        res.setPerCusTran(perCusTran);
        res.setTotalAmount(totalMoney);
        return res;
    }

    @Override
    public Integer countEntryOrder() {
        Integer entryCount = ordersBaseManualMapper.countEntryOrder();
        return entryCount;
    }

    @Override
    public Map<String, Object> getAuthenticationStateOfCountAndRatio() {
        return openidRecordManualMapper.getAuthenticationStateOfCountAndRatio();
    }


}
