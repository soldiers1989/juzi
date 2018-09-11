package com.jzfq.retail.core.config;

import com.juzifenqi.core.ServiceResult;
import com.juzifenqi.usercenter.entity.member.MemberInfo;
import com.juzifenqi.usercenter.entity.score.MemberScoreFraudmetrix;
import com.juzifenqi.usercenter.service.authorization.IShopPassportService;
import com.juzifenqi.usercenter.service.member.IMemberShopService;
import com.juzifenqi.usercenter.service.score.IMemberScoreFraudMetrixService;
import com.juzifenqi.usercenter.vo.WxLoginInfo;
import com.jzfq.auth.core.api.JzfqAuthApi;
import com.jzfq.auth.core.api.dto.JzfqAuthInfoDto;
import com.jzfq.auth.core.api.entiy.AuthResult;
import com.jzfq.auth.core.api.vo.JsonResult;
import com.jzfq.retail.bean.domain.OrdersBase;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.util.IPUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Title: MemberCenterHandler
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Liuwei@juzifenqi.com
 * @Date 2018年08月13日 10:05
 * @Description:
 */
@Slf4j
@Component
public class MemberCenterHandler {

    @Autowired
    private IShopPassportService iShopPassportService;
    @Autowired
    private IMemberShopService memberShopService;
    @Autowired
    private JzfqAuthApi jzfqAuthApi;
    @Autowired
    private IMemberScoreFraudMetrixService iMemberScoreFraudMetrixService;
    /**
     * 调用用户中心获取数据
     */
    public ServiceResult<WxLoginInfo> validateChatLogin_(String wxCode){
        log.info("MemberCenterHandler 调用户中心接口，wxCode:{}" ,wxCode);
        ServiceResult<WxLoginInfo> mbi = iShopPassportService.validateChatLogin(wxCode, IPUtil.getServerIp());
        log.info("MemberCenterHandler 调用户中心接口：mbi-getSuccess：" + mbi.getSuccess());
        log.info("MemberCenterHandler 调用户中心接口：mbi-getCode：" + mbi.getCode());
        return mbi;
    }

    /**
     * 调用用户中心获取数据
     */
    public ServiceResult<WxLoginInfo> validateChatLogin(String wxCode){
        ServiceResult<WxLoginInfo> mbi = validateChatLogin_(wxCode);
        if (mbi == null || !mbi.getSuccess() || mbi.getResult() == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0007);
        }
        if (!mbi.getSuccess() && (mbi.getCode().equals("WX_ERR:210003") || mbi.getCode().equals("WX_ERR:210004"))) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0001);
        }
        if (!mbi.getSuccess() || mbi.getResult() == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0005);
        }
        log.info("MemberCenterHandler 调用户中心接口：mbi-getSuccess：" + mbi.getSuccess());
        log.info("MemberCenterHandler 调用户中心接口：mbi-getCode：" + mbi.getCode());
        return mbi;
    }

    /**
     * 请求用户中心-根据微信code获取openId
     */
    public String wxAuthorizing4Business(String wxCode){
        //1、拿wxCode到微信端换取openID
        log.info("认证成功后回调-请求用户中心-根据微信code获取openId，微信code:{}", wxCode);
        ServiceResult<String> openIdResult = iShopPassportService.wxAuthorizing4Business(wxCode);
        log.info("认证成功后回调-请求用户中心-根据微信code获取openId，微信cod:{}，返回结果：{}", wxCode, openIdResult.toString());
        if (openIdResult == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0004);
        }
        if (openIdResult.getResult() == null) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0001);
        }
        String openId = openIdResult.getResult();
        return openId;
    }

    /**
     * 通过wxCode获取用户中心用户对象
     */
    public MemberInfo getMemberByWxCode(String wxCode){
        ServiceResult<WxLoginInfo> mbi = validateChatLogin(wxCode);
        Integer memberId = mbi.getResult().getMemberId();
        ServiceResult<MemberInfo> result = memberShopService.getMemberById(memberId);
        if (result == null || !result.getSuccess()) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0004);
        }
        if(result.getResult() == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1355);
        }
        MemberInfo member = result.getResult();
        return member;
    }

    /**
     * 通过手机号获取用户中心用户对象
     */
    public MemberInfo getMemberByMobile(String mobile){
        ServiceResult<MemberInfo> result = memberShopService.getMemberByMobile(mobile);
        if (result == null || !result.getSuccess()) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0004);
        }
        if(result.getResult() == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1355);
        }
        MemberInfo member = result.getResult();
        return member;
    }

    /**
     * 通过身份证获取用户中心用户对象
     */
    public MemberInfo getMemberByIdCard(String idCard){
        ServiceResult<MemberInfo> result = memberShopService.getMemberByIdcard(idCard);
        if (result == null || !result.getSuccess()) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0004);
        }
        if(result.getResult() == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1355);
        }
        MemberInfo member = result.getResult();
        return member;
    }

    /**
     * 通过姓名获取用户中心用户对象
     */
    public List<MemberInfo> getMemberByName(String name){
        ServiceResult<List<MemberInfo>> result = memberShopService.getMemberByName(name);
        if (result == null || !result.getSuccess()) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0004);
        }
        if(result.getResult() == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1355);
        }
        List<MemberInfo> memberList = result.getResult();
        return memberList;
    }

    /**
     * 通过memberId获取用户中心用户对象
     */
    public MemberInfo getMemberById(Integer memberId){
        // 获取提交订单的用户 调用用户中心方法(根据用户id获取用户对象)
        log.info("commitPay-通过member_id 到用户中心获取身份证号码等其他信息start,member_id:{}", memberId);
        ServiceResult<MemberInfo> result = memberShopService.getMemberById(memberId);
        log.info("commitPay-通过member_id 到用户中心获取身份证号码等其他信息end,member_id:{}, result:{}", memberId, result.toString());
        if (result == null || !result.getSuccess()) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0004);
        }
        if(result.getResult() == null){
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1355);
        }
        MemberInfo member = result.getResult();
        return member;
    }

    /**
     * 校验交易密码
     */
    public ServiceResult<Boolean> checkBalancePwdByMemberId(String tradePassword, OrdersBase ordersBase, MemberInfo member){
        ServiceResult<Boolean> checkVal = null;
        log.info("用户中心交易密码：{}， 输入交易密码：{}", member.getBalancePwd(), tradePassword);
        try {
            checkVal = memberShopService.checkBalancePwdByMemberId(tradePassword, ordersBase.getMemberId());
        } catch (Exception e) {
            log.error("订单[{}]支付调用账户系统校验交易密码异常：{}", ordersBase.getOrderSn(), e.getMessage());
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0035, "调用账户系统校验交易密码异常", false);
        }
        log.info("请求用户中心-校验用户交易密码接口返回值：" + checkVal.toString());
        if (checkVal == null || checkVal.getResult() == null) {
            log.error("订单[{}]支付调用账户系统校验交易密码异常：返回值为空", ordersBase.getOrderSn());
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0035, "交易密码错误", false);
        }
        return checkVal;
    }

    /**
     * 调用账户系统 判断是否设置交易密码
     */
    public ServiceResult<MemberInfo> getServiceResultById(Integer memberId) {
        log.info("调用账户系统 判断是否设置交易密码：{}", memberId);
        ServiceResult<MemberInfo> serviceResult = memberShopService.getMemberById(memberId);
        if (serviceResult == null) {
            log.info("调用账户系统 判断是否设置交易密码：{}", TouchApiCode.TOUCH_API_CODE_0004.getMsg());
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0004);
        }
        log.info("调用账户系统 判断是否设置交易密码：{}}", serviceResult.getResult().getBalancePwd());
        return serviceResult;
    }

    /**
     * 用户中心获取同盾分
     */
    public ServiceResult<MemberScoreFraudmetrix> getLastScoreByChannel(Integer memberId){
        return iMemberScoreFraudMetrixService.getLastScoreByChannel(memberId, 1);
    }

    /**
     * 获取用户中心认证数据
     */
    public JsonResult<JzfqAuthInfoDto> getInfoByUserId(Integer memberId){
        AuthResult authResult = new AuthResult();
        authResult.setUserId(memberId);
        authResult.setChannel("1");
        JsonResult<JzfqAuthInfoDto> res = jzfqAuthApi.getInfoByUserId(authResult);
        if (!res.getCode().equals("SUCCESS")) {
            throw new BusinessException("获取认证中心数据失败");
        }
        return res;
    }

}
