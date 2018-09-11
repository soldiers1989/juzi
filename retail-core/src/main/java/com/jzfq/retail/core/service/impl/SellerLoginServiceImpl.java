package com.jzfq.retail.core.service.impl;

import com.jzfq.retail.bean.domain.Dictionary;
import com.jzfq.retail.bean.domain.SellerLoginOpenIDRel;
import com.jzfq.retail.bean.domain.SellerLoginPermission;
import com.jzfq.retail.bean.vo.req.SellerLoginReq;
import com.jzfq.retail.common.enmu.GpsCheckFlag;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.JwtHelper;
import com.jzfq.retail.common.util.MD5Util;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.*;
import com.jzfq.retail.core.config.GPSHandler;
import com.jzfq.retail.core.config.MemberCenterHandler;
import com.jzfq.retail.core.dao.SellerLoginOpenIDRelMapper;
import com.jzfq.retail.core.service.SystemLogSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: SellerLoginServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年06月29日 18:28
 * @Description: 商户登录操作接口实现
 */
@Slf4j
@Service
public class SellerLoginServiceImpl implements SellerLoginService {

    @Value("${jwt.seller.ttlMillis}")
    private int JWT_TTLMILLIS;

    @Value("${jwt.seller.sec}")
    private String JWT_SEC;

    @Value("${spring.redis.timeout}")
    private int REDIS_TIMEOUT;

    @Value("${touch.test.login}")
    private String testLogin;

    @Value("${touch.test.mobile}")
    private String testMobile;

    @Value("${touch.test.password}")
    private String testPass;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SellerLoginOpenIDRelMapper sellerLoginOpenIDRelMapper;

    @Autowired
    private SellerLoginPermissionService sellerLoginPermissionService;

    @Autowired
    private SystemLogSupport systemLogSupport;

    @Autowired
    private MemberCenterHandler memberCenterHandler;

    @Autowired
    private GPSHandler gpsHandler;

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 商户端小程序登录验证
     * 1、验证手机号、商户店铺编码、密码是否正确
     * 2、验证商户是否冻结状态
     * 3、拿wxCode到微信端换取openID
     * 4、保存openID至seller_login_openID_rel   商户店员登录openID关联表
     * 5、以手机号、商户店铺编码、openID 为key生成token
     * 6、以openID为key ，手机号、商户店铺编码、openID 为value缓存至Redis
     * 7、将token返回给前端  sellerToken
     * <p>
     * sellerCode   BD后台维护的店铺编码
     * sellerMobile BD后台维护的店铺手机号
     * password     BD后台维护的店铺密码
     * wxCode       微信code
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> login(SellerLoginReq req) throws TouchCodeException {
        String openId = memberCenterHandler.wxAuthorizing4Business(req.getWxCode());
        //判断用户登录
        SellerLoginPermission search = new SellerLoginPermission();
        search.setSellerLogin(req.getSellerLogin());
        search.setSellerMobile(req.getSellerMobile());
        search.setSellerPassword(MD5Util.getMD5String(req.getPassword()));
        List<SellerLoginPermission> loginList = sellerLoginPermissionService.getAllList(search);
        //1、验证手机号、商户店铺编码、密码是否正确 用户是否存在且唯一
        if (loginList == null || loginList.size() != 1) {
            //登录失败之后redis记录，并大于N次之后提示不同提示信息
            if (this.loginHandle(openId, req.getSellerLogin(), false)) {
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0020);
            }
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0019);
        }
        search = loginList.get(0);
        //2、查看是否冻结
        if (sellerService.sellerIsFrozen(search.getSellerId())) {
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0012, "本店铺状态已被冻结，请联系运营中心",false);
        }
        if(testLogin.equals(req.getSellerLogin()) && testMobile.equals(req.getSellerMobile()) && testPass.equals(req.getPassword())){
            //小程序审核默认登陆账号
        }else{
            //验证位置是否在指定gps范围内
            gpsHandler.GPSValid(GpsCheckFlag.SELLER_LOGIN, search.getSellerId(), req.getLat(), req.getLng(), "该商户登录未在指定的范围内");
        }
        //4、保存openID至seller_login_openID_rel   商户店员登录openID关联表
        SellerLoginOpenIDRel sellerLoginOpenIDRel = new SellerLoginOpenIDRel();
        sellerLoginOpenIDRel.setLoginPermissionId(search.getId());
        sellerLoginOpenIDRel.setOpenid(openId);
        sellerLoginOpenIDRel.setStatus(0);
        sellerLoginOpenIDRel.setCreateTime(new Date());
        sellerLoginOpenIDRelMapper.insert(sellerLoginOpenIDRel);
        //5、以手机号、商户店铺编码、openID 为key生成token
        StringBuffer tokenStr = new StringBuffer("");
        tokenStr.append(req.getSellerMobile()).append("@").append(req.getSellerLogin()).append("@").append(openId);
        String token = JwtHelper.createJWTSeller(tokenStr.toString(), JWT_TTLMILLIS, JWT_SEC);
        //6、保存到redis，key:openID value:tokenStr
        redisService.setTimesData(openId, tokenStr.toString(), REDIS_TIMEOUT);
        //7、将token返回给前端
        Map<String, Object> map = new HashMap<>();
        map.put("sellerToken", token);
        log.info("商户小程序返回sellerToken：" + token);
        //插入日志
        systemLogSupport.sellerLoginOperationLogSave(search.getSellerId(), search.getSellerName(), search.getSellerMobile(), search.getSellerLogin(), search.getSellerPassword(), req.getWxCode(), openId);
        //登录成功之后删除redis登录错误统计
        this.loginHandle(openId, req.getSellerLogin(), true);
        return map;

    }

    //商户登录次数字典code值
    private final static String SELLER_LOGIN_COUNT_DICT = "SELLER_LOGIN_COUNT";

    @Override
    public boolean loginHandle(String openId, String sellerLogin, boolean loginStatus) {
        //获取redis中的key值
        String redisKey = "seller_Login_" + sellerLogin + "_" + openId;
        String redisVal = redisService.queryStringByKey(redisKey);
        //判断是否是登录失败造成
        if (loginStatus && StringUtils.isNotBlank(redisVal)) {
            //如果登录成功且Redis中有值，进行删除
            redisService.deleteByKey(redisKey);
            log.info("sellerLogin[{}]，登录成功删除redis错误统计", sellerLogin);
        }
        if (!loginStatus) {
            //获取设定次数
            Dictionary dict = dictionaryService.getEntityByCode(SELLER_LOGIN_COUNT_DICT);
            Integer dicVal = (dict != null && StringUtils.isNotBlank(dict.getDictVal())) ? Integer.valueOf(dict.getDictVal()) : 2;
            int loginCount = 1;
            if (StringUtils.isNotBlank(redisVal)) {
                loginCount = Integer.valueOf(redisVal) + 1;
            }
            //更新redis值
            redisService.setForeverData(redisKey, String.valueOf(loginCount));
            log.info("sellerLogin[{}]，登录成功修改redis错误统计：{}", sellerLogin, loginCount);
            //如果值大于N 则返回true
            if (loginCount > dicVal) {
                return true;
            }
        }
        return false;
    }
}
