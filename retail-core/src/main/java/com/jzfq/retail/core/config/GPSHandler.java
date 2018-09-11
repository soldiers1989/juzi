package com.jzfq.retail.core.config;

import com.jzfq.retail.bean.domain.GpsCheckRule;
import com.jzfq.retail.bean.domain.SellerAddress;
import com.jzfq.retail.bean.domain.SellerAddressQuery;
import com.jzfq.retail.common.enmu.GpsCheckFlag;
import com.jzfq.retail.common.enmu.GpsSwitchStatus;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.util.longitude.LatitudeLongitudeUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.GpsCheckRuleService;
import com.jzfq.retail.core.dao.SellerAddressMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Title: GPSHandler
 * @Company: 北京桔子分期电子商务有限公司
 * @Author Li Zhe lizhe@juzifenqi.com
 * @Date 2018年07月17日 10:05
 * @Description:
 */
@Slf4j
@Component
public class GPSHandler {

    @Autowired
    private SellerAddressMapper sellerAddressMapper;

    @Autowired
    private GpsCheckRuleService gpsCheckRuleService;

    /**
     * 验证GPS是否在商户认定范围之内
     *
     * @param gpsCheckFlag 场景
     * @param sellerId     商户id
     * @param lat          纬度
     * @param lng          经度
     * @param msg          提示信息
     */
    public void GPSValid(GpsCheckFlag gpsCheckFlag, Integer sellerId, Double lat, Double lng, String msg) {
        //1.查询GPS_Rule场景是否开启
        GpsCheckRule rule = gpsCheckRuleService.getEntityByFlag(gpsCheckFlag.getFlag());
        if (rule != null && rule.getIsOpen().equals(GpsSwitchStatus.GPS_RULE_OPEN.getStatus())) {
            Integer gpsRange = rule.getRange() == null ? 2000 : rule.getRange();//默认校验2km
            SellerAddressQuery query = new SellerAddressQuery();
            query.createCriteria().andSellerIdEqualTo(sellerId);
            List<SellerAddress> sellerAddressList = sellerAddressMapper.selectByExample(query);
            if (sellerAddressList == null || sellerAddressList.size() != 1) {
                log.error("GPS位置校验，缺少商户[{}]地址GPS", sellerId);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0022);
            }
            SellerAddress sellerAddress = sellerAddressList.get(0);
            //位置校验
            if (LatitudeLongitudeUtil.getDistance(lat, lng, sellerAddress.getLatitude(), sellerAddress.getLongitude()) > gpsRange) {
                log.error("GPS位置校验，商户[{}]校验场景[{}]，指定的范围[{}m]，已超出范围", sellerId, gpsCheckFlag.getFlag(), gpsRange);
                throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_0021, msg, false);
            }
        }
    }
}
