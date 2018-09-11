package com.jzfq.retail.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzfq.retail.bean.domain.*;
import com.jzfq.retail.bean.vo.req.SellerSettlePointReq;
import com.jzfq.retail.bean.vo.req.SellerSettlePointSearchReq;
import com.jzfq.retail.bean.vo.res.ListResultRes;
import com.jzfq.retail.common.enmu.ForeignInterfaceStatus;
import com.jzfq.retail.common.enmu.SystemUserStatus;
import com.jzfq.retail.common.enmu.TouchApiCode;
import com.jzfq.retail.common.exception.BusinessException;
import com.jzfq.retail.common.util.TransferUtil;
import com.jzfq.retail.core.api.exception.TouchCodeException;
import com.jzfq.retail.core.api.service.SellerSettlePointService;
import com.jzfq.retail.core.dao.CallAccountsOperationMapper;
import com.jzfq.retail.core.dao.SellerSettlePointMapper;
import com.jzfq.retail.core.dao.SellerSingleCreditMapper;
import com.jzfq.retail.core.dao.manual.SellerSettlePointManualMapper;
import com.jzfq.retail.core.service.CallAccountsOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Title: SellerSettlePointServiceImpl
 * @Company: 北京桔子分期电子商务有限公司
 * @Author caishijian@juzifenqi.com
 * @Date 2018年07月18日 9:53
 * @Description: 商户结算扣点设定表增删改
 */
@Slf4j
@Service
public class SellerSettlePointServiceImpl implements SellerSettlePointService {

    @Autowired
    private SellerSettlePointMapper sellerSettlePointMapper;

    @Autowired
    private SellerSettlePointManualMapper sellerSettlePointManualMapper;

    @Override
    public ListResultRes<Map<String, Object>> findList(Integer page, Integer pageSize, SellerSettlePointSearchReq search) {
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> listPage = sellerSettlePointManualMapper.findList(search);
        return ListResultRes.newListResult(listPage.getResult(), listPage.getTotal(), listPage.getPageNum(), listPage.getPageSize());
    }

    @Override
    public int save(SellerSettlePointReq req) {
        int updateCount = 0;
        List<SellerSettlePoint> sellerSettlePoints = getSellerSettlePoints(req.getSellerId());
        // 库中不存在该商户对应的结算扣点，则新增；否则提示用户结算扣点已存在
        if(CollectionUtils.isEmpty(sellerSettlePoints)) {
            SellerSettlePoint sellerSettlePoint = new SellerSettlePoint();
            sellerSettlePoint.setSellerId(req.getSellerId());
            sellerSettlePoint.setSettlePoint(req.getSettlePoint());
            // sort 字段暂未使用，现阶段都用0占位
            sellerSettlePoint.setSort(0);
            sellerSettlePoint.setStatus(SystemUserStatus.NORMAL.getCode());
            updateCount = sellerSettlePointMapper.insert(sellerSettlePoint);
        } else {
            log.error("新增商户结算扣点异常，异常描述：商户结算扣点已经存在，记录数：{}，商户id：{}",sellerSettlePoints.size(),req.getSellerId());
            throw new TouchCodeException(TouchApiCode.TOUCH_API_CODE_1358);
        }
        return updateCount;
    }

    @Override
    public int update(SellerSettlePointReq req) {
        SellerSettlePoint sellerSettlePoint = sellerSettlePointMapper.selectByPrimaryKey(req.getId());
        TransferUtil.transferIgnoreNull(req, sellerSettlePoint);
        return sellerSettlePointMapper.updateByPrimaryKey(sellerSettlePoint);
    }

    @Override
    public int delete(Integer sellerId) {
        int updateCount = 0;
        List<SellerSettlePoint> sellerSettlePoints = getSellerSettlePoints(sellerId);
        // 如果存在结算扣点且记录数为1，则为正常数据；否则提示用户数据异常
        if(!CollectionUtils.isEmpty(sellerSettlePoints) && sellerSettlePoints.size() == 1) {
            SellerSettlePoint dbSellerSettlePoint = sellerSettlePoints.get(0);
            dbSellerSettlePoint.setStatus(SystemUserStatus.DELETE.getCode());
            updateCount = sellerSettlePointMapper.updateByPrimaryKey(dbSellerSettlePoint);
        } else {
            if(CollectionUtils.isEmpty(sellerSettlePoints)) {
                log.error("删除商户结算扣点异常，异常描述：商户结算扣点不存在！");
                throw new BusinessException("删除商户结算扣点异常，异常描述：商户结算扣点不存在！");
            }
            if(!CollectionUtils.isEmpty(sellerSettlePoints) && sellerSettlePoints.size() != 1) {
                log.error("删除商户结算扣点异常，异常描述：商户结算扣点存在多条记录，记录数：{}，商户id：{}",sellerSettlePoints.size(),sellerId);
                throw new BusinessException("删除商户结算扣点异常，异常描述：商户结算扣点存在多条记录！");
            }
        }
        return updateCount;
    }

    /**
     * 根据商户id查询对应的结算扣点
     * @param sellerId
     * @return
     */
    private List<SellerSettlePoint> getSellerSettlePoints(Integer sellerId) {
        // 查询该商户是否已经设置了结算扣点
        SellerSettlePointQuery query = new SellerSettlePointQuery();
        query.or().andStatusEqualTo(SystemUserStatus.NORMAL.getCode())
                .andSellerIdEqualTo(sellerId);
        return sellerSettlePointMapper.selectByExample(query);
    }
}
