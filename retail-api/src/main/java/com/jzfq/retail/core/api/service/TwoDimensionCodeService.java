package com.jzfq.retail.core.api.service;

import com.jzfq.retail.bean.vo.res.TwoDimensionRes;

import java.util.List;

/**
 * @Author caishijian@juzifenqi.com
 * @Date 2018年08月7日 17:43
 * @Description: 订单影像信息接口
 */
public interface TwoDimensionCodeService {

   List<TwoDimensionRes> getExportData();
}
