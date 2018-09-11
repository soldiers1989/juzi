package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.Areas;
import com.jzfq.retail.bean.domain.AreasQuery;
import com.jzfq.retail.bean.vo.req.AreasSearchReq;
import com.jzfq.retail.bean.vo.req.PurchaseApplyOrderApplyReq;
import com.jzfq.retail.bean.vo.req.PurchaseApplyOrderSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.bean.vo.res.PurchaseApplyOrderListRes;
import com.jzfq.retail.core.api.service.AreasService;
import com.jzfq.retail.core.api.service.PurchaseApplyOrderService;
import com.jzfq.retail.core.dao.AreasMapper;
import com.jzfq.retail.core.dao.PurchaseApplyOrderMapper;
import com.jzfq.retail.core.dao.manual.AreasManualMapper;
import com.jzfq.retail.core.dao.manual.PurchaseApplyOrderManualMapper;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author lizhe lizhe@juzifenqi.com
 * @Date 2018年06月27日 19:42
 * @Description: 城市接口操作实现
 */
@Service
public class PurchaseApplyOrderServiceImpl implements PurchaseApplyOrderService {

    @Autowired
    private PurchaseApplyOrderMapper purchaseApplyOrderMapper;

    @Autowired
    private PurchaseApplyOrderManualMapper purchaseApplyOrderManualMapper;

    @Override
    public ListResultRes<PurchaseApplyOrderListRes> getList(Integer page, Integer pageSize, PurchaseApplyOrderSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = purchaseApplyOrderManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    public void apply(PurchaseApplyOrderApplyReq applyReq) {



    }

    @Override
    public String generateApplySn() {
        return null;
    }

}
